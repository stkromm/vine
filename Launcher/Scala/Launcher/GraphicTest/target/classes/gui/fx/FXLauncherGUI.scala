package gui.fx

import javafx.{ collections => jfxc }
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.stage._
import scalafx.event._
import scalafx.scene.input.MouseEvent
import scalafx.geometry.Point2D
import scalafx.scene.Node
import javafx.scene.{ input => jfxsi }
import scalafx.scene.Group
import scalafx.scene.layout.AnchorPane
import scalafx.beans.Observable
import javafx.collections.ObservableList
import scala.collection.JavaConverters._
/**
 * @author Steffen
 */
object FXLauncherGUI extends JFXApp {
  val buttonBar = new Button("Projects") {
    id = "projectsButton"
    onAction = { (_: ActionEvent) => }
  }
  val launcherContent = new BorderPane {
    hgrow = Priority.Always
    styleClass += "content"
    val sideList = new ListView[String] {
      prefWidth <== Screen.primary.bounds.height / 4;
    }
    updateWorkspaceList(sideList)
    val sidebar = new VBox {
      children = List(sideList,
        new HBox {
          children = List(
            new Button("+") {
              onAction = { (_: ActionEvent) =>
                Platform.runLater {
                  val dialogStage = new Stage {
                    initStyle(scalafx.stage.StageStyle.TRANSPARENT)
                    title = "Stand-Alone Dialog"
                    scene = new Scene {
                      stylesheets += this.getClass.getResource("/fx/css/dialog.css").toExternalForm
                      root = new VBox {
                        children = List(
                          new Button("Import existing Project"),
                          new Button("Create new Project") {
                            onAction = {
                              (_: ActionEvent) =>
                                handle { Platform.runLater() -> createProjectCreationDialog }
                            }
                          },
                          new Button("Cancel") { onAction = { (_: ActionEvent) => close } })
                      }
                    }
                  }

                  // Show dialog and wait till it is closed
                  dialogStage.showAndWait()
                }
              }
            },
            new Button("-") {
            })
        })
    }

    left = sidebar
    center = new ListView {
      styleClass += "content"
    }
    bottom = new Region {
      prefHeight = 5
    }
    right = new Region {
      prefWidth = 5
    }
  }
  val toolbarIcon = new ComboBox[String] {
    id = "toolbarIcon"
    prefHeight = Screen.primary.bounds.height / 20
    prefWidth = Screen.primary.bounds.height / 20
  }

  def updateWorkspaceList(projectList: ListView[String]) {
    // Set current content
    val list =
      for (project <- projectManagement.ProjectManager.getProjectsListed(settings.Settings.getWorkspace))
        yield (project.getName)
    projectList.items.setValue(jfxc.FXCollections.observableList(list.asJava))
  }

  /*
   *  Init stage
   * 
   * 
   * 
   */
  stage = createStage

