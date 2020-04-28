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

import it.xaan.ap.common.parsing.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Represents a list of ParsedArguments by name.
 */
@SuppressWarnings("WeakerAccess")
public final class ParsedArguments {
  private final Map<String, Object> backing = new HashMap<>();

  /**
   * Creates a new ParsedArguments instance based on the arguments passed.
   *
   * @param args The arguments to add.
   *
   * @throws IllegalArgumentException When an argument with a null name is passed.
   */
  public ParsedArguments(List<ParsedArgument<?>> args) {
    for (ParsedArgument<?> arg : args) {
      if (arg.getName() == null) {
        throw new IllegalArgumentException("Arguments can not have null names.");
      }
      this.backing.put(arg.getName(), arg.getValue());
    }
  }

  /**
   * Returns whether or not the {@link Argument} exists in the parsed arguments.
   *
   * @param argument The argument to check against.
   *
   * @return {@code false} if {@link #get(Argument)} would return null, {@code true} otherwise.
   */
  public boolean exists(Argument<?> argument) {
    return argument.getType() == Types.VOID_TYPE ? this.backing.containsKey(argument.getName()) : getOpt(argument).isPresent();
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
  public <T> Optional<T> getOpt(Argument<T> argument) {
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
  @SuppressWarnings({"unchecked", "ConstantConditions"})
  public <T> T get(Argument<T> argument) {
    try {
      return argument.getType() == Types.VOID_TYPE ? null : (T) this.backing.get(argument.getName().toLowerCase());
    } catch (ClassCastException ignored) {
      throw new ClassCastException("You should not be seeing this. If you do, please open a bug report. Tried to cast to " +
        argument.getName() + " with the value in the backing map being " + this.backing.get(argument.getName()));

    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.backing);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof ParsedArguments)) return false;
    ParsedArguments other = (ParsedArguments) obj;
    return Objects.equals(this.backing, other.backing);
  }

  @Override
  public String toString() {
    return String.format("ParsedArguments[backing=%s]", this.backing);
  }
}
