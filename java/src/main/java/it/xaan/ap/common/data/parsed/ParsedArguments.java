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
package it.xaan.ap.common.data.parsed;

import it.xaan.ap.common.data.Argument;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Represents a number of parsed arguments,
 */
public interface ParsedArguments {
  /**
   * Returns whether or not the {@link Argument} exists in the parsed arguments.
   *
   * @param argument The argument to check against.
   *
   * @return {@code false} if {@link #get(Argument)} would return null, {@code true} otherwise.
   */
  default boolean exists(Argument<?> argument) {
    return getOpt(argument).isPresent();
  }

  /**
   * Gets the value of the {@link Argument}, wrapped in an {@link Optional}.
   *
   * @param argument The argument to get the value of.
   * @param <T>      The type of the Argument.
   *
   * @return An empty Optional if {@link #exists(Argument)} would return false, otherwise an Optional containing
   * the value.
   */
  default <T> Optional<T> getOpt(Argument<T> argument) {
    return Optional.ofNullable(get(argument));
  }

  /**
   * Gets the value of the {@link Argument}, possibly null.
   *
   * @param argument The Argument to check against.
   * @param <T>      The type of the Argument.
   *
   * @return {@code null} if {@link #exists(Argument)} returns false, otherwise the value.
   */
  @Nullable
  <T> T get(Argument<T> argument);
}
