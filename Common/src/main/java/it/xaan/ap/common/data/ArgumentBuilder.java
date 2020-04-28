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

public final class ArgumentBuilder<T> {
  private Type<T> type;
  private String name;
  private boolean required;

  public ArgumentBuilder<T> withType(Type<T> type) {
    this.type = type;
    return this;
  }

  public ArgumentBuilder<T> withName(String name) {
    this.name = name;
    return this;
  }

  public ArgumentBuilder<T> withRequired(boolean required) {
    this.required = required;
    return this;
  }

  public Argument<T> build() {
    if(this.type == null) {
      throw new IllegalStateException("Type must be set.");
    }
    return new Argument<>(this.type, this.name, this.required);
  }
}
