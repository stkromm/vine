package util

import java.awt.Toolkit
import scala.swing.Dimension
import scala.swing.Rectangle

/**
 * @author Steffen
 */
object ScreenUtils {
  def getScaledScreenDimension(widthScale: Double, heightScale: Double): Dimension = {
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    val width = double2Double(screenSize.getWidth * widthScale).intValue();
    val height = double2Double(screenSize.getHeight * heightScale).intValue();
    new Dimension(width, height)
  }

  def createRectangle(pos: Dimension, extend: Dimension): Rectangle = {
    new Rectangle(double2Double(pos.getWidth).intValue(),
      double2Double(pos.getHeight).intValue(),
      double2Double(extend.getWidth).intValue(),
      double2Double(extend.getHeight).intValue())
  }
}