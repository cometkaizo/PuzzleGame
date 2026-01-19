package com.cometkaizo.game;

import com.cometkaizo.game.item.Item;
import com.cometkaizo.game.item.ItemTypes;
import com.cometkaizo.game.item.WeighableItem;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Direction;
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
 * Date Modified: 2026-01-17
 * Description: This class represents a single game save state which can be written to and loaded from files
 */
public class GameState {
    // todo: save notes from noteholder too
    public Vector.Double playerPos;
    public List<Item> inventory = new ArrayList<>();
    public boolean keyCollected;
    public Map<String, Boolean> doorsOpen = new HashMap<>();
    public Map<Integer, Boolean> locksOpen = new HashMap<>();
    public boolean anubisScaleUnlocked;
    public WeighableItem anubisWeighedItem;
    public boolean raEmittingLight;
    public Direction raDirection = Direction.UP;
    public Direction mirrorDirection = Direction.UP;
    public boolean venusChestOpen, venusHeartOpen;
    public boolean aresChestOpen, aresHeartOpen;
    public boolean thinkerBrainSolved;
    public boolean hermesSolved;
    public boolean organKeyFallenOut;
    public Map<String, Boolean> morseCodeWorking = new HashMap<>();
    public int[] morseCodePosterNotes = null;

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

        anubisScaleUnlocked = in.readBoolean();
        if (in.readBoolean()) {
            var args = new Args(in.readUTF());
            anubisWeighedItem = (WeighableItem) ItemTypes.ITEMS.get(args.id()).apply(args);
        }

        raEmittingLight = in.readBoolean();
        raDirection = Direction.values()[in.readInt()];

        mirrorDirection = Direction.values()[in.readInt()];

        venusChestOpen = in.readBoolean();
        venusHeartOpen = in.readBoolean();
        aresChestOpen = in.readBoolean();
        aresHeartOpen = in.readBoolean();
        thinkerBrainSolved = in.readBoolean();
        hermesSolved = in.readBoolean();
        organKeyFallenOut = in.readBoolean();

        int morseCodeSize = in.readInt();
        for (int i = 0; i < morseCodeSize; i++) {
            morseCodeWorking.put(in.readUTF(), in.readBoolean());
        }

        int morseCodePosterNotesSize = in.readInt();
        morseCodePosterNotes = new int[morseCodePosterNotesSize];
        for (int i = 0; i < morseCodePosterNotesSize; i++) {
            morseCodePosterNotes[i] = in.readInt();
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

        out.writeBoolean(anubisScaleUnlocked);
        out.writeBoolean(anubisWeighedItem != null);
        if (anubisWeighedItem != null) out.writeUTF(anubisWeighedItem.write());

        out.writeBoolean(raEmittingLight);
        out.writeInt(raDirection.ordinal());

        out.writeInt(mirrorDirection.ordinal());

        out.writeBoolean(venusChestOpen);
        out.writeBoolean(venusHeartOpen);
        out.writeBoolean(aresChestOpen);
        out.writeBoolean(aresHeartOpen);
        out.writeBoolean(thinkerBrainSolved);
        out.writeBoolean(hermesSolved);
        out.writeBoolean(organKeyFallenOut);

        out.writeInt(morseCodeWorking.size());
        for (var entry : morseCodeWorking.entrySet()) {
            out.writeUTF(entry.getKey());
            out.writeBoolean(entry.getValue());
        }

        out.writeInt(morseCodePosterNotes.length);
        for (int id : morseCodePosterNotes) {
            out.writeInt(id);
        }

        out.flush();
    }
}
