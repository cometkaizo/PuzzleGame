package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-22
 * Description: This class represents a mouse moved event
 */
public record MouseMovedEvent(double x, double y, int screenX, int screenY) implements Event {
}
