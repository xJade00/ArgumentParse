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
import it.xaan.ap.common.data.parsed.ParsedPositionalArguments;
import it.xaan.ap.common.parsing.Parser;
import it.xaan.ap.common.parsing.options.MissingArgsStrategy;
import it.xaan.ap.common.parsing.options.Options;
import it.xaan.random.result.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents a parser based on positional arguments.
 */
@SuppressWarnings("unused")
public final class PositionalParser implements Parser<ParsedPositionalArguments> {
  @Override
  public Result<ParsedPositionalArguments> parse(Collection<Argument<?>> arguments, String content, Options options) {
    try {
      arguments = arguments.stream().distinct().sorted(Comparator.comparing(Argument::getName)).collect(Collectors.toList());
      final List<ParsedArgument<?>> parsed = new ArrayList<>();
      final List<Argument<?>> missed = new ArrayList<>();
      for (Argument<?> argument : arguments) {
        Pattern pattern = Pattern.compile(argument.getType().getRegex());
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
          if (argument.isRequired()) {
            missed.add(argument);
            if (options.getMissingArgsStrategy() == MissingArgsStrategy.QUICK) {
              break;
            }
          }
          continue;
        }
        String value = matcher.group(0);
        int index = content.indexOf(value);
        content = content.substring(index + value.length()).trim();
        Result<?> result = argument.getType().decode(UnvalidatedArgument.from(argument.getName(), value));
        if (result.isEmpty()) { // huh?
          continue;
        }

        if (result.isError()) {
          return Result.error(result.getError());
        }

        parsed.add(new ParsedArgument<>(argument.getName(), result.get()));
      }

      if (!missed.isEmpty()) {
        throw new MissingArgumentsException(missed);
      }

      return Result.of(new ParsedPositionalArguments(parsed));
    } catch (Exception ex) {
      return Result.error(ex);
    }
  }
}