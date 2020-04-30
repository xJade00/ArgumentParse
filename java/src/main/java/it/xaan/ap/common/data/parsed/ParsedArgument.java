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

import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Represents a ParsedArgument that has been validated.
 *
 * @param <T> The type of the Argument.
 */
public final class ParsedArgument<T> {
  @Nullable
  private final String name;
  @Nullable
  private final T value;

  /**
   * Creates a new ParsedArgument with the name and value.
   *
   * @param name  The nullable name, this should only be null for things like indexed arguments.
   * @param value The value, this should only be null for things like {@code --help} which takes in no value.
   *
   * @throws IllegalArgumentException When both name and value are null.
   */
  public ParsedArgument(@Nullable String name, @Nullable T value) {
    if (name == null && value == null) {
      throw new IllegalArgumentException("One of name or value must be non null.");
    }
    this.name = name;
    this.value = value;
  }

  /**
   * The name of the argument. This should only be null for things lke indexed arguments.
   *
   * @return The possibly-null name.
   */
  @Nullable
  public String getName() {
    return this.name;
  }

  /**
   * The value of the argument. This should only be null for things like {@code --help} which takes in no value.
   *
   * @return The possibly-null value.
   */
  @Nullable
  public T getValue() {
    return this.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof ParsedArgument)) return false;
    ParsedArgument<?> other = (ParsedArgument<?>) obj;
    return Objects.equals(this.name, other.name) && Objects.equals(this.value, other.value);
  }

  @Override
  public String toString() {
    return String.format("ParsedArgument[name=%s,value=%s]", this.name, this.value);
  }
}
