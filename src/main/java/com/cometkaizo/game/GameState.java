package com.cometkaizo.game;

import com.cometkaizo.game.item.Item;
import com.cometkaizo.game.item.ItemTypes;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Vector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a single game save state which can be written to and loaded from files
 */
public class GameState {
    public Vector.Double playerPos;
    public List<Item> inventory = new ArrayList<>();
    public boolean keyCollected;
    public Map<String, Boolean> doorsOpen = new HashMap<>();
    public Map<Integer, Boolean> locksOpen = new HashMap<>();

    public GameState() {}
    public GameState(InputStream is) throws IOException {
        var in = new ObjectInputStream(is);
        // order matters!

        playerPos = Vector.immutable(in.readDouble(), in.readDouble());

        int invSize = in.readInt();
        for (int i = 0; i < invSize; i++) {
            var args = new Args(in.readUTF());
            inventory.add(ItemTypes.ITEMS.get(args.id()).apply(args));
        }

        keyCollected = in.readBoolean();

        int doorsSize = in.readInt();
        for (int i = 0; i < doorsSize; i++) {
            doorsOpen.put(in.readUTF(), in.readBoolean());
        }

        int locksSize = in.readInt();
        for (int i = 0; i < locksSize; i++) {
            locksOpen.put(in.readInt(), in.readBoolean());
        }
    }
    public GameState(Path path) throws IOException {
        var is = Files.newInputStream(path);
        this(is);
        is.close();
    }

    public void write(Path path) throws IOException {
        try (var os = Files.newOutputStream(path)) {
            write(os);
        }
    }
    public void write(OutputStream os) throws IOException {
        var out = new ObjectOutputStream(os);
        // order matters!

        out.writeDouble(playerPos.getX());
        out.writeDouble(playerPos.getY());

        out.writeInt(inventory.size());
        for (var item : inventory) out.writeUTF(item.write());

        out.writeBoolean(keyCollected);

        out.writeInt(doorsOpen.size());
        for (var entry : doorsOpen.entrySet()) {
            out.writeUTF(entry.getKey());
            out.writeBoolean(entry.getValue());
        }

        out.writeInt(locksOpen.size());
        for (var entry : locksOpen.entrySet()) {
            out.writeInt(entry.getKey());
            out.writeBoolean(entry.getValue());
        }

        out.flush();
    }
}
