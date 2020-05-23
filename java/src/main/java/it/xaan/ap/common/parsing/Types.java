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
package it.xaan.ap.common.parsing;

import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.random.result.Result;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a list of default types.
 */
@SuppressWarnings("unused")
public final class Types {

  /**
   * Type for {@link Integer}
   */
  public static final Type<Integer> INTEGER_TYPE = createNumberType(Integer::parseInt, int.class);
  /**
   * Type for {@link Long}
   */
  public static final Type<Long> LONG_TYPE = createNumberType(Long::parseLong, long.class);
  /**
   * Type for {@link Byte}
   */
  public static final Type<Byte> BYTE_TYPE = createNumberType(Byte::parseByte, byte.class);
  /**
   * Type for {@link Short}
   */
  public static final Type<Short> SHORT_TYPE = createNumberType(Short::parseShort, short.class);
  /**
   * Type for {@link Boolean}
   */
  public static final Type<Boolean> BOOLEAN_TYPE = new Type<>(
    (unvalidated) -> unvalidated.equals("true") || unvalidated.equals("false"),
    (unconverted) -> unconverted.equals("true"),
    "true|false",
    boolean.class,
    Filter.TRIM_NEWLINES,
    Filter.TRIM_SPACES
  );
  /**
   * Type for {@link Character}
   */
  public static final Type<Character> CHARACTER_TYPE = new Type<>(
    (unvalidated) -> unvalidated.startsWith("'") && unvalidated.endsWith("'") && unvalidated.length() == 3,
    (unconverted) -> unconverted.charAt(1),
    "'.'",
    char.class,
    Filter.TRIM_SPACES,
    Filter.TRIM_NEWLINES
  );
  /**
   * Type for {@link Void}. This MUST be used for void arguments.
   */
  // Void is special, it never gets passed anything. We want an explicit exception if it is.
  public static final Type<Void> VOID_TYPE = new Type<>(
    (unvalidated) -> {
      throw new IllegalStateException("Void type predicate called. Please make an issue.");
    },
    (unconverted) -> {
      throw new IllegalStateException("Void type function called. Please make an issue.");
    },
    null,
    Void.class
  );
  private static final String STRING_REGEX = "\"(\\\\.|[^\\\\\"])*\"";
  private static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);
  /**
   * Type for {@link String}
   */
  public static final Type<String> STRING_TYPE = new Type<>(
    (unvalidated) -> STRING_PATTERN.matcher(unvalidated).matches(),
    (unconverted) -> unconverted.substring(1, unconverted.length() - 1).replaceAll("\\\\(.)", "$1"),
    STRING_REGEX,
    String.class,
    Filter.TRIM_NEWLINES,
    Filter.TRIM_SPACES
  );
  private static final String FLOATING_POINT_REGEX = "-?\\d*.?\\d*";
  /**
   * Type for {@link Float}
   */
  public static final Type<Float> FLOAT_TYPE = createNumberType(Float::parseFloat, FLOATING_POINT_REGEX, float.class);
  /**
   * Type for {@link Double}
   */
  public static final Type<Double> DOUBLE_TYPE = createNumberType(Double::parseDouble, FLOATING_POINT_REGEX, Double.class);
  @SuppressWarnings("rawtypes")
  private static final Map<Type, Type> types = new HashMap<>();

  private Types() {
    throw new IllegalAccessError("You can not create an instance of Types.");
  }

  /**
   * Creates a new Type<List<T>> out of the {@link Type} passed.
   *
   * @param type The type to make it out of.
   *
   * @return The instance of the list type for the passed type.
   */
  @SuppressWarnings("unchecked")
  public static <T> Type<List<T>> createListType(Type<T> type) {

    return (Type<List<T>>) types.compute(type, ($, value) -> {
      if (value != null) return value;
      String regex = String.format("\\[(?:\\s*%s)(?:\\s*,\\s*%s)*\\s*\\]", type.getRegex(), type.getRegex());
      return new Type<>(
        (unvalidated) -> unvalidated.startsWith("[") && unvalidated.endsWith("]") && unvalidated.matches(regex),
        (unconverted) -> {
          List<T> elements = new ArrayList<>();
          Pattern inner = Pattern.compile(type.getRegex());
          Matcher matcher = inner.matcher(unconverted.substring(1, unconverted.length() - 1));
          while (matcher.find()) {
            String element = matcher.group(0);
            Result<T> result = type.decode(UnvalidatedArgument.from("", element));
            if (result.isSuccess()) {
              elements.add(result.get());
            } else {
              throw new RuntimeException("");
            }
          }
          return elements;
        },
        regex,
        List.class,
        Filter.TRIM_NEWLINES,
        Filter.TRIM_SPACES
      );
    });
  }

  private static <T> List<T> create(Type<T> type, Pattern pattern, String str) {
    if (!str.startsWith("[") || !str.endsWith("]") || !pattern.matcher(str).matches()) {
      throw new IllegalArgumentException("Incorrect format");
    }

    str = str.substring(1, str.length() - 1);
    List<T> elements = new ArrayList<>();
    Pattern inner = Pattern.compile(type.getRegex());
    Matcher matcher = inner.matcher(str);
    while (matcher.find()) {
      String element = matcher.group(0);
      System.out.println("ELEMENT: " + element);
      Result<T> result = type.decode(UnvalidatedArgument.from("", element));
      if (result.isSuccess()) {
        elements.add(result.get());
      } else {
        throw new RuntimeException("");
      }
    }
    return elements;
  }


  private static boolean successfulConversion(Consumer<String> consumer, String str) {
    try {
      consumer.accept(str);
      return true;
    } catch (Exception ignored) {
      return false;
    }
  }

  private static <T> Type<T> createNumberType(Function<String, T> function, Class<T> clazz, Filter... filters) {
    return createNumberType(function, "-?\\d+", clazz, filters);
  }

  private static <T> Type<T> createNumberType(Function<String, T> function, String regex, Class<T> clazz, Filter... filters) {
    return new Type<>(
      (unvalidated) -> successfulConversion(function::apply, unvalidated),
      function,
      regex,
      clazz, new ArrayList<Filter>(Arrays.asList(filters)) {{
      add(Filter.TRIM_NEWLINES);
      add(Filter.TRIM_SPACES);
    }}.stream().distinct().toArray(Filter[]::new)
    );
  }


}
