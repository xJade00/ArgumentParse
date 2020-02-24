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

public class Success<T> implements State<T> {
  private final T element;

  private Success(final T element) {
    this.element = element;
  }

  public static <U> Success<U> from(final U element) {
    return new Success<>(element);
  }

  public static <E> ResultType<Success<E>> type() {
    return new ResultType<Success<E>>() {};
  }

  public T getElement() {
    return element;
  }

  @Override
  public int hashCode() {
    return this.element.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Success)) return false;
    Success<?> other = (Success<?>) obj;
    return this.element.equals(other.element);
  }

  @Override
  public String toString() {
    return String.format("Ok[element=%s]", this.element);
  }
}
