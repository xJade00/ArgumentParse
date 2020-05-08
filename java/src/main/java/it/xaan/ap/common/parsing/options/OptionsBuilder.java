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
 * The builder for a new {@link Options}. <br> <br>
 * <p>
 * Defualts: <br>
 * Prefix: {@code --} <br>
 * Missing arguments strategy: {@link MissingArgsStrategy#TOTAL}
 */
@SuppressWarnings("unused")
public final class OptionsBuilder {
  private String prefix = "--";
  private MissingArgsStrategy missingArgsStrategy = MissingArgsStrategy.TOTAL;

  /**
   * Sets the prefix for this builder.
   *
   * @param prefix The prefix to use.
   *
   * @return The current builder, useful for chaining.
   */
  public OptionsBuilder withPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  /**
   * Sets the missing arguments strategy for this builder.
   *
   * @param missingArgsStrategy The missing arguments strategy to use.
   *
   * @return The current builder, useful for chaining.
   */
  public OptionsBuilder withMissingArgsStrategy(MissingArgsStrategy missingArgsStrategy) {
    this.missingArgsStrategy = missingArgsStrategy;
    return this;
  }

  /**
   * Creates a new {@link Options} instance based on the prefix and missing args strategy.
   *
   * @return The new Options instance.
   */
  public Options build() {
    return new Options(this.prefix, this.missingArgsStrategy);
  }
}
