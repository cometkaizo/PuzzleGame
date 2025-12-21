package com.cometkaizo.input;

import java.awt.event.*;

public interface RawInputListener extends MouseListener, MouseMotionListener, KeyListener {
    void addInputListener(InputListener listener);
    void removeInputListener(InputListener listener);
}
