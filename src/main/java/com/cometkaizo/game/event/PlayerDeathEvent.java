package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;
import com.cometkaizo.world.entity.Player;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: This class represents a player down event
 */
public record PlayerDeathEvent(Player player) implements Event {
}
