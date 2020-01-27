package it.xaan.ap

import scala.util.Try

class ParsedArgument(private val map: Map[String, Any]) {
  def apply[T](key: String): T = get(key).get

  def get[T](key: String): Option[T] = Try(map(key).asInstanceOf[T]).toOption

  def has(key: String): Boolean = map.contains(key)

  override def toString: String = map.toString()
}
