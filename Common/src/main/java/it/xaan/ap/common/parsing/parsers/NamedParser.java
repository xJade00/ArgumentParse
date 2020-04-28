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
import it.xaan.ap.common.data.ParsedArgument;
import it.xaan.ap.common.data.ParsedArguments;
import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.ap.common.parsing.Parser;
import it.xaan.ap.common.parsing.Types;
import it.xaan.random.result.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NamedParser implements Parser<ParsedArguments> {

  @Override
  public Result<ParsedArguments> parse(Set<Argument<?>> arguments, String content) {
    try {
      final List<ParsedArgument<?>> parsed = new ArrayList<>();
      final List<Argument<?>> missing = new ArrayList<>();
      for (Argument<?> argument : arguments) {
        // Could append using + and ternaries, but that looks messier. and I want it to be final.
        final StringBuilder builder = new StringBuilder();
        builder.append("--(")
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
          missing.add(argument);
          continue;
        }
        // Should only be the first one
        final String name = matcher.group(1);
        if (argument.isVoided()) {
          parsed.add(new ParsedArgument<>(name, null));
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
      return Result.of(new ParsedArguments((parsed)));
    } catch (Exception ex) {
      return Result.error(ex);
    }
  }

  public static void main(String[] args) {
    String test = "!ban --user=123 --reason=\"Being a jerk to everyone\" --clear=true --output";
    Argument<Integer> user = new Argument<>(Types.INTEGER_TYPE, "user", true);
    Argument<Boolean> clear = new Argument<>(Types.BOOLEAN_TYPE, "clear", false);
    Argument<String> reason = new Argument<>(Types.STRING_TYPE, "reason", false);
    Argument<Void> output = new Argument<>(Types.VOID_TYPE, "output", false);

    Parser<ParsedArguments> parser = new NamedParser();
    Result<ParsedArguments> result = parser.parse(Argument.set(user, clear, reason, output), test);
    result.onSuccess(System.out::println)
      .onError(Exception.class, Exception::printStackTrace)
      .onEmpty(() -> System.out.println("was empty?"));
  }
}
