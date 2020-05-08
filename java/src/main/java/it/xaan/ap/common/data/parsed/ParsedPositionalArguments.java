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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Represents a list of arguments based on their position.
 */
public final class ParsedPositionalArguments implements ParsedArguments {
  private final List<ParsedArgument<?>> list;

  /**
   * Creates a new {@link ParsedPositionalArguments}.
   *
   * @param list The list of arguments.
   */
  @SuppressWarnings("ConstantConditions")
  public ParsedPositionalArguments(List<ParsedArgument<?>> list) {
    this.list = list.stream().sorted(Comparator.comparing(ParsedArgument::getName)).collect(Collectors.toList());
  }

  @Nullable
  @SuppressWarnings({"unchecked", "NullableProblems"})
  public <T> T get(Argument<T> argument) {
    int index = Integer.parseInt(argument.getName());
    if (index >= this.list.size()) {
      return null;
    }
    try {
      return (T) this.list.get(index);
    } catch (ClassCastException ignored) {
      throw new ClassCastException("You should not be seeing this. If you do, please open a bug report. Tried to cast to " +
        argument.getName() + " with the value in the backing map being " + this.list);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.list);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof ParsedPositionalArguments)) return false;
    ParsedPositionalArguments other = (ParsedPositionalArguments) obj;

    return this.list.equals(other.list);
  }

  @Override
  public String toString() {
    return String.format("ParsedPotionalArguments[list=%s]", this.list);
  }
}
