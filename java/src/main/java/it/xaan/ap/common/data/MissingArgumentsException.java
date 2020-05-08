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

import java.util.Collections;
import java.util.List;

/**
 * Represents an exceptional state where the parser was missing required arguments.
 */
public final class MissingArgumentsException extends RuntimeException {
  private final List<Argument<?>> list;

  /**
   * Creates a new {@link MissingArgumentsException} from the list.
   *
   * @param list The list of mising arguments.
   */
  public MissingArgumentsException(List<Argument<?>> list) {
    super("Missing arguments: " + list);
    this.list = Collections.unmodifiableList(list);
  }

  /**
   * @return An unmodifiable list of all missing arguments.
   */
  @SuppressWarnings("unused")
  public List<Argument<?>> getList() {
    return this.list;
  }
}
