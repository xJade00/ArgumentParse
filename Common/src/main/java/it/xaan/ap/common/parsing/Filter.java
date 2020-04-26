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

import java.util.function.Function;

/**
 * Represents a function that takes in a string and filters out any unnecessary noise that may
 * appear making it harder to validate and convert the content to the type specified in a {@link
 * Type}.
 */
public interface Filter extends Function<String, String> {

  /**
   * Trims off any leading spaces. <br>
   * Alias for {@link String#trim()}.
   */
  Filter TRIM_SPACES = String::trim;

  /**
   * This trims any newlines that may occur at the end of the string. Newlines are defined as '\r'
   * or '\n'.
   */
  Filter TRIM_NEWLINES =
    str -> {
      str = new StringBuilder(str).reverse().toString();
      while (str.length() > 0 && (str.charAt(0) == '\r' || str.charAt(0) == '\n')) {
        str = str.substring(1);
      }
      return new StringBuilder(str).reverse().toString();
    };

  /**
   * Converts the passed {@link String} into one that is more usable for parsing. <br>
   * See {@link Function#apply(Object)}.
   */
  default String filter(String str) {
    return apply(str);
  }
}
