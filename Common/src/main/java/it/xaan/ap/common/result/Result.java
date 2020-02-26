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

import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "unchecked"})
public class Result<T> {
  private final State<T> state;

  private Result(final State<T> state) {
    this.state = state;
  }

  public static <U> Result<U> from(State<U> state) {
    return new Result<>(state);
  }

  public <S extends State<T>, R extends ResultType<S>> boolean is(R result) {
    return getClass(result).map(clazz -> clazz.isAssignableFrom(state.getClass())).orElse(false);
  }

  public boolean isSuccess() {
    return is(Success.type());
  }

  public boolean isError() {
    return is(Error.type());
  }

  public <S extends State<T>, R extends ResultType<S>> void when(R result, Consumer<S> consumer) {
    getClass(result)
        .filter(clazz -> this.state.getClass().isAssignableFrom(clazz))
        .ifPresent($ -> consumer.accept((S) this.state));
  }

  public <S extends State<T>, R extends ResultType<S>> void whenNot(
      R result, Consumer<State<T>> consumer) {
    getClass(result)
        .filter(clazz -> !this.state.getClass().isAssignableFrom(clazz))
        .ifPresent($ -> consumer.accept(this.state));
  }

  private <S extends State<T>, R extends ResultType<S>> Optional<Class<?>> getClass(R result) {
    try {
      Class<? extends ResultType> rClazz = result.getClass();
      ParameterizedType t = (ParameterizedType) rClazz.getGenericSuperclass();
      Class<?> clazz = (Class<?>) ((ParameterizedType) t.getActualTypeArguments()[0]).getRawType();
      return Optional.ofNullable(clazz);
    } catch (Throwable ignored) {
      return Optional.empty();
    }
  }

  public State<T> get() {
    return this.state;
  }

  @Override
  public int hashCode() {
    return this.state.hashCode() >> 4;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Result)) return false;
    Result<?> other = (Result<?>) obj;
    return this.state.equals(other.state);
  }

  @Override
  public String toString() {
    return String.format("Argument[state=%s]", this.state);
  }
}
