package settings

import java.util.prefs._
import scala.Iterator

/**
 * @author Steffen
 */
object Settings {
  private lazy val preferences = Preferences.userRoot;

  val names: Iterator[String] = Iterator(
    "workspace",
    "language")

  def setLanguage(language: String) {
    preferences.node("com.sparkling.launcher").put("language", language)
  }
  def getLanguage: String = preferences.node("com.sparkling.launcher").get("workspace", System.getProperty("user.home"))

  def setWorkspace(workspacePath: String) {
    preferences.node("com.sparkling.launcher").put("workspace", workspacePath)
  }
  def getWorkspace: String = preferences.node("com.sparkling.launcher").get("workspace", System.getProperty("user.home"))
}
  /* Property Management 
  def loadSettings {
    val settings = new File(System.getProperty("java.io.tmpdir") + "sparkling_settings.ini")
    if (!settings.exists() && !settings.createNewFile()) {
      println("Couldn't create sparkling.ini file")
    }
    val lineList = Source.fromFile(settings.getAbsolutePath).getLines
    val lineIndexList = Source.fromFile(settings.getAbsolutePath).getLines map (_.indexOf("="))
    val tuples = (lineList zip lineIndexList) filter
      (y => y._2 != -1 && names.contains(y._1.substring(0, y._2)))
    tuples foreach
      (tuple => System.setProperty(tuple._1.substring(0, tuple._2), tuple._1.substring(tuple._2 + 1)))
  }

  def saveSettings {
    val bw = new PrintWriter(new File(System.getProperty("java.io.tmpdir") + "sparkling_settings.ini"))
    names foreach (x => bw.write(x + "=" + System.getProperty(x) + System.lineSeparator()))
    bw.close
  }
*/