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

public class Error<T> implements State<T> {
  private final Throwable exception;

  private Error(final Throwable exception) {
    this.exception = exception;
  }

  public static <E> Error<E> from(final Throwable exception) {
    return new Error<>(exception);
  }

  public static <E> ResultType<Error<E>> type() {
    return new ResultType<Error<E>>() {};
  }

  public Throwable getException() {
    return exception;
  }

  @Override
  public int hashCode() {
    return this.exception.hashCode() >> 4;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Error)) return false;
    Error other = (Error) obj;
    return this.exception.equals(other.exception);
  }

  @Override
  public String toString() {
    return String.format("Error[exception=%s]", this.exception);
  }
}
