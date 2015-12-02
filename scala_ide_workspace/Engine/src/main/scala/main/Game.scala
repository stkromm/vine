package main

import actors.threadpool.TimeUnit

class Game {

  val maxFrameDuration = 8
  val renderer = new GLRenderer()
  val fpsManager = new FrameStatManager()

  // Constants
  val MIN_SLEEP_TIME = 500
  val MS_IN_NS = 1000000L
  val SEC_IN_NS = 1000000000L

  def tick(previous: Long): Unit = {
    val current = System.nanoTime
    previous match {
      case 'x' if previous < 0L =>
        throw new NegativeTimeException("The Timepoint passed as an argument to tick in Game is no valid time. The value is negative...")
      case 'x' if (previous > current) =>
        throw new NegativeTimeException("Timepoint passed to tick in Game is a Timepoint in the future.")
      case _ => {}
    }
    renderer.pollInput()
    update()
    renderer.render(previous)
    // Only needed, when vsync disabled and framerate capped
    if (renderer.vsync != 0 && maxFrameDuration != 0) {
      val frameDuration = maxFrameDuration * MS_IN_NS - System.nanoTime + previous
      if (frameDuration >= MIN_SLEEP_TIME) {
        TimeUnit.NANOSECONDS.sleep(frameDuration);
      }
    }
    fpsManager.tick(System.nanoTime - previous);
    println(fpsManager.getFrameDurationMs + "")
    if (!renderer.exit) {
      tick(System.nanoTime)
    }
  }

  def run(): Unit = {
    try {
      init();
      tick(System.nanoTime);
      renderer.close()
    } finally {
      renderer.finish()
    }
  }

  def init(): Unit = {
    renderer.init()
  }
  def update(): Unit = {

  }
}
