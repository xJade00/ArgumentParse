package it.xaan.ap

import java.util.{Optional, Set => JSet}

import scala.collection.mutable.ListBuffer
import scala.util.Try


object JavaApi {
  /**
   * An argument type representing booleans.
   */
  implicit case object BooleanArg extends ArgumentType[java.lang.Boolean](x => x == "true" || x == "false", _.toBoolean)
  /**
   * An argument type representing doubles.
   */
  implicit case object DoubleArg extends ArgumentType[java.lang.Double](x => Try(x.replace("\r\n", "").toDouble).isSuccess, _.toDouble)
  /**
   * An argument type representing longs.
   */
  implicit case object LongArg extends ArgumentType[java.lang.Long](x => Try(x.replace("\r\n", "").toLong).isSuccess, _.toLong)
  /**
   * An argument type representing floats.
   */
  implicit case object FloatArg extends ArgumentType[java.lang.Float](x => Try(x.replace("\r\n", "").toFloat).isSuccess, _.toFloat)
  /**
   * An argument type representing bytes.
   */
  implicit case object ByteArg extends ArgumentType[java.lang.Byte](x => Try(x.replace("\r\n", "").toByte).isSuccess, _.toByte)
  /**
   * An argument type representing shorts.
   */
  implicit case object ShortArg extends ArgumentType[java.lang.Short](x => Try(x.replace("\r\n", "").toShort).isSuccess, _.toShort)
  /**
   * An argument type representing integers.
   */
  implicit case object IntArg extends ArgumentType[java.lang.Integer](x => Try(x.replace("\r\n", "").toInt).isSuccess, _.toInt)
  /**
   * An argument type representing characters.
   */
  implicit case object CharArg extends ArgumentType[java.lang.Character](x => x.length == 3, x => x.charAt(0)) {
    override def find(str: String): String =
      if (str.length < 3) ""
      else str.substring(0, 3)
  }

  implicit case object UnitArg extends ArgumentType[Void](_ => true, _ => null) {
    override def find(str: String): String = ""
  }

  def asInteger(builder: ArgumentBuilder[Integer]): Argument[Integer] = builder.build()
  def asBoolean(builder: ArgumentBuilder[java.lang.Boolean]): Argument[java.lang.Boolean] = builder.build()
  def asDouble(builder: ArgumentBuilder[java.lang.Double]): Argument[java.lang.Double] = builder.build()
  def asLong(builder: ArgumentBuilder[java.lang.Long]): Argument[java.lang.Long] = builder.build()
  def asFloat(builder: ArgumentBuilder[java.lang.Float]): Argument[java.lang.Float] = builder.build()
  def asByte(builder: ArgumentBuilder[java.lang.Byte]): Argument[java.lang.Byte] = builder.build()
  def asShort(builder: ArgumentBuilder[java.lang.Short]): Argument[java.lang.Short] = builder.build()
  def asCharacter(builder: ArgumentBuilder[java.lang.Character]): Argument[java.lang.Character] = builder.build()
  def asString(builder: ArgumentBuilder[String]): Argument[String] = builder.build()
  def asVoid(builder: ArgumentBuilder[Void]): Argument[Void] = builder.build()


  def toScalaSet[T](set: JSet[T]): Set[T] = {
    val buffer = ListBuffer[T]()
    set.forEach(x => buffer += x)
    buffer.toSeq.toSet
  }

  def toJavaOptional[T](opt: Option[T]): Optional[T] = {
    opt match {
      case Some(value) => Optional.of(value)
      case None => Optional.empty()
    }
  }

}
