import scala.swing._
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
import javax.swing.UIManager
import util.ScreenUtils
import settings.Settings
import projectManagement.ProjectManager

object LauncherGUI extends SimpleSwingApplication {
  UIManager.setLookAndFeel(new NimbusLookAndFeel())
  val projectList: BoxPanel = new BoxPanel(Orientation.Vertical) {
  }

  def top = {
    /* Settings.loadSettings 
    if (Settings.getWorkspace.equals("")) {
      createWorkspaceDialog
    }*/
    createMainWindow
  }

  def createMainWindow() = {
    new MainFrame {
      title = "Sparkling Launcher!"
      val pos = ScreenUtils.getScaledScreenDimension(0.2, 0.2)
      val extend = ScreenUtils.getScaledScreenDimension(0.6, 0.6)
      bounds = ScreenUtils.createRectangle(pos, extend)
      preferredSize = extend
      val scroll = new ScrollPane(
        new BoxPanel(Orientation.Vertical) {
          contents += projectList
        }) {
        preferredSize = ScreenUtils.getScaledScreenDimension(0.6 * 0.25, 0.6 * 0.5);
      }
      scroll.horizontalScrollBar.visible = false
      contents = new BorderPanel {
        add(new BoxPanel(Orientation.Vertical) {
          contents += new Label("Projects")
          contents += scroll
        }, BorderPanel.Position.West)
      }

      menuBar = new MenuBar {
        contents += new Menu("Einstellungen") {
          contents += new MenuItem(Action("Select Workspace") {
            createWorkspaceDialog
          })
          contents += new MenuItem("Sprache")
          contents += new Separator()
          contents += new MenuItem(Action("Beenden") { quit })
        }
        contents += new Menu("Datei") {
          contents += new MenuItem(Action("Neues Projekt") {
            val createDialog = new Dialog
            createDialog.contents = new BoxPanel(Orientation.Vertical) {
              val text = new TextField("Project1");
              contents += text
              contents += new Button(Action("Create") {
                ProjectManager.createProject(text.text)
                updateWorkspaceList
                createDialog.dispose
              })
            }
            createDialog.open
          })
          contents += new MenuItem(Action("Importieren") {
            val chooser = new FileChooser()
            chooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
            chooser.title = "Import exisiting project."
            if (chooser.showOpenDialog(this) == FileChooser.Result.Approve) {
              ProjectManager.importProject(chooser.selectedFile)
              updateWorkspaceList
            }
          })
          contents += new MenuItem(Action("Exportieren") {
            val chooser = new FileChooser()
            chooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
            chooser.title = "Select project"
            if (chooser.showOpenDialog(this) == FileChooser.Result.Approve) {
              val pro = chooser.selectedFile;
              if (chooser.showSaveDialog(this) == FileChooser.Result.Approve) {
                ProjectManager.exportProject(pro, chooser.selectedFile)
              }
            }
          })
        }
        contents += new Separator
        contents += new Menu("Hilfe") {
          contents += new MenuItem("Support")
        }
      }

      updateWorkspaceList
    }
  }

  def createWorkspaceDialog: Dialog = {
    val options = new Dialog();
    val path = new TextField(System.getProperty("user.home") + "sparkling_workspace");
    if (!Settings.getWorkspace.equals("")) path.text = Settings.getWorkspace
    path.editable = false
    val pos = ScreenUtils.getScaledScreenDimension(0.2, 0.2)
    val extend = ScreenUtils.getScaledScreenDimension(0.6, 0.6)
    options.bounds = ScreenUtils.createRectangle(pos, extend)
    //
    //
    // Content
    options.contents = new BoxPanel(Orientation.Vertical) {
      contents += new Label("Set your workspace directory") {
        border = Swing.EmptyBorder(0, 0, 30, 30)
        font = new Font("Arial", java.awt.Font.PLAIN, 18)
      }
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += path
        contents += new Button(Action("Select") {
          val chooser = new FileChooser()
          chooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
          chooser.title = "Select the workspace folder"
          if (chooser.showOpenDialog(this) == FileChooser.Result.Approve) {
            path.text = chooser.selectedFile.getAbsolutePath
          }
        })
      }
      contents += new BoxPanel(Orientation.Horizontal) {
        border = Swing.EmptyBorder(10, 30, 10, 30)
        contents += new Button(Action("Ok") {
          Settings.setWorkspace(path.text)
          updateWorkspaceList
          options.dispose
        })
        contents += new Button(Action("Cancel") {
          options.dispose
        })
      }
    }
    //  
    options.open
    options
  }

  def updateWorkspaceList {
    println("Called update workspace list")
    // Delete projectList's content
    projectList.contents.clear
    // Set current content
    for (project <- ProjectManager.getProjectsListed(Settings.getWorkspace)) {
      projectList.contents += new ProjectButton(project.getName)
    }
    // Show new project list on gui
    projectList.revalidate
  }

}