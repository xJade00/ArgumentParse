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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import it.xaan.ap.common.data.FailedValidationException;
import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.random.result.Result;
import java.util.Objects;
import org.junit.Test;

public class TypeTest {
  private final Type<String> success = new Type<>(Objects::nonNull, String::toLowerCase, null, String.class);
  private final Type<String> error =
    new Type<>(
      Objects::nonNull,
      x -> {
        throw new RuntimeException("Runtime exception thrown.");
      }, null, String.class);
  private final Type<String> nullable = new Type<>(Objects::nonNull, x -> null, null, String.class);
  private final Type<String> failed =
    new Type<>(
      Objects::isNull,
      x -> {
        throw new IllegalStateException("Runtime exception thrown.");
      }, null, String.class);
  private final UnvalidatedArgument arg = UnvalidatedArgument.from("Name", "TESTING");

  @Test
  public void testSuccess() {
    Result<String> result = this.success.decode(this.arg);
    result.onSuccess(success -> assertEquals("testing", success));
    result.onError(Object.class, $ -> fail("State was Error, not Success."));
    result.onEmpty(() -> fail("State was empty, not Success."));
  }

  @Test
  public void testError() {
    Result<String> result = this.error.decode(this.arg);
    result.onError(RuntimeException.class, error -> assertEquals("Runtime exception thrown.", error.getMessage()));
    result.onSuccess($ -> fail("State was Success, not Error."));
    result.onEmpty(() -> fail("State was Success, not Error."));
  }

  @Test
  public void testNullable() {
    Result<String> result = this.nullable.decode(this.arg);
    assertThrows(RuntimeException.class, () -> result.onEmpty(() -> {
      throw new RuntimeException("");
    }));
    result.onSuccess($ -> fail("State was Success, not Empty"));
    result.onError(Object.class, $ -> fail("State was Error, not Empty."));
  }

  @Test
  public void testFailed() {
    Result<String> result = this.failed.decode(this.arg);
    result.onError(FailedValidationException.class, failed -> assertEquals(this.arg, failed.getArgument()));
    result.onError(Exception.class, ex -> {
      if (!(ex instanceof FailedValidationException)) {
        fail("State was error, but exception instead was: " + ex.getCause().getClass().getName());
      }
    });
    result.onSuccess($ -> fail("State was Success, not Error."));
    result.onEmpty(() -> fail("State was Success, not Error."));
  }
}
