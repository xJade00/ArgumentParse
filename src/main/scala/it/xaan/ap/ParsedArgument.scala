package it.xaan.ap

import java.util.Optional

import scala.util.{Failure, Success, Try}

/**
 * Represents parsed arguments.
 *
 * @param map A map of all parsed arguments/
 */
class ParsedArgument(private val map: Map[String, Any]) {
  /**
   * Grabs a parsed argument.
   *
   * @param key The key of the argument to grab.
   * @tparam T The type to cast to.
   * @throws NoSuchElementException If the key doesn't exist in the map.
   * @return The value associated with the key.
   */
  def apply[T](key: Argument[T]): T = get(key).get

  /**
   * Grabs a parsed argument.
   *
   * @param key The key of the argument to grab.
   * @tparam T The type to cast to.
   * @return None if the key doesn't exist or it's not of the specified type, Some with the value if it does.
   */
  def get[T](key: Argument[T]): Option[T] = Try(map(key.name).asInstanceOf[T]).toOption

  def getJava[T](key: Argument[T]): Optional[T] = JavaApi.toJavaOptional(get(key))

  /**
   * Returns whether or not an argument is defined.
   *
   * @param key The key of the argument.
   * @return true if the key exists, otherwise false.
   */
  def has(key: String): Boolean = map.contains(key)

  override def toString: String = map.toString()
}
