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
package it.xaan.ap.common.parsing;

import it.xaan.ap.common.data.Argument;
import it.xaan.ap.common.parsing.options.Options;
import it.xaan.ap.common.parsing.options.OptionsBuilder;
import it.xaan.random.result.Result;
import java.util.Collection;

/**
 * Represents a parser that parses a list of arguments into the specified type.
 */
@SuppressWarnings("unused")
public interface Parser<T> {
  /**
   * Parses a number of arguments in the specified content to a readable format. The content
   * that is passed should be stripped of uneccesary symbols. Such as with {@link it.xaan.ap.common.parsing.parsers.NamedParser}
   * if you are parsing {@code ?ban user --days=7} you should only be passing {@code --days=7}.
   *
   * @param arguments The arguments.
   * @param content   The content to parse.
   * @param options   THe options to use.
   *
   * @return A {@link Result}. This contains an instance of {@code T} in a success state if the content could be parsed,
   * it'll contain an error in an error state if one was thrown.
   */
  Result<T> parse(Collection<Argument<?>> arguments, String content, Options options);

  /**
   * The same as {@link #parse(Collection, String, Options)} but with the default options of {@code new OptionsBuilder().build()}
   */
  default Result<T> parse(Collection<Argument<?>> arguments, String content) {
    return parse(arguments, content, new OptionsBuilder().build());
  }
}
