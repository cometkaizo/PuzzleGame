package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;
import com.cometkaizo.input.KeyBinding;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a key up event
 */
public record KeyReleasedEvent(KeyBinding input) implements Event {

}
