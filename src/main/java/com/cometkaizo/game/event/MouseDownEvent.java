package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;
import com.cometkaizo.input.MouseButtonBinding;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a mouse down event
 */
public record MouseDownEvent(MouseButtonBinding input, double x, double y, int screenX, int screenY) implements Event {

}
