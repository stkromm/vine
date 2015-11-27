package main

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._

import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._

class Screen {

  // We need to strongly reference callback instances.
  var errorCallback = None: Option[GLFWErrorCallback]
  var keyCallback = None: Option[GLFWKeyCallback]

  // The window handle
  var window = 0: Long;

  def run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    try {
      init();
      loop();

      // Release window and window callbacks
      glfwDestroyWindow(window);
      //keyCallback.get.release();
    } finally {
      // Terminate GLFW and release the GLFWErrorCallback
      glfwTerminate();
      errorCallback.get.release();
    }
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

    val WIDTH = 300;
    val HEIGHT = 300;

    // Create the window
    window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    /* Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
            }
        });*/

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
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  def loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    // Set the clear color
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (glfwWindowShouldClose(window) == GLFW_FALSE) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

      glfwSwapBuffers(window); // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();
    }
  }

  def main(args: Array[String]) {
    run();
  }

}