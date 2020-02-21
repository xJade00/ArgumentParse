/**
 * ArgumentParse - Parsing CLI arguments in Java. Copyright © 2020 xaanit (shadowjacob1@gmail.com)
 *
 * <p>This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package it.xaan.ap.common.parsing;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class ValidatedArgumentTest {
  private final ValidatedArgument<String> success = ValidatedArgument.success("Testing");
  private final ValidatedArgument<String> nullable = ValidatedArgument.nullable();
  private final ValidatedArgument<String> exceptional =
      ValidatedArgument.exception(new IllegalStateException("Exceptional state for ValidArgument"));

  @Test
  public void testHandle() {
    final String exceptional = "Exceptional state in expected %s state. Exception: %s";
    success.handle(
        str -> assertEquals("Testing", str),
        () -> Assert.fail("Null state in expected success state."),
        exception -> Assert.fail(String.format(exceptional, "success", exception)));

    nullable.handle(
        str -> Assert.fail("Success state in expected null state."),
        () -> {},
        exception -> Assert.fail(String.format(exceptional, "null", exception)));

    this.exceptional.handle(
        str -> Assert.fail("Success state in expected exceptional state."),
        () -> Assert.fail("Null state in expected exceptional state."),
        exception -> assertEquals(exception.getMessage(), "Exceptional state for ValidArgument"));
  }
}
