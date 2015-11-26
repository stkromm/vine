package util

import scalafx.application.Platform
import scalafx.stage.Stage
import java.io.BufferedInputStream
import java.awt.TrayIcon
import java.awt.event.{ ActionEvent, ActionListener, MouseAdapter, MouseEvent }
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import scala.util.Try
import java.awt.SystemTray
import java.awt.PopupMenu
import java.awt.MenuItem

object Tray {
  final def installTray(show: => Unit): Boolean = {
    val isSupported = SystemTray.isSupported
    if (isSupported && false) {
      val popup = new PopupMenu()
      popup add menuItem("Open", Platform.runLater(show))
      popup add menuItem("Exit", { Platform.exit(); System.exit(0) })
      val image = Try(ImageIO.read(this.getClass.getResourceAsStream("/images/trayIcon.png"))).getOrElse(new BufferedImage(256, 256,
        BufferedImage.TYPE_INT_RGB))

      val trayIcon = new TrayIcon(image, "Open Sparkling Launcher", popup)
      trayIcon setImageAutoSize true
      // Probable problems with combination with popup menu
      trayIcon.addMouseListener(new MouseAdapter {
        override def mouseClicked(e: MouseEvent): Unit = {
          if (e.getClickCount == 1 && e.getButton == MouseEvent.BUTTON1) {
            Platform.runLater(show)
          }
        }
      })
      //
      val tray = SystemTray.getSystemTray
      Try(tray add trayIcon).map(_ => {
        trayIcon.displayMessage("Sparkling Launcher", "Sparkling Launcher is now running.", TrayIcon.MessageType.INFO)
      })
    }
    isSupported
  }
  private def menuItem(label: String, onClick: => Unit) = {
    val item = new MenuItem(label)
    item addActionListener actionListener(onClick)
    item
  }
  private def actionListener(code: => Unit) = new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      code
    }
  }
}