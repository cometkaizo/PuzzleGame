package com.cometkaizo.input;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: An input listener for mouse and key events
 */
public interface InputListener {

    void keyPressed(KeyBinding key);
    void keyDown(KeyBinding key);
    void keyReleased(KeyBinding key);
    void mousePressed(MouseButtonBinding button, int x, int y);
    void mouseDown(MouseButtonBinding button, int x, int y);
    void mouseReleased(MouseButtonBinding button, int x, int y);
    void mouseMoved(int x, int y);

}
