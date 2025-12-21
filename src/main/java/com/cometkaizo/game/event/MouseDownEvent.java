package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;
import com.cometkaizo.input.MouseButtonBinding;

public record MouseDownEvent(MouseButtonBinding input, double x, double y, int screenX, int screenY) implements Event {

}
