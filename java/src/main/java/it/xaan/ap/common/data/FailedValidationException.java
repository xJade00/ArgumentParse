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
package it.xaan.ap.common.data;

/**
 * Represents an exceptional state where the type couldn't validate an argument.
 */
@SuppressWarnings("unused")
public final class FailedValidationException extends RuntimeException {
  private final UnvalidatedArgument argument;

  /**
   * Creates a new {@link FailedValidationException} out of the following argument and input.
   *
   * @param argument The argument that couldn't be validated.
   */
  public FailedValidationException(final UnvalidatedArgument argument) {
    super(String.format("Couldn't validated argument %s.", argument));
    this.argument = argument;
  }

  /**
   * @return The argument that couldn't be validated.
   */
  public UnvalidatedArgument getArgument() {
    return this.argument;
  }
}
