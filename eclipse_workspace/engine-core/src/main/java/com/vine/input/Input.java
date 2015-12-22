package com.vine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {

    private static boolean[] keys = new boolean[65536];

    public void invoke(long window, int key, int scancode, int action, int mods) {
        setKey(key, action != GLFW.GLFW_RELEASE);
    }

    private static void setKey(int key, boolean value) {
        keys[key] = value;
    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

}
