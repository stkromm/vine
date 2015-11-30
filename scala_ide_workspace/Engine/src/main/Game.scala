package main

import java.util.concurrent.TimeUnit

class Game {

  val maxFrameDuration = 8
  val renderer = new GLRenderer()
  val fpsManager = new FrameStatManager()

  // Constants
  val MIN_SLEEP_TIME = 500
  val MS_IN_NS = 1000000L
  val SEC_IN_NS = 1000000000L

  def tick(previous: Long) {
    renderer.pollInput()
    update()
    renderer.render(previous)
    // Only needed, when vsync disabled and framerate capped
    if (renderer.vsync != 0 && maxFrameDuration != 0) {
      val frameDuration = maxFrameDuration * MS_IN_NS - java.lang.System.nanoTime + previous
      if (frameDuration >= MIN_SLEEP_TIME) {
        TimeUnit.NANOSECONDS.sleep(frameDuration);
      }
    }
    fpsManager.tick(java.lang.System.nanoTime - previous);
    println(fpsManager.getFrameDurationMs + "")
    if (!renderer.exit) {
      tick(java.lang.System.nanoTime)
    }
  }

  def run() {

    try {
      init();
      tick(java.lang.System.nanoTime);
      renderer.close()
    } finally {
      renderer.finish()
    }
  }

  def init() {
    renderer.init()
  }
  def update() {

  }
}
