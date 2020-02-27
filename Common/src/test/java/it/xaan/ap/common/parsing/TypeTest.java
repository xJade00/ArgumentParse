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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.ap.common.result.Error;
import it.xaan.ap.common.result.FailedValidation;
import it.xaan.ap.common.result.Null;
import it.xaan.ap.common.result.Result;
import it.xaan.ap.common.result.Success;
import java.util.Objects;
import org.junit.Test;

public class TypeTest {
  private final Type<String> success = new Type<>(Objects::nonNull, String::toLowerCase);
  private final Type<String> error =
      new Type<>(
          Objects::nonNull,
          x -> {
            throw new RuntimeException("Runtime exception thrown.");
          });
  private final Type<String> nullable = new Type<>(Objects::nonNull, x -> null);
  private final Type<String> failed =
      new Type<>(
          Objects::isNull,
          x -> {
            throw new IllegalStateException("Runtime exception thrown.");
          });
  private final UnvalidatedArgument arg = UnvalidatedArgument.from("Name", "TESTING");

  @Test
  public void testSuccess() {
    Result<String> result = success.decode(arg);
    result.when(Success.type(), success -> assertEquals("testing", success.getElement()));
    result.whenNot(Success.type(), state -> fail("State was " + state + ", not Success."));
  }

  @Test
  public void testError() {
    Result<String> result = error.decode(arg);
    result.when(
        Error.type(),
        error -> assertEquals("Runtime exception thrown.", error.getException().getMessage()));
    result.whenNot(Error.type(), state -> fail("State was " + state + ", not Error."));
  }

  @Test
  public void testNullable() {
    Result<String> result = nullable.decode(arg);
    result.when(Null.type(), $ -> {});
    result.whenNot(Null.type(), state -> fail("State was " + state + ", not Null."));
  }

  @Test
  public void testFailed() {
    Result<String> result = failed.decode(arg);
    result.when(FailedValidation.type(), failed -> assertEquals(arg, failed.getUnvalidated()));
    result.whenNot(
        FailedValidation.type(), state -> fail("State was " + state + ", not FailedValidation."));
  }
}
