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

import java.lang.reflect.Field;

/**
 * Represents an exceptional state where there isn't a suitable default value for a field,
 */
@SuppressWarnings("unused")
public final class MissingValueException extends RuntimeException {
  private final Field field;

  public MissingValueException(Field field) {
    super("Missing argument for field: " + field);
    this.field = field;
  }

  public Field getField() {
    return this.field;
  }
}
