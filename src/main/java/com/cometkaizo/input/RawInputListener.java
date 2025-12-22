package com.cometkaizo.input;

import com.cometkaizo.world.Tickable;

import java.awt.event.*;

public interface RawInputListener extends MouseListener, MouseMotionListener, KeyListener, Tickable {
    void addInputListener(InputListener listener);
    void removeInputListener(InputListener listener);
}
