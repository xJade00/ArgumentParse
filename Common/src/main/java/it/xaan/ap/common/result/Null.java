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

package it.xaan.ap.common.result;

public class Null<T> implements State<T> {

  private Null() {}

  public static <T> Null<T> create() {
    return new Null<>();
  }

  public static <E> ResultType<Null<E>> type() {
    return new ResultType<Null<E>>() {};
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString() {
    return "Null State.";
  }
}
