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
package it.xaan.ap.common.parsing.options;

/**
 * Represents a number of options the parsers can use. You are encouraged to make subclasses of this.
 */
@SuppressWarnings("unused")
public class Options {
  private final String prefix;
  private final MissingArgsStrategy missingArgsStrategy;

  /**
   * Creates a new {@link Options}.
   *
   * @param prefix              The prefix for arguments, if applicable.
   * @param missingArgsStrategy The strategy for missing arguments.
   */
  public Options(String prefix, MissingArgsStrategy missingArgsStrategy) {
    this.prefix = prefix;
    this.missingArgsStrategy = missingArgsStrategy;
  }

  /**
   * @return The prefix for the arguments.
   */
  public String getPrefix() {
    return this.prefix;
  }

  /**
   * @return The strategy for missing arguments.
   */
  public MissingArgsStrategy getMissingArgsStrategy() {
    return this.missingArgsStrategy;
  }
}
