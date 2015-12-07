package main

/**
 * Abstract renderer implemented through an graphics provider like OpenGL.
 */
abstract class Renderer {
  val WIDTH = 300;
  val HEIGHT = 300;
  val title = "Hello World!"
  val vsync = 2

  def init: Unit
  def render(lastTimeRendered: Long): Unit
  def exit: Boolean
  def finish: Unit
  def close: Unit
}