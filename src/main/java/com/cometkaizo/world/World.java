package com.cometkaizo.world;

import com.cometkaizo.Main;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameState;
import com.cometkaizo.game.LoadException;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;

import java.nio.file.Path;
import java.util.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: A world containing a list of rooms
 */
public class World implements Tickable, Renderable {
    private final Game game;
    private String namespace;
    private String name;
    private final Map<String, Room> rooms = new HashMap<>(2);

    /// Creates a new world with the given rooms
    public World(Game game, String namespace, String name, List<Room> rooms) {
        this.game = game;
        this.namespace = namespace;
        this.name = name;
        rooms.forEach(this::addRoom);
    }

    /// Creates a new world by reading it from the given path
    public World(Game game, Path directoryPath) {
        this.game = game;
        read(directoryPath);
    }


    /// Adds a room to this world
    void addRoom(Room room) {
        rooms.put(room.getNamespace(), room);
    }

    /// Gets all the rooms in this world
    public Map<String, Room> getRooms() {
        return rooms;
    }

    /// Gets the room with the given id, or throws an exception if it does not exist
    public Room getRoom(String namespace) {
        Room room = rooms.get(namespace);
        if (room == null) throw new NoSuchElementException("Unknown room '" + namespace + "'; available rooms: " + rooms.values().stream().map(Room::getNamespace).toList());
        return room;
    }


    /// Updates this world every tick
    @Override
    public void tick() {
    }

    /// Render this world to the screen
    @Override
    public void render(Canvas canvas) {

    }

    /// Saves this world to the game state
    public void write(GameState state) {
        for (var room : rooms.values()) room.write(state);
    }

    /// Reads this world in from the given path
    public void read(Path path) {
        rooms.clear();

        try (Scanner in = new Scanner(Main.getResource(path + "/info.txt"))) {
            for (var roomDir : in.nextLine().split(";")) {
                addRoom(new Room(game, this, path.resolve(roomDir)));
            }
        } catch (Exception e) {
            throw new LoadException("World failed to load", e);
        }
    }

    /// Returns the id of this room
    public String getNamespace() {
        return namespace;
    }

    /// Returns the name of this room
    public String getName() {
        return name;
    }

}
