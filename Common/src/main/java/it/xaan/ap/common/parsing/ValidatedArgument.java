/**
 * ArgumentParse - Parsing CLI arguments in Java. Copyright © 2020 xaanit (shadowjacob1@gmail.com)
 *
 * <p>This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package it.xaan.ap.common.parsing;

import java.util.function.Consumer;
import javax.annotation.Nullable;

/**
 * Represents a validated argument in either a Successful, Nulled, or Exceptional state.
 *
 * @param <T> The type of the argument.
 */
@SuppressWarnings({"WeakerAccess", "Unused"})
public class ValidatedArgument<T> {
  private final @Nullable T value;
  private final @Nullable Throwable exception;

  private ValidatedArgument(final @Nullable T value, final @Nullable Throwable exception) {
    this.value = value;
    this.exception = exception;
  }

  public static <U> ValidatedArgument<U> success(final U value) {
    return new ValidatedArgument<>(value, null);
  }

  public static <U> ValidatedArgument<U> nullable() {
    return new ValidatedArgument<>(null, null);
  }

  public static <U> ValidatedArgument<U> exception(final Throwable exception) {
    return new ValidatedArgument<>(null, exception);
  }

  public T get() {
    if (value == null) throw new IllegalStateException("Value can't be null.");
    return value;
  }

  public void handle(
      @Nullable Consumer<T> success,
      @Nullable Runnable nullable,
      @Nullable Consumer<Throwable> exceptional) {
    if (success != null && value != null) success.accept(value);
    if (nullable != null && value == null && exception == null) nullable.run();
    if (exceptional != null && exception != null) exceptional.accept(exception);
  }

  public void onSuccess(Consumer<T> success) {
    handle(success, null, null);
  }

  public void onNull(Runnable nullable) {
    handle(null, nullable, null);
  }

  public void onException(Consumer<Throwable> exceptional) {
    handle(null, null, exceptional);
  }

  public <E extends Throwable> void onException(Class<E> clazz, Consumer<E> consumer) {
    if (exception == null) return;
    if (clazz.isAssignableFrom(exception.getClass())) {
      consumer.accept(clazz.cast(exception));
    }
  }
}
