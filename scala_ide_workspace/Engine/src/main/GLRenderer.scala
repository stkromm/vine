package main

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._

import scala.util.Try

class GLRenderer extends Renderer {

  // We need to strongly reference callback instances.
  private var errorCallback = None: Option[GLFWErrorCallback]
  private var keyCallback = None: Option[GLFWKeyCallback]
  def pollInput() {
    glfwPollEvents();
  }
  def close() {
    // Release window and window callbacks
    glfwDestroyWindow(glfwGetCurrentContext);
  }
  def finish() {
    Try(keyCallback.get.release());
    // Terminate GLFW and release the GLFWErrorCallback
    glfwTerminate();
    Try(errorCallback.get.release());
  }
  def render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    glfwSwapBuffers(glfwGetCurrentContext); // swap the color buffers
  }
  def exit: Boolean = {
    glfwWindowShouldClose(glfwGetCurrentContext) == GLFW_TRUE
  }
  def init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    errorCallback = Option(glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err)))

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (glfwInit() != GLFW_TRUE)
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure our window
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Create the window
    val window = glfwCreateWindow(WIDTH, HEIGHT, title, NULL, NULL);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    // Get the resolution of the primary monitor
    val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    // Center our window
    glfwSetWindowPos(
      window,
      (vidmode.width() - WIDTH) / 2,
      (vidmode.height() - HEIGHT) / 2);

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(vsync);
    // Make the window visible
    glfwShowWindow(window);
    GL.createCapabilities();
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
  }
}