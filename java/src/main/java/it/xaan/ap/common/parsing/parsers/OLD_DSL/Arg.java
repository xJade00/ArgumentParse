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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an argument for the DSL.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Arg {
  /**
   * Represents the name of the argument. If this isn't set or is set to an empty string,
   * the name of the field is used. Default is an empty string.
   *
   * @return The name of the argument.
   */
  String name() default "";

  /**
   * Represents if the argument has a default value. If this isn't set, but there is meant
   * to be a default value, the parser will assume there is no default value and will error
   * accordingly. This does not have to be set if the type of the field is {@link java.util.Optional}
   * as the assumed default for that is {@link java.util.Optional#empty()}.
   *
   * @return If the arg has a default value.
   */
  boolean hasDefault() default false;
}
