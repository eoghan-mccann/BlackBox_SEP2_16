package com.badlogic.game;
/**
 * An interface for objects that require user mouse input.
 */
public interface Clickable {
    /**
     * Checks if a Clickable object has been clicked.
     *
     * @return True if object has been clicked, False  if object has not.
     */
     boolean isClicked();

    /**
     * Checks if a Clickable object is being hovered over.
     *
     * @return True if object is being hovered over, False if not.
     */
     boolean isHoveredOver();
}
