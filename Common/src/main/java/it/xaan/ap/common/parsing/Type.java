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

import it.xaan.ap.common.FailedValidation;
import it.xaan.ap.common.data.UnvalidatedArgument;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

/**
 * Represents a Type that you can deserialize a string to. <br>
 * The following classes are given a default implementation: <br>
 * - {@link Boolean} <br>
 * - {@link Integer} <br>
 * - {@link Character} <br>
 * - {@link Double} <br>
 * - {@link Float} <br>
 * - {@link Long} <br>
 * - {@link Short} <br>
 * - {@link Byte} <br>
 * - {@link String} <br>
 * - {@link List} <br>
 *
 * <p>Note that all boxed types for primitives can be safely extracted to their normal primitive
 * types as the instance from {@link Type#getConverter()} is guaranteed to be @{@link Nonnull} for
 * all default implementations. <br>
 *
 * @param <T> The type to deserialize to.
 */
@SuppressWarnings({"Unused", "WeakerAccess"})
public final class Type<T> {

  private final Predicate<String> validator;
  private final Function<String, T> converter;
  private final Filter[] filters;

  /**
   * Constructs a new {@link Type} object. The main function of this class is to turn a {@link
   * UnvalidatedArgument} object into a {@link ValidatedArgument} by using {@link
   * #decode(UnvalidatedArgument)}. The {@link Predicate} passed to {@code validator} will determine
   * if it can be turned into a new instance of {@code T} without the {@link Function} passed to
   * {@code converter} throwing an Exception. <br>
   * <br>
   * It should be noted that the {@code validator} should only be returning true not only when the
   * converter won't throw an exception, but also if it <i>can</i> be a valid value for the
   * specified type in {@link T}. For instance in the default implementation of {@link List} the
   * predicate will return false if the passed string does not start with {@code [} and end with
   * {@code ]}. It will not return {@code null} or {@link Collections#emptyList()}. Similarly the
   * default implementation of Integer will not allow non-integer values but may not return the
   * correct result if passed 2^31.
   *
   * @param validator The predicate that specifies whether or not the string can be deserialized.
   * @param converter The function that takes in a string and returns an instance of the specified T
   *     or null if making an instance is impossible for the input.
   * @param filters A list of filters that will transform the content to be easier to validate.
   */
  public Type(
      final Predicate<String> validator,
      final Function<String, T> converter,
      final Filter... filters) {
    this.validator = validator;
    this.converter = converter;
    this.filters = filters;
  }

  /**
   * Decodes a {@link UnvalidatedArgument} into a {@link ValidatedArgument}. If the {@link
   * Predicate} returns false or the {@link Function} throws an exception, this method returns the
   * {@link ValidatedArgument} in an exceptional state. If the {@link Function} returns null, this
   * method returns the {@link ValidatedArgument} in a nulled state. Else this method returns a
   * successful state.
   *
   * @throws IllegalStateException When the combiner argument of {@link
   *     java.util.stream.Stream#reduce(Object, BiFunction, BinaryOperator)} is called, since it
   *     should never happen since it's not a parallel stream.
   * @param argument The argument to validate and decode.
   * @return A never-null {@link ValidatedArgument} in either a successful, nulled, or exceptional
   *     state.
   */
  public ValidatedArgument<T> decode(UnvalidatedArgument argument) {
    String filtered =
        Arrays.stream(getFilters())
            .reduce(
                argument.getValue(),
                (accumulator, filter) -> filter.filter(accumulator),
                (x, y) -> {
                  throw new IllegalStateException("Should never get here. x: " + x + ", y: " + y);
                });

    T instance;
    try {
      boolean valid = getValidator().test(filtered);
      if (!valid) throw new FailedValidation(String.format("Couldn't validate for %s", argument));
      instance = getConverter().apply(filtered);
    } catch (Throwable exception) {
      return ValidatedArgument.exception(exception);
    }
    return instance == null ? ValidatedArgument.nullable() : ValidatedArgument.success(instance);
  }

  /**
   * Getter for the {@link Predicate} that the content will run through before it is allowed to be
   * converted with {@link Type#getConverter()}. If this is is true then you can safely call {@link
   * Type#getConverter()} without worrying about any sort of Exception.
   *
   * @return The {@link Predicate} that takes in a {@link String} and returns true if it can be
   *     safely converted with {@link Type#getConverter()}, otherwise false.
   */
  public Predicate<String> getValidator() {
    return validator;
  }

  /**
   * Getter for the {@link Function} that converts a {@link String} to the specified {@link T}.
   * While the function itself and the input is guaranteed to never be null there is nothing
   * stopping the user from returning null. Ideally this should never happen, but the implementation
   * may want to return null.
   *
   * @return The {@link Function} that takes in a {@link String} and returns a possibly-null {@link
   *     T}.
   */
  public Function<String, T> getConverter() {
    return converter;
  }

  /**
   * Getter for the {@link List} of {@link Filter}s that the content will be passed through before
   * being passed to {@link Type#getValidator()} and {@link Type#getConverter()}. <br>
   * The filters inside are guaranteed to never be null, and are nearly impossible to
   * programmatically determine what they do. Order isn't guaranteed to be preserved through
   * multiple calls to this method. <br>
   *
   * @return A never-null list of the filters the content will pass through.
   */
  public Filter[] getFilters() {
    return filters;
  }
}
