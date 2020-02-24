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

import org.junit.Test;

public class FilterTest {

  @Test
  public void testTrimSpaces() {
    final String test = "Test             ";
    assertEquals("Test", Filter.TRIM_SPACES.filter(test));
  }

  @Test
  public void testTrimFrontSpaces() {
    final String test = "            Test";
    assertEquals("Test", Filter.REMOVE_SPACE_PREFIX.filter(test));
  }

  @Test
  public void testTrimNewlines() {
    final String r = "Trim\r";
    final String n = "Trim\n";
    final String both = "Trim\r";
    assertEquals("Trim", Filter.TRIM_NEWLINES.filter(r));
    assertEquals("Trim", Filter.TRIM_NEWLINES.filter(n));
    assertEquals("Trim", Filter.TRIM_NEWLINES.filter(both));
  }
}
