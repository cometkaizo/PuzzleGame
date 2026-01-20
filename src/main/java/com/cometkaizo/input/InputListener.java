package com.cometkaizo.input;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-22
 * Description: An input listener for mouse and key events
 */
public interface InputListener {

    /// Called when a key is pressed
    void keyPressed(KeyBinding key);
    /// Called every tick that a key is down
    void keyDown(KeyBinding key);
    /// Called when a key is released
    void keyReleased(KeyBinding key);
    /// Called when a mouse button is pressed
    void mousePressed(MouseButtonBinding button, int x, int y);
    /// Called every tick that a mouse button is down
    void mouseDown(MouseButtonBinding button, int x, int y);
    /// Called when a mouse button is released
    void mouseReleased(MouseButtonBinding button, int x, int y);
    /// Called when a mouse button is moved
    void mouseMoved(int x, int y);

}
