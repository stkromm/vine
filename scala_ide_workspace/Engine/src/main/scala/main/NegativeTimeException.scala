package main

class NegativeTimeException private (ex: RuntimeException) extends RuntimeException(ex) {
  def this(message: String) = this(new RuntimeException(message))
  def this(message: String, throwable: Throwable) = this(new RuntimeException(message, throwable))
}

object MissingConfigurationException {
  def apply(message: String) = new NegativeTimeException(message)
  def apply(message: String, throwable: Throwable) = new NegativeTimeException(message, throwable)
}