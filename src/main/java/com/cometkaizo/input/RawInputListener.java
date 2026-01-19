package com.cometkaizo.input;

import com.cometkaizo.world.Tickable;

import java.awt.event.*;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-22
 * Description: This class represents an adapter between the Java listeners API and the InputListener class
 */
public interface RawInputListener extends MouseListener, MouseMotionListener, KeyListener, Tickable {
    void addInputListener(InputListener listener);
    void removeInputListener(InputListener listener);
}
