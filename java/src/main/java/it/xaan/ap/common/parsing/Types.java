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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class Types {

  /* TODO: ALL OF THESE
   * - {@link List} <br>
   */

  public static final Type<Integer> INTEGER_TYPE = createNumberType(Integer::parseInt);
  public static final Type<Long> LONG_TYPE = createNumberType(Long::parseLong);
  public static final Type<Byte> BYTE_TYPE = createNumberType(Byte::parseByte);
  public static final Type<Short> SHORT_TYPE = createNumberType(Short::parseShort);
  public static final Type<Float> FLOAT_TYPE = createNumberType(Float::parseFloat);
  public static final Type<Boolean> BOOLEAN_TYPE = new Type<>(
    (unvalidated) -> unvalidated.equals("true") || unvalidated.equals("false"),
    (unconverted) -> unconverted.equals("true"),
    "true|false",
    Filter.TRIM_NEWLINES,
    Filter.TRIM_SPACES
  );
  public static final Type<Character> CHARACTER_TYPE = new Type<>(
    (unvalidated) -> unvalidated.length() == 3 && unvalidated.startsWith("'") && unvalidated.endsWith("'"),
    (unconverted) -> unconverted.charAt(1),
    "'.'",
    Filter.TRIM_SPACES,
    Filter.TRIM_NEWLINES
  );
  // Void is special, it never gets passed anything. We want an explicit exception if it is.
  public static final Type<Void> VOID_TYPE = new Type<>(
    (unvalidated) -> {
      throw new IllegalStateException("Void type predicate called. Please make an issue.");
    },
    (unconverted) -> {
      throw new IllegalStateException("Void type function called. Please make an issue.");
    },
    null
  );
  private static final String FLOATING_POINT_REGEX = "\\d*.?\\d*";
  public static final Type<Double> DOUBLE_TYPE = createNumberType(Double::parseDouble, FLOATING_POINT_REGEX);
  private static final String STRING_REGEX = "\"(\\\\\"|[^\"])*[^\\\\]\"";
  private static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);
  public static final Type<String> STRING_TYPE = new Type<>(
    (unvalidated) -> STRING_PATTERN.matcher(unvalidated).matches(),
    (unconverted) -> unconverted.substring(1, unconverted.length() - 1).replace("\\\"", "\""),
    STRING_REGEX,
    Filter.TRIM_NEWLINES,
    Filter.TRIM_SPACES
  );

  private Types() {
    throw new IllegalAccessError("You can not create an instance of Types.");
  }

  private static boolean successfulConversion(Consumer<String> consumer, String str) {
    try {
      consumer.accept(str);
      return true;
    } catch (Exception ignored) {
      return false;
    }
  }

  private static <T> Type<T> createNumberType(Function<String, T> function, Filter... filters) {
    return createNumberType(function, "\\d+", filters);
  }

  private static <T> Type<T> createNumberType(Function<String, T> function, String regex, Filter... filters) {
    return new Type<>(
      (unvalidated) -> successfulConversion(function::apply, unvalidated),
      function,
      regex,
      new ArrayList<Filter>(Arrays.asList(filters)) {{
        add(Filter.TRIM_NEWLINES);
        add(Filter.TRIM_SPACES);
      }}.stream().distinct().toArray(Filter[]::new)
    );
  }


}
