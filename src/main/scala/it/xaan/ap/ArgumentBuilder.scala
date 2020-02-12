package it.xaan.ap

import java.util.{List => JList}

import scala.collection.mutable.ListBuffer

class ArgumentBuilder[T](name: String) {
  private var required: Boolean = false
  private var allowedArgs: Seq[T] = Seq.empty

  def required(required: Boolean): ArgumentBuilder[T] = {
    this.required = required
    this
  }

  def allowedArgs(allowedArgs: JList[T]): ArgumentBuilder[T] = {
    val buffer = ListBuffer[T]()
    allowedArgs.forEach(x => buffer += x)
    this.allowedArgs = buffer.toSeq
    this
  }

  /**
   * Not to be called outside of scala otherwise you run into the same problems as before.
   *
   * @param argType The implicit arg type
   * @return A built Argument
   */
  def build()(implicit argType: ArgumentType[T]): Argument[T] = Argument(name, allowedArgs, required)
}
