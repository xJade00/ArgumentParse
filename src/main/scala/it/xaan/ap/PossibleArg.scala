package it.xaan.ap

import java.util.NoSuchElementException

abstract class PossibleArg[+T]() {
  def get: T = throw new NoSuchElementException

  def isDefined: Boolean = this match {
    case SomeArg(_) => true
    case _ => false
  }

  def isInvalid: Boolean = this match {

    case InvalidType => true
    case _ => false
  }

  def isEmpty: Boolean = this match {
    case NoArg => true
    case _ => false
  }
}
case object NoArg extends PossibleArg[Nothing]
case object InvalidType extends PossibleArg[Nothing]
case class SomeArg[T](value: T) extends PossibleArg[T] {
  override def get: T = value
}