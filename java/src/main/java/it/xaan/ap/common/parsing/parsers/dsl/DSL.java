/*
 * ArgumentParse - Parsing CLI arguments in Java.
 * Copyright © 2020 xaanit (shadowjacob1@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package it.xaan.ap.common.parsing.parsers.dsl;

import it.xaan.ap.common.data.Argument;
import it.xaan.ap.common.data.parsed.ParsedArguments;
import it.xaan.ap.common.parsing.Parser;
import it.xaan.ap.common.parsing.options.Options;
import it.xaan.ap.common.parsing.options.OptionsBuilder;
import it.xaan.random.core.Pair;
import it.xaan.random.result.Result;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused", "VoidReturn"})
public final class DSL<R extends ParsedArguments, C> {
  private final Parser<R> backing;
  private final Class<C> clazz;

  private final List<Pair<Class<?>, Argument<?>>> parameters = new ArrayList<>();
  private final Map<Argument<?>, Object> defaults = new HashMap<>();

  private Constructor<C> constructor;
  private Object[] values = new Object[0];

  private boolean cache;
  private C cached;

  private Options options = new OptionsBuilder().build();
  private String content;


  private DSL(Parser<R> backing, Class<C> clazz) {
    this.backing = backing;
    this.clazz = clazz;
  }

  public static <R extends ParsedArguments, C> DSL<R, C> from(Parser<R> parser, Class<C> clazz) {
    return new DSL<>(parser, clazz);
  }

  public <T> DSL<R, C> backup(Argument<T> argument, @Nullable T value) {
    this.defaults.put(argument, value);
    return this;
  }

  @SafeVarargs
  public final <T> DSL<R, C> backup(Pair<Argument<T>, T>... pairs) {
    for (Pair<Argument<T>, T> pair : pairs) {
      Argument<T> argument = pair.getFirst();
      T value = pair.getSecond();
      Objects.requireNonNull(argument, "argument");
      backup(argument, value);
    }
    return this;
  }

  private <T> boolean containsWhere(Collection<T> collection, Predicate<T> pred) {
    for (T element : collection) {
      if (pred.test(element)) {
        return true;
      }
    }
    return false;
  }

  private <T> int indexWhere(List<T> collection, Predicate<T> pred) {
    for (int i = 0; i < collection.size(); i++) {
      if (pred.test(collection.get(i))) {
        return i;
      }
    }
    return -1;
  }

  public DSL<R, C> bindNamed(Argument<?>... args) {
    List<Argument<?>> arguments = Arrays.asList(args);
    List<Pair<String, Class<?>>> named = arguments.stream()
      .map(arg -> Pair.<String, Class<?>>from(arg.getName(), arg.getType().getClazz()))
      .collect(Collectors.toList());

    Arrays.stream(this.clazz.getConstructors())
      .filter(x -> x.getParameterTypes().length == named.size())
      .filter(x -> Arrays.stream(x.getParameters()).allMatch(parameter -> {
        Class<?> clazz = parameter.getType();
        String name = parameter.isNamePresent() ? parameter.getName() : parameter.getAnnotation(Parameter.class).name();
        return containsWhere(named, pair -> {
          String argName = pair.getFirst();
          Class<?> argClass = pair.getSecond();
          return name.equalsIgnoreCase(argName) && clazz == argClass;
        });
      }))
      .filter(x -> {
        if (Arrays.stream(x.getParameters()).allMatch(java.lang.reflect.Parameter::isNamePresent)) {
          return true;
        } else {
          return Arrays.stream(x.getParameterAnnotations()).allMatch(arr -> Arrays.stream(arr).anyMatch(annotation -> annotation.annotationType() == Parameter.class));
        }
      })
      .map(ctor ->
        named.stream().sorted(
          Comparator.comparingInt(pair ->
            indexWhere(
              Arrays.asList(ctor.getParameters()),
              param -> {
                if (Arrays.stream(ctor.getParameters()).allMatch(java.lang.reflect.Parameter::isNamePresent)) {
                  return param.getName().equalsIgnoreCase(pair.getFirst());
                } else {
                  return param.getAnnotation(Parameter.class).name().equalsIgnoreCase(pair.getFirst());
                }
              }
            )
          )
        )
          .map(pair -> arguments.stream().filter(arg -> arg.getName().equals(pair.getFirst())).findFirst().orElse(null))
          .collect(Collectors.toList())
      )
      .findAny()
      .orElseThrow(() -> new InvalidConstructorException(arguments))
      .forEach(this::bind);
    return this;
  }

  public DSL<R, C> bind(Argument<?>... arguments) {
    for (Argument<?> argument : arguments) {
      this.parameters.add(Pair.from(argument.getType().getClazz(), argument));
    }
    return this;
  }

  public DSL<R, C> resetBindings() {
    this.parameters.clear();
    return this;
  }

  public DSL<R, C> options(Options options) {
    this.options = options;
    this.values = new Object[0];
    return this;
  }

  public DSL<R, C> content(String content) {
    this.content = content;
    this.values = new Object[0];
    return this;
  }

  public DSL<R, C> cache() {
    this.cache = true;
    return this;
  }

  @Nullable
  public C validate() {
    try {
      if (this.cache && this.cached != null) {
        return this.cached;
      }

      if (this.values.length == this.parameters.size() && this.constructor != null) {
        C instance = this.constructor.newInstance(this.values);
        if (this.cache) {
          this.cached = instance;
        }
        return instance;
      }

      if (this.parameters.isEmpty()) {
        this.constructor = this.clazz.getConstructor();
        return validate();
      }

      Class<?>[] classes = this.parameters.stream().map(Pair::getFirst).toArray(Class[]::new);
      this.constructor = this.clazz.getConstructor(classes);

      Result<R> result = this.backing.parse(this.parameters.stream().map(Pair::getSecond).collect(Collectors.toList()), this.content, this.options);

      if (result.isError()) {
        throw result.getError(Object.class)
          .<RuntimeException>map(err -> {
            String message = "There was an error while parsing. ";
            if (err instanceof Exception) {
              return new FailedParseException(message, (Exception) err);
            } else {
              return new FailedParseException(message + "Caused by " + err);
            }
          })
          .orElseGet(() -> new RuntimeException("You should never see this"));
      }

      if (result.isEmpty()) {
        return null;
      }

      R value = result.get();

      this.values = new Object[this.parameters.size()];
      for (int i = 0; i < this.parameters.size(); i++) {
        Class<?> clazz = this.parameters.get(i).getFirst();
        Argument<?> argument = this.parameters.get(i).getSecond();
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(argument, "argument");
        Object o = value.get(argument);
        if (o == null) {
          if (clazz == Optional.class) {
            o = Optional.empty();
          } else if (this.defaults.containsKey(argument)) {
            o = this.defaults.get(argument);
          } else {
            throw new ValidationException("Couldn't find value for argument: " + argument.getName());
          }
        }

        if (o != Optional.empty() && clazz == Optional.class) {
          o = Optional.of(o);
        }

        this.values[i] = o;
      }
      return validate();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
      // Can't create instance?
      ex.printStackTrace();
    } catch (NoSuchMethodException ex) {
      // No constructor.
      ex.printStackTrace();
    }
    return null;
  }

  public Optional<C> validateOpt() {
    try {
      return Optional.ofNullable(validate());
    } catch (Exception ex) {
      return Optional.empty();
    }
  }

  public Result<C> validateResult() {
    try {
      return validateOpt().map(Result::of).orElse(Result.empty());
    } catch (Exception ex) {
      return Result.error(ex);
    }
  }

}
