package com.cometkaizo.world;

import com.cometkaizo.Main;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameState;
import com.cometkaizo.game.LoadException;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;

import java.io.IOException;
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

    public World(Game game, String namespace, String name, List<Room> rooms) {
        this.game = game;
        this.namespace = namespace;
        this.name = name;
        rooms.forEach(this::addRoom);
    }

    public World(Game game, Path directoryPath) throws IOException {
        this.game = game;
        read(directoryPath);
    }


    void addRoom(Room room) {
        rooms.put(room.getNamespace(), room);
        room.onAddedTo(this);
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public Room getRoom(String namespace) {
        Room room = rooms.get(namespace);
        if (room == null) throw new NoSuchElementException("Unknown room '" + namespace + "'; available rooms: " + rooms.values().stream().map(Room::getNamespace).toList());
        return room;
    }


    @Override
    public void tick() {
    }

    @Override
    public void render(Canvas canvas) {

    }

    public void write(GameState state) {
        for (var room : rooms.values()) room.write(state);
    }

    public void read(Path path) throws IOException {
        rooms.clear();

        try (Scanner in = new Scanner(Main.getResource(path + "/info.txt"))) {
            for (var roomDir : in.nextLine().split(";")) {
                addRoom(new Room(game, this, path.resolve(roomDir)));
            }
        } catch (Exception e) {
            throw new LoadException("World failed to load", e);
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public static class Builder {
        private Game game;
        private String namespace;
        private String name;
        private List<Room> rooms;

        public Builder(Game game, String namespace, String name, List<Room> rooms) {
            this.game = game;
            this.namespace = namespace;
            this.name = name;
            this.rooms = rooms;
        }

        public Builder(Game game, String namespace, String name) {
            this(game, namespace, name, new ArrayList<>(1));
        }

        public Builder(World world) {
            this(world.game, world.namespace, world.name, List.copyOf(world.rooms.values()));
        }

        public Builder setGame(Game game) {
            this.game = game;
            return this;
        }

        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setRooms(List<Room> rooms) {
            this.rooms = rooms;
            return this;
        }

        public Builder withRoom(Room room) {
            this.rooms.add(room);
            return this;
        }

        public World build() {
            return new World(game, namespace, name, rooms);
        }
    }

}
