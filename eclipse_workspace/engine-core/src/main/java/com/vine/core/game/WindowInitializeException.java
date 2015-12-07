package com.vine.core.game;

/**
 * Gets created, when the game window creation fails.
 * 
 * @author Steffen
 *
 */
public class WindowInitializeException extends Exception {

    /**
     * Default serial id.
     */
    private static final long serialVersionUID = 1L;

    public WindowInitializeException(String message) {
        super(message);
    }

}
