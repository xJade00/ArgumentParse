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

/**
 * A builder for the {@link Argument} class. You must set type and name. Required is false by default.
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public final class ArgumentBuilder<T> {
  private Type<T> type;
  private String name;
  private boolean required = false;

  /**
   * Creates a new Builder with the initial {@link Type}.
   *
   * @param type The initial type.
   */
  public ArgumentBuilder(Type<T> type) {
    this.type = type;
  }

  /**
   * Sets the {@link Type} for this argument.
   *
   * @param type The type.
   *
   * @return The current builder, useful for chaining.
   */
  public ArgumentBuilder<T> withType(Type<T> type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the name for this argument.
   *
   * @param name The name of the argument.
   *
   * @return The current builder, useful for chaining.
   */
  public ArgumentBuilder<T> withName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets if the argument is required.
   *
   * @param required If the argument is required.
   *
   * @return The current builder, useful for chaining.
   */
  public ArgumentBuilder<T> withRequired(boolean required) {
    this.required = required;
    return this;
  }

  /**
   * Builds a new Argument from the builder.
   *
   * @return The built argument.
   *
   * @throws IllegalStateException When type or name isn't set.
   */
  public Argument<T> build() {
    if (this.type == null || this.name == null) {
      throw new IllegalStateException("Type and name must be set.");
    }
    return new Argument<>(this.type, this.name, this.required);
  }
}
