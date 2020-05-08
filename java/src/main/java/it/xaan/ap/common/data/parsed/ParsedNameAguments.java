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
import it.xaan.ap.common.parsing.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Represents a list of ParsedArguments by name.
 */
public final class ParsedNameAguments implements ParsedArguments {
  private final Map<String, Object> backing = new HashMap<>();

  /**
   * Creates a new ParsedArguments instance based on the arguments passed.
   *
   * @param args The arguments to add.
   *
   * @throws IllegalArgumentException When an argument with a null name is passed.
   */
  public ParsedNameAguments(List<ParsedArgument<?>> args) {
    for (ParsedArgument<?> arg : args) {
      if (arg.getName() == null) {
        throw new IllegalArgumentException("Arguments can not have null names.");
      }
      this.backing.put(arg.getName(), arg.getValue());
    }
  }

  /**
   * See {@link ParsedArguments#exists(Argument)}. This has special handling for the void type.
   *
   * @param argument The argument to check against.
   *
   * @return If the argument's type is of {@link Types#VOID_TYPE}, if the backing map contains the key, otherwise false.
   * If the type isn't void, see {@link ParsedArguments#exists(Argument)};
   */
  @Override
  public boolean exists(Argument<?> argument) {
    return argument.getType() == Types.VOID_TYPE ? this.backing.containsKey(argument.getName()) : getOpt(argument).isPresent();
  }

  /**
   * See {@link ParsedArguments#get(Argument)}. This will always return null for VOID_TYPE.
   *
   * @param argument The Argument to check against.
   *
   * @return null if the type is {@link Types#VOID_TYPE}, otherwise see {@link ParsedArguments#get(Argument)}.
   */
  @Override
  @Nullable
  @SuppressWarnings({"unchecked", "NullableProblems"})
  public <T> T get(Argument<T> argument) {
    try {
      return argument.getType() == Types.VOID_TYPE ? null : (T) this.backing.get(argument.getName());
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
    if (!(obj instanceof ParsedNameAguments)) return false;
    ParsedNameAguments other = (ParsedNameAguments) obj;
    return Objects.equals(this.backing, other.backing);
  }

  @Override
  public String toString() {
    return String.format("ParsedArguments[backing=%s]", this.backing);
  }
}
