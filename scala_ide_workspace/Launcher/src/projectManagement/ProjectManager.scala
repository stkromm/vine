
package projectManagement

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import settings.Settings

/**
 * @author Steffen
 */
object ProjectManager {

  def isProject(projectDir: File): Boolean = {
    lazy val list = projectDir.list
    if (!projectDir.exists() | !projectDir.isDirectory() | list == null) return false
    list find (x => x.equals("project.ini")) isDefined
  }

  def copyProject(srcFile: File, destPath: String) {
    val file = new File(destPath + srcFile.getName)
    if (file.isDirectory()) {
      file.mkdir()
      file.listFiles.toList foreach
        (x => if (x.canRead && !x.isHidden) { copyProject(x, file.getAbsolutePath + File.separator) })
    } else {
      new FileOutputStream(file) getChannel () transferFrom (
        new FileInputStream(file) getChannel, 0, Long.MaxValue)
    }
  }

  def importProject(projectDir: File) {
    if (isProject(projectDir)) copyProject(projectDir, Settings.getWorkspace + File.separator)
  }

  def exportProject(projectDir: File, dest: File) {
    if (isProject(projectDir)) copyProject(projectDir, dest.getAbsolutePath + File.separator);
  }

  def getProjectsListed(workspace: String): List[File] = {
    val workspaceDir = new File(workspace)
    if (workspaceDir.exists() && workspaceDir.isDirectory()) { workspaceDir.listFiles().filter(isProject(_)).toList }
    else Nil
  }

  def createProject(projectName: String) {
    val project = new File(Settings.getWorkspace + File.separator + projectName)
    if (!project.exists) project.mkdir()
    val bw = new PrintWriter(new File(project.getAbsolutePath + File.separator + "project.ini"))
  }

  def removeProject(projectName: String) {

  }
}