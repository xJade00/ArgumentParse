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

import it.xaan.ap.common.data.UnvalidatedArgument;

public class FailedValidation<T> implements State<T> {
  private final UnvalidatedArgument unvalidated;

  private FailedValidation(final UnvalidatedArgument unvalidated) {
    this.unvalidated = unvalidated;
  }

  public static <T> FailedValidation<T> from(final UnvalidatedArgument unvalidated) {
    return new FailedValidation<>(unvalidated);
  }

  public static <E> ResultType<FailedValidation<E>> type() {
    return new ResultType<FailedValidation<E>>() {};
  }


  public UnvalidatedArgument getUnvalidated() {
    return unvalidated;
  }

  @Override
  public int hashCode() {
    return this.unvalidated.hashCode() >> 4;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof FailedValidation)) return false;
    FailedValidation other = (FailedValidation) obj;
    return this.unvalidated.equals(other.unvalidated);
  }

  @Override
  public String toString() {
    return String.format("FailedValidation[unvalidated=%s]", this.unvalidated);
  }

}
