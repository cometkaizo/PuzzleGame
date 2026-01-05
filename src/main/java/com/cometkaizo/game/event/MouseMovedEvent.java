package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a mouse moved event
 */
public record MouseMovedEvent(double x, double y, int screenX, int screenY) implements Event {
}
