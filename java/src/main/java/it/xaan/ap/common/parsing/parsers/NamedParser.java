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
package it.xaan.ap.common.parsing.parsers;

import it.xaan.ap.common.data.Argument;
import it.xaan.ap.common.data.MissingArgumentsException;
import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.ap.common.data.parsed.ParsedArgument;
import it.xaan.ap.common.data.parsed.ParsedNameAguments;
import it.xaan.ap.common.parsing.Parser;
import it.xaan.ap.common.parsing.options.MissingArgsStrategy;
import it.xaan.ap.common.parsing.options.Options;
import it.xaan.random.result.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parser that parses arguments based on naming, such as --name="Hello world"
 */
@SuppressWarnings("unused")
public final class NamedParser implements Parser<ParsedNameAguments> {

  private final static Object PRESENT = new Object();

  @Override
  public Result<ParsedNameAguments> parse(Collection<Argument<?>> arguments, String content, Options options) {
    arguments = new HashSet<>(arguments);
    try {
      final List<ParsedArgument<?>> parsed = new ArrayList<>();
      final List<Argument<?>> missing = new ArrayList<>();
      for (Argument<?> argument : arguments) {
        // Could append using + and ternaries, but that looks messier. and I want it to be final.
        final StringBuilder builder = new StringBuilder();
        builder.append(options.getPrefix())
          .append("(")
          .append(argument.getName())
          .append(')');
        if (!argument.isVoided()) {
          builder.append("=(")
            .append(argument.getType().getRegex())
            .append(')');
        }
        final Pattern pattern = Pattern.compile(builder.toString());
        final Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
          if (!argument.isRequired()) {
            continue;
          }
          missing.add(argument);
          if (options.getMissingArgsStrategy() == MissingArgsStrategy.TOTAL) {
            continue;
          } else {
            break;
          }
        }
        // Should only be the first one
        final String name = matcher.group(1);
        if (argument.isVoided()) {
          // using null means that ParsedNameArguments can't work properly
          parsed.add(new ParsedArgument<>(name, PRESENT));
          continue;
        }
        final String value = matcher.group(2);
        Result<?> result = argument.getType().decode(UnvalidatedArgument.from(name, value));
        // If it's empty, it returned null. This is not an error state, instead we view it as a
        // "We don't care".
        if (result.isEmpty()) continue;

        if (result.isError()) {
          return Result.error(result.getError());
        }

        parsed.add(new ParsedArgument<>(name, result.get()));
      }
      if (!missing.isEmpty()) {
        throw new MissingArgumentsException(missing);
      }
      return Result.of(new ParsedNameAguments(parsed));
    } catch (Exception ex) {
      return Result.error(ex);
    }
  }
}
