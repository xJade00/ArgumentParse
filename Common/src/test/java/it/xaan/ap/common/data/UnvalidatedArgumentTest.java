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
package it.xaan.ap.common.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class UnvalidatedArgumentTest {
  private static final UnvalidatedArgument first = UnvalidatedArgument.from("name", "value");
  private static final UnvalidatedArgument second = UnvalidatedArgument.from("name", "value");
  private static final UnvalidatedArgument third =
    UnvalidatedArgument.from("name", "different value");

  @Test
  public void checkData() {
    assertEquals("name", first.getName());
    assertEquals("value", first.getValue());
  }

  @Test
  public void checkEquals() {
    assertEquals(first, second);
    assertNotEquals(first, third);
  }
}
