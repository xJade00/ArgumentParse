package it.xaan

import scala.util.Try
import scala.util.matching.Regex

package object ap {

  implicit case object UnitArg extends ArgumentType[Unit](_ => true, _ => {}) {
    override def find(str: String): String = ""
  }
  /**
   * An argument type representing characters.
   */
  implicit case object CharArg extends ArgumentType[Char](x => x.length == 3, _ (1)) {
    override def find(str: String): String =
      if (str.length < 3) ""
      else str.substring(0, 3)
  }
  /**
   * An argument type representing Strings.
   */
  implicit case object StringArg extends ArgumentType[String](x => x == "\"\"" || x.matches(ArgumentType.StringRegex), x => x.substring(1, x.length - 1).replace("\\\"", "\"")) {
    private val regex = new Regex(ArgumentType.StringRegex)

    override def find(str: String): String = regex.findFirstIn(str).getOrElse("")
  }
  /**
   * An argument type representing booleans.
   */
  implicit case object BooleanArg extends ArgumentType[Boolean](x => x == "true" || x == "false", _.toBoolean)
  /**
   * An argument type representing doubles.
   */
  implicit case object DoubleArg extends ArgumentType[Double](x => Try(x.replace("\r\n", "").toDouble).isSuccess, _.toDouble)
  /**
   * An argument type representing longs.
   */
  implicit case object LongArg extends ArgumentType[Long](x => Try(x.replace("\r\n", "").toLong).isSuccess, _.toLong)
  /**
   * An argument type representing floats.
   */
  implicit case object FloatArg extends ArgumentType[Float](x => Try(x.replace("\r\n", "").toFloat).isSuccess, _.toFloat)
  /**
   * An argument type representing bytes.
   */
  implicit case object ByteArg extends ArgumentType[Byte](x => Try(x.replace("\r\n", "").toByte).isSuccess, _.toByte)
  /**
   * An argument type representing shorts.
   */
  implicit case object ShortArg extends ArgumentType[Short](x => Try(x.replace("\r\n", "").toShort).isSuccess, _.toShort)
  /**
   * An argument type representing integers.
   */
  implicit case object IntArg extends ArgumentType[Int](x => Try(x.replace("\r\n", "").toInt).isSuccess, _.toInt)


}
