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
package it.xaan.ap.common.parsing.parsers.OLD_DSL;

import it.xaan.ap.common.data.Argument;
import it.xaan.ap.common.data.MissingArgumentsException;
import it.xaan.ap.common.data.parsed.ParsedArguments;
import it.xaan.ap.common.parsing.Parser;
import it.xaan.ap.common.parsing.options.Options;
import it.xaan.random.core.Pair;
import it.xaan.random.result.Result;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: Not make this a parser, instead a static wrapper class.

/**
 * A DSL parser that parses arguments into a new instance of a class. It is important to note
 * that {@link Argument#isRequired()} is completely disregarded with this class. An argument
 * is considered optonal under one of two circumstances: <br>
 * - It's type is {@code Optional<T>}, in which case it has a default of {@link Optional#empty()} <br>
 * - {@link Arg#hasDefault()} is set to true. <br>
 * <br>
 *
 * @param <T> The class to initialise.
 * @param <U> The parser to use.
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public final class DSLParser<T, U extends ParsedArguments> implements Parser<T> {
  private final Class<? extends T> clazz;
  private final Parser<U> underlying;
  private final List<Pair<Class<?>, Argument<?>>> constructor;

  /**
   * Creates a new DSLParser.
   *
   * @param clazz The class to inject into.
   * @param underlying The underlying parser to use to parse the content.
   * @param constructor The constructor to use
   */
  public DSLParser(Class<? extends T> clazz, Parser<U> underlying, List<Pair<Class<?>, Argument<?>>> constructor) {
    this.clazz = clazz;
    this.underlying = underlying;
    this.constructor = constructor == null ? new ArrayList<>() : constructor;
  }

  public DSLParser(Class<? extends T> clazz, Parser<U> underlying) {
    this(clazz, underlying, new ArrayList<>());
  }

  @Override
  public Result<T> parse(Collection<Argument<?>> arguments, String content, Options options) {
    try {
      List<Field> fields = Arrays.stream(this.clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Arg.class))
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .collect(Collectors.toList());

      if (fields.isEmpty()) {
        return Result.empty();
      }

      Result<U> result = this.underlying.parse(arguments, content, options);

      if (result.isEmpty()) return Result.empty();
      if (result.isError()) return Result.error(result.getError());

      U parsed = result.get();

      final Map<Field, DslMetadata> metadata = new HashMap<>();

      for (Field field : fields) {
        Arg annotation = field.getAnnotation(Arg.class);
        boolean optional = field.getType() == Optional.class;
        metadata.put(field, new DslMetadata(optional, annotation.hasDefault()));
      }

      final Map<Field, Argument<?>> mapped = new HashMap<>();
      final Map<Field, Object> values = new HashMap<>();
      for (Field field : fields) {
        Argument<?> arg = arguments.stream()
          .filter(argument -> {
            String name = field.getAnnotation(Arg.class).name();
            name = name.equals("") ? field.getName() : name;
            return argument.getName().equalsIgnoreCase(name);
          })
          .findAny().orElse(null);
        if (arg == null) {
          DslMetadata data = metadata.get(field);
          if (data.isOptional()) {
            values.put(field, Optional.empty());
          } else {
            if (!data.hasDefault()) {
              throw new MissingValueException(field);
            }
          }
        }
        mapped.put(field, arg);
      }


      for (Entry<Field, Argument<?>> entry : mapped.entrySet()) {
        Field field = entry.getKey();
        Argument<?> argument = entry.getValue();
        DslMetadata data = metadata.get(field);
        if (argument == null || !parsed.exists(argument)) {
          if (data.isOptional()) {
            values.put(field, Optional.empty());
            continue;
          } else {
            throw new MissingArgumentsException(Collections.singletonList(argument));
          }
        }
        Object o = parsed.get(argument);
        values.put(field, data.isOptional() ? Optional.of(o) : o);
      }

      T instance;
      if (this.constructor.isEmpty()) {
        instance = this.clazz.newInstance();
        for (Entry<Field, Object> entry : values.entrySet()) {
          Field field = entry.getKey();
          Object value = entry.getValue();
          if (!Modifier.isFinal(field.getModifiers())) {
            field.set(instance, value);
          }
        }
      } else {
        List<Pair<Argument<?>, Object>> argValues = new ArrayList<>();
        for (Entry<Field, Object> entry : values.entrySet()) {
          Argument<?> arg = mapped.get(entry.getKey());
          argValues.add(Pair.from(arg, entry.getValue()));
        }

        argValues.sort(Comparator.comparingInt(pair -> indexWhere(this.constructor, pair.getFirst())));

        instance = this.clazz.getConstructor(this.constructor.stream().map(Pair::getFirst).toArray(Class[]::new))
          .newInstance(argValues.stream().map(Pair::getSecond).toArray());
      }
      return Result.of(instance);
    } catch (Exception ex) {
      return Result.error(ex);
    }
  }


  private <K, V> int indexWhere(List<Pair<K, V>> list, V value) {
    for (int i = 0; i < list.size(); i++) {
      if (value.equals(list.get(i).getSecond()))
        return i;
    }
    return -1;
  }
}
