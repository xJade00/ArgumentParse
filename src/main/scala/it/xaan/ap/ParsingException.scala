package it.xaan.ap

class ParsingException(message: String) extends RuntimeException(s"There was an error parsing: $message")
case class MissingArgumentsException(arguments: Set[Argument[_]]) extends ParsingException(s"Couldn't find arguments: $arguments")
case class FailedValidationException(failed: Set[Argument[_]]) extends ParsingException(s"Couldn't parse arguments: $failed")
