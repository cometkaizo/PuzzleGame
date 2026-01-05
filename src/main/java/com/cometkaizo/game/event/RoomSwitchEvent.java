package com.cometkaizo.game.event;

import com.cometkaizo.event.Event;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.entity.Player;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a room switch event
 */
public record RoomSwitchEvent(Player player, Room from, Room to) implements Event {
}
