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

import it.xaan.ap.common.parsing.Type;
import it.xaan.ap.common.parsing.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a possible Argument.
 *
 * @param <T> The type of the argument.
 */
public class Argument<T> {
  public static final String NAME_REGEX = "[a-zA-Z_0-9]+";
  private final Type<T> type;
  private final String name;
  private final boolean required;
  private final boolean voided;

  /**
   * Creates a new Argument. The name, if not null, must match {@link #NAME_REGEX}.
   *
   * @param type     The type of the argument.
   * @param name     The name of the argument. Can be null for things like indexed arguments. This will be
   *                 lowercased for ease of parsing.
   * @param required If the argument is required or not.
   */
  public Argument(Type<T> type, String name, boolean required) {
    if (!name.matches(NAME_REGEX)) {
      throw new IllegalArgumentException("Names must match " + NAME_REGEX);
    }
    this.type = type;
    this.name = name.toLowerCase();
    this.required = required;
    // Void types MUST use this specific type. People can make their own for everything else.
    this.voided = type == Types.VOID_TYPE;
  }

  public static Set<Argument<?>> set(Argument<?>... elements) {
    return new HashSet<>(Arrays.asList(elements));
  }

  public Type<T> getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  public boolean isRequired() {
    return this.required;
  }

  public boolean isVoided() {
    return this.voided;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.type, this.name, this.required);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Argument)) return false;
    Argument<?> other = (Argument<?>) obj;
    return Objects.equals(this.type, other.type) && Objects.equals(this.name, other.name) && Objects.equals(this.required, other.required);
  }

  @Override
  public String toString() {
    return String.format("Argument[type=%s,name=%s,required=%s,voided=%s]", getType(), getName(), isRequired(), isVoided());
  }
}
