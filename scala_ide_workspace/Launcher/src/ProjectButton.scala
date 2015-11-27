import scala.swing._
import scala.swing.event._
import util.ScreenUtils

/**
 * @author Steffen
 */
class ProjectButton(name: String) extends Button {
  text = name
  listenTo(mouse.clicks)
  preferredSize = ScreenUtils.getScaledScreenDimension(0.6 * 0.25, 0.6 * 0.5 * 0.025)
  font = new Font("Arial", java.awt.Font.PLAIN, 18)
  reactions += {
    case evt @ MousePressed(b, pt, _, _, _) => evt.peer.getButton match {
      case 1 => println("left clicked")
      case 2 => println("pressed scroll wheel")
      case 3 => println("right clicked")
      case _ => println("pressed unknown mouse button")
    }
  }
  border = Swing.EmptyBorder(10, 30, 10, 30)
}