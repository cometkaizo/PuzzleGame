package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;
import com.cometkaizo.input.KeyBinding;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: This class represents a key pressed event
 */
public record KeyPressedEvent(KeyBinding input) implements Event {

}