  private def createStage(): JFXApp.PrimaryStage = {
    val resizeIcon = new ImageView("/images/resize.png") {
      fitHeight = 10
      preserveRatio = true
      styleClass += "resizeElement"
    }
    val bottomResizeBorder = new HBox {
      val spacer = new Region()
      HBox.setHgrow(spacer, Priority.Always)
      children = List(spacer, resizeIcon)
      styleClass += "resizeElement"
    }
    val toolbar = createToolbar

    val rightResizeBorder = new Region() {
      prefWidth = 10
      prefHeight = Screen.primary.bounds.height / 2.0 - toolbar.height.get - bottomResizeBorder.height.get
      vgrow = Priority.Always
      styleClass += "resizeElement"
    }
    val launcherContentLayout = new HBox {
      children = List(launcherContent)
      styleClass += "resizeElement"
    }
    val mainElementLayout = new VBox {
      children = List(toolbar, launcherContentLayout)
      fillWidth = true
      //styleClass += "root"
    }
    val s = new JFXApp.PrimaryStage {
      title.value = "Sparkling Launcher"
      width = Screen.primary.bounds.width / 2.0
      height = Screen.primary.bounds.height / 2.0
      minWidth = Screen.primary.bounds.width / 3
      minHeight = Screen.primary.bounds.height / 3
      // Remove standard window border
      initStyle(scalafx.stage.StageStyle.TRANSPARENT)
      // Set taskbar icon
      icons += new Image("/images/icon.png")
      // Init Elements
      width.onChange {
        toolbar.setPrefWidth(stage.width.get)
      }
      height.onChange {
        rightResizeBorder.setPrefHeight(stage.height.get - toolbar.height.get - bottomResizeBorder.height.get)
      }

      scene = new Scene {
        stylesheets += this.getClass.getResource("/fx/css/launcher.css").toExternalForm
        fill = "#0B4466"
        content = new Group(mainElementLayout)
      }
      launcherContentLayout.prefHeight <== scene.height - toolbar.height
      launcherContentLayout.prefWidth <== scene.width
      // Windows is hidden and not closed, if a tray is used
      onCloseRequest =
        if (util.Tray.installTray(stage.show())) { handle { Platform.runLater(() -> stage.hide()) } }
        else { handle { Platform.runLater(() -> System.exit(0)) } }

      onShowing = { handle { stage.centerOnScreen() } }
      // Configure ui elements

    }
    // Needed for SystemTray ( call stage functions not on the javafx thread)
    Platform.implicitExit = false
    makeNodeResizePoint(s, mainElementLayout)
    s
  }
  /*
   * 
   * Creating functions
   * 
   * 
   * 
   */
  private def createProjectCreationDialog(): Stage = {
    val popupStage = new Stage {
      outer =>
      title = "Stand-Alone Dialog"
      scene = new Scene {
        root = new BorderPane {
          padding = Insets(25)
          bottom = new Button {
            text = "Click me to close the dialog"
            onAction = handle { outer.close() }
          }
        }
      }
      initOwner(stage)
      initModality(Modality.WINDOW_MODAL)
      focused.onChange { if (!focused.value) close() }
      alwaysOnTop = true;
    }
    popupStage.showAndWait()
    popupStage.requestFocus()
    popupStage
  }
  private def importProjectDialog(): Stage =
    new Stage {
      outer =>
      title = "Stand-Alone Dialog"
      scene = new Scene {
        root = new BorderPane {
          padding = Insets(25)
          bottom = new Button {
            text = "Click me to close the dialog"
            onAction = handle { outer.close() }
          }
        }
      }
    }
  /*
   * Constructs a new custom Toolbar
   */
  private def createToolbar(): ToolBar = {
    // Icon + dropdown menu
    toolbarIcon.+=("About")
    toolbarIcon.+=("Exit")

    val toolbar = new ToolBar {
      val spacer = new Region()
      HBox.setHgrow(spacer, Priority.Always)
      styleClass += "toolbar"
      prefWidth = Screen.primary.bounds.width / 2.0
      items.add(toolbarIcon)
      items.add(buttonBar)
      items.add(spacer)
      val windowControlButtons = new HBox {
        children = List(
          new Button {
            onAction = { (_: ActionEvent) => stage.setIconified(!stage.iconified.value) }
            id = "minimizeButton"
          },
          new Button {
            onAction = { (_: ActionEvent) => stage.setMaximized(!stage.maximized.value) }
            id = "maximizeButton"
          },
          new Button {
            onAction = { (_: ActionEvent) => stage.close() }
            id = "closeButton"
          })
      }
      items.add(windowControlButtons)
    }
    makeNodeDragPoint(toolbar)
    toolbar
  }

  /*
   * Makes the stage at the given node draggable on the screen.
   */
  private def makeNodeDragPoint(node: Node) {
    var dragAnchorPoint = new Point2D(0, 0)
    var previousLocation = new Point2D(0, 0)
    node.onMousePressed = (event: MouseEvent) => {
      dragAnchorPoint = new Point2D(event.screenX, event.screenY)
      previousLocation = new Point2D(stage.getX, stage.getY)
      node match {
        case x: ComboBox[_] => x.hide()
        case _              =>
      }
    }
    node.onMouseDragged = (event: MouseEvent) =>
      if (event.getButton == jfxsi.MouseButton.PRIMARY) {
        stage.x = previousLocation.x + event.screenX - dragAnchorPoint.x
        stage.y = math.min(Screen.primary.bounds.height.get * (19.0 / 20.0), previousLocation.y + event.screenY - dragAnchorPoint.y)
      }

  }

  private def makeNodeResizePoint(stage: JFXApp.PrimaryStage, node: Node) {
    var previousHeight = 0.0
    var previousWidth = 0.0
    var dragAnchorPoint = new Point2D(0, 0)
    node.onMousePressed = (event: MouseEvent) => {
      dragAnchorPoint = new Point2D(event.screenX, event.screenY)
      reset()
    }
    node.onMouseDragged = (event: MouseEvent) =>
      if (dragAnchorPoint != null) {
        if (event.getButton == jfxsi.MouseButton.PRIMARY) {
          stage.width = math.min(math.max(stage.minWidth, previousWidth + event.screenX - dragAnchorPoint.x), Screen.primary.bounds.width)
          stage.height = math.min(math.max(stage.minHeight, previousHeight + event.screenY - dragAnchorPoint.y), Screen.primary.bounds.height)
        }
      }
    stage.onShown = (_: WindowEvent) => reset()
    def reset(): Unit = {
      previousHeight = stage.getHeight
      previousWidth = stage.getWidth
    }
  }
}