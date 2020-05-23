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

import it.xaan.ap.common.data.FailedValidationException;
import it.xaan.ap.common.data.UnvalidatedArgument;
import it.xaan.random.result.Result;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
 * - {@link Void} <br>
 *
 * <p>Note that all boxed types for primitives can be safely extracted to their normal primitive
 * types as the instance from {@link Type#getConverter()} is guaranteed to be @{@link Nonnull} for
 * all default implementations. <br>
 * <p>
 * All instances of this class should be singletons. See the {@link Types} class for examples.
 * <br>
 * It is important to note that the {@link Types#VOID_TYPE} is hard-coded into the application. If
 * you want an argument that takes in no value, such as a {@code --version} then you must use that.
 * Using your own void type will not work, as java has no easy way to see the generics of a type.
 *
 * @param <T> The type to deserialize to.
 */
@SuppressWarnings({"Unused", "WeakerAccess"})
public final class Type<T> {

  private final Predicate<String> validator;
  private final Function<String, T> converter;
  private final Filter[] filters;
  private final String regex;
  private final Class<T> clazz;

  /**
   * Constructs a new {@link Type} object. The main function of this class is to turn a {@link
   * UnvalidatedArgument} object into an instance of {@code T} by using {@link
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
   *                  or null if making an instance is impossible for the input.
   * @param regex     The regex to match it, this is used in combo with validator and should not replace it.
   *                  This is simply used to find the argument, not check that it's valid. For instance,
   *                  a regex for {@code int} is {@code \d+}, even though you could input {@code 2147483648} which
   *                  is normally outside the range. Null is the same as {@code .+}.
   * @param clazz     The class this type represents
   * @param filters   A list of filters that will transform the content to be easier to validate.
   */
  public Type(
    final Predicate<String> validator,
    final Function<String, T> converter,
    @Nullable final String regex,
    Class<T> clazz,
    final Filter... filters) {
    this.validator = validator;
    this.converter = converter;
    this.regex = regex == null ? ".+" : regex;
    this.clazz = clazz;
    this.filters = filters;
  }

  /**
   * Decodes a {@link UnvalidatedArgument} into a {@link T}.
   *
   * @param argument The argument to validate and decode.
   *
   * @return A never-null {@link Result}. If the {@link Predicate} returns false, the result
   * contains a {@link FailedValidationException} as the error. If the {@link Function} returns
   * null, the Result will be empty. If an error is thrown the result contains it as an error.
   *
   * @throws IllegalStateException When the combiner argument of {@link
   *                               java.util.stream.Stream#reduce(Object, BiFunction, BinaryOperator)} is called, since it
   *                               should never happen since it's not a parallel stream.
   */
  public Result<T> decode(UnvalidatedArgument argument) {
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
      if (!valid) return Result.error(new FailedValidationException(argument));
      instance = getConverter().apply(filtered);
    } catch (Throwable exception) {
      return Result.error(exception);
    }
    return Result.ofNullable(instance);
  }

  /**
   * Getter for the {@link Predicate} that the content will run through before it is allowed to be
   * converted with {@link Type#getConverter()}. If this is is true then you can safely call {@link
   * Type#getConverter()} without worrying about any sort of Exception.
   *
   * @return The {@link Predicate} that takes in a {@link String} and returns true if it can be
   * safely converted with {@link Type#getConverter()}, otherwise false.
   */
  public Predicate<String> getValidator() {
    return this.validator;
  }

  /**
   * Getter for the {@link Function} that converts a {@link String} to the specified {@link T}.
   * While the function itself and the input is guaranteed to never be null there is nothing
   * stopping the user from returning null. Ideally this should never happen, but the implementation
   * may want to return null.
   *
   * @return The {@link Function} that takes in a {@link String} and returns a possibly-null {@link
   * T}.
   */
  public Function<String, T> getConverter() {
    return this.converter;
  }

  /**
   * Getter for the {@link List} of {@link Filter}s that the content will be passed through before
   * being passed to {@link Type#getValidator()} and {@link Type#getConverter()}. <br>
   * The filters inside are guaranteed to never be null, and are nearly impossible to
   * programmatically determine what they do.
   *
   * @return A never-null list of the filters the content will pass through.
   */
  public Filter[] getFilters() {
    return this.filters;
  }

  /**
   * Getter for the regex of this type, aka what it can accept. If null was originally passed then this is equal to
   * passing {@code .+}.
   *
   * @return The regex for this type, for gathering.
   */
  public String getRegex() {
    return this.regex;
  }


  /**
   * Getter for the {@link Class} the {@link Type} applies for.<br>
   * For instance: {@link Types#STRING_TYPE} applies to {@link String}.
   *
   * @return The class that this type applies to.
   */
  public Class<T> getClazz() {
    return this.clazz;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.filters, this.converter, this.regex, this.validator);
  }

  @Override
  public String toString() {
    return String.format("Type[regex=%s, object_super=%s]", this.regex, super.toString());
  }
}
