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

import static it.xaan.ap.common.parsing.Types.BOOLEAN_TYPE;
import static it.xaan.ap.common.parsing.Types.BYTE_TYPE;
import static it.xaan.ap.common.parsing.Types.CHARACTER_TYPE;
import static it.xaan.ap.common.parsing.Types.DOUBLE_TYPE;
import static it.xaan.ap.common.parsing.Types.FLOAT_TYPE;
import static it.xaan.ap.common.parsing.Types.INTEGER_TYPE;
import static it.xaan.ap.common.parsing.Types.LONG_TYPE;
import static it.xaan.ap.common.parsing.Types.SHORT_TYPE;
import static it.xaan.ap.common.parsing.Types.STRING_TYPE;

import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.random.result.Result;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.Test;

public final class TypesTest {

  private <T> void test(Result<T> result, Consumer<T> consumer) {
    result.onSuccess(consumer)
      .onError(Throwable.class, throwable -> Assert.fail("Result [" + result + "] failed with exception" +
        " [" + throwable.getClass().getSimpleName() + "] and message " +
        "[" + throwable.getMessage() + "]"))
      .onEmpty(() -> Assert.fail("Result [" + result + "] as was empty."));
  }

  @Test
  public void testIntegerType() {
    test(INTEGER_TYPE.decode(UnvalidatedArgument.from("ignored", "0")), i -> Assert.assertEquals(0, (int) i));
  }

  @Test
  public void testLongType() {
    test(LONG_TYPE.decode(UnvalidatedArgument.from("ignored", "0")), i -> Assert.assertEquals(0, (long) i));
  }

  @Test
  public void testByteType() {
    test(BYTE_TYPE.decode(UnvalidatedArgument.from("ignored", "0")), i -> Assert.assertEquals((byte) 0, (byte) i));
  }

  @Test
  public void testShortType() {
    test(SHORT_TYPE.decode(UnvalidatedArgument.from("ignored", "0")), i -> Assert.assertEquals(0, (short) i));
  }

  @Test
  public void testFloatType() {
    test(FLOAT_TYPE.decode(UnvalidatedArgument.from("ignored", "0.0")), i -> Assert.assertEquals(0.0f, i, 0f));
  }

  @Test
  public void testDoubleType() {
    test(DOUBLE_TYPE.decode(UnvalidatedArgument.from("ignored", "0.0")), i -> Assert.assertEquals(0.0, i, 0.0));
  }

  @Test
  public void testBooleanType() {
    test(BOOLEAN_TYPE.decode(UnvalidatedArgument.from("ignored", "true")), i -> Assert.assertEquals(true, i));
  }

  @Test
  public void testCharacterType() {
    test(CHARACTER_TYPE.decode(UnvalidatedArgument.from("ignored", "'0'")), i -> Assert.assertEquals('0', (char) i));
  }

  @Test
  public void testStringType() {
    test(STRING_TYPE.decode(UnvalidatedArgument.from("ignored", "\"Hello world\"")), i -> Assert.assertEquals("Hello world", i));
    test(STRING_TYPE.decode(UnvalidatedArgument.from("ignored", "\"Hello \\\"Hello world\\\" world\"")), i -> Assert.assertEquals("Hello \"Hello world\" world", i));
  }
}
