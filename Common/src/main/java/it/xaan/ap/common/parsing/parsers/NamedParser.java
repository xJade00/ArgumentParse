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
import it.xaan.ap.common.data.ParsedArgument;
import it.xaan.ap.common.data.ParsedArguments;
import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.ap.common.parsing.Parser;
import it.xaan.random.result.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final class NamedParser implements Parser<ParsedArguments> {

  private int findWhere(String str, Predicate<Character> pred) {
    for (int i = 0; i < str.length(); i++) {
      if (pred.test(str.charAt(i))) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public Result<ParsedArguments> parse(final Set<Argument<?>> arguments, String content) {
    try {
      // Faster lookup, memory vs speed. This will take up twice the amount of memory
      // But for many arguments it'll be much faster. This is
      final Map<String, Argument<?>> mapped = new HashMap<>();
      for (Argument<?> argument : arguments) {
        mapped.put(argument.getName(), argument);
      }
      final Set<UnvalidatedArgument> unvalidated = new HashSet<>();
      int index = content.indexOf("--");
      while (index != -1) {
        // "--".indexOf("--") returns 2
        // "--".substring(2) returns ""
        // We can always safely index + 2
        content = content.substring(index + 2);
        int equals = content.indexOf('=');
        // TODO: Voided arguments?
        // TODO: findWhere can help above.
        if (equals == -1) break;
        String name = content.substring(0, equals).trim();
        // Easiest to just trim stuff.
        content = content.substring(name.length() + 1).trim();
        index = content.indexOf("--");
        // If it's empty at this point, someone did "--name=" which is invalid, and so we no longer care
        // if index of "--" is -1, there is no further commands
        // TODO: BUG? If you do "--name="Hello--world" it'll fail, it'll store
        // TODO: UnvalidatedArgument[name=name,value=Hello] and then fail.
        if (!content.isEmpty()) {
          // If it's -1, we just take the rest of the string.
          int sub = index == -1 ? content.length() : index;
          String value = content.substring(0, sub);
          unvalidated.add(UnvalidatedArgument.from(name, value));
        }
      }

      final List<ParsedArgument<?>> parsed = new ArrayList<>();
      for (UnvalidatedArgument arg : unvalidated) {
        final Argument<?> argument = mapped.get(arg.getName());
        // If it's null, that means there's no argument we care about there.
        if (argument == null) continue;
        Result<?> result = argument.getType().decode(arg);
        // If there is an error, we want to exit immediately.
        if (result.isError()) {
          return Result.error(result.getError());
        }
        // If it's empty, it returned null. This is not an error state, instead we view it as a
        // "We don't care".
        if (result.isEmpty()) continue;
        parsed.add(new ParsedArgument<>(argument.getName(), result.get()));
      }
      return Result.of(new ParsedArguments(parsed));
    } catch (Throwable t) {
      return Result.error(t);
    }
  }
}
