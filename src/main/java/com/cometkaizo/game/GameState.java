package com.cometkaizo.game;

import com.cometkaizo.world.Vector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a single game save state which can be written to and loaded from files
 */
public class GameState {
    public Vector.MutableDouble playerPos;

    public GameState() {}
    public GameState(InputStream is) throws IOException {
        var in = new ObjectInputStream(is);
        // order matters!
        playerPos = Vector.mutable(in.readDouble(), in.readDouble());
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
        out.flush();
    }
}
