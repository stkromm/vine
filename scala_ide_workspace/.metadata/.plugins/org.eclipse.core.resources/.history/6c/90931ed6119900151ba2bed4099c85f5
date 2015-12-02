package main

class FrameStatManager {
  private var frames: List[Long] = Nil
  private var avgFrameDuration = 30L;

  def getFrameDurationMs: Double = {
    frames match {
      case lastFrameDuration :: xs => {
        ((avgFrameDuration * 0.9) + (lastFrameDuration * 0.1)) / 1000000
      }
      case Nil => 0
      case _   => 0
    }
  }

  def tick(duration: Long) {
    if (frames.length >= 100) {
      avgFrameDuration = frames.sum / frames.length
      frames = duration :: Nil
    } else {
      frames = duration :: frames
    }
  }
}