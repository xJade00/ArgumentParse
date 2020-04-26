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
 * Represents an unvalidated {@link Argument}. It contains the unique name of the Argument and the
 * extracted unvalidated value from the content. This value is still in it's {@link String} format
 * and needs to be converted with {@link it.xaan.ap.common.parsing.Type#decode(UnvalidatedArgument)}
 */
public class UnvalidatedArgument {

  private final String name;
  private final String value;

  /**
   * Constructs a new {@link UnvalidatedArgument}. This is simply a holder for data.
   *
   * @param name  The unique name of the {@link Argument}.
   * @param value The unvalidated, unfiltered value from the content.
   */
  private UnvalidatedArgument(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new {@link UnvalidatedArgument}. This is simply a holder for data.
   *
   * @param name  The unique name of the {@link Argument}.
   * @param value The unvalidated, unfiltered value from the content.
   */
  public static UnvalidatedArgument from(final String name, final String value) {
    return new UnvalidatedArgument(name, value);
  }

  /**
   * Gets the unique name of the {@link Argument} this is validating for.
   *
   * @return The never-null name representing the {@link Argument}.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the unvalidated value extracted from the content.
   *
   * @return The unvalidated value extracted from the content.
   */
  public String getValue() {
    return this.value;
  }

  @Override
  public int hashCode() {
    return getName().hashCode() + getValue().hashCode();
  }

  @Override
  public String toString() {
    return String.format("UnvalidatedArgument[name=%s,value=%s]", getName(), getValue());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof UnvalidatedArgument)) {
      return false;
    }
    UnvalidatedArgument other = (UnvalidatedArgument) obj;
    return getName().equals(other.getName()) && getValue().equals(other.getValue());
  }
}
