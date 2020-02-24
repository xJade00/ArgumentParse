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

package it.xaan.ap;

import it.xaan.ap.common.data.UnvalidatedArgumentTest;
import it.xaan.ap.common.parsing.FilterTest;
import it.xaan.ap.common.parsing.TypeTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

  public static void main(String[] args) {
    Result result =
        JUnitCore.runClasses(FilterTest.class, UnvalidatedArgumentTest.class, TypeTest.class);

    for (Failure failure : result.getFailures()) {
      System.out.println(String.format("Trace: %s", failure.getTrimmedTrace()));
    }

    System.out.println(result.wasSuccessful());
  }
}
