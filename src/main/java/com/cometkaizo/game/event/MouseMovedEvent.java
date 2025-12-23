package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;

public record MouseMovedEvent(double x, double y, int screenX, int screenY) implements Event {
}
