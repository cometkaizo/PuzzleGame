package com.cometkaizo.game;

import com.cometkaizo.world.Vector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameState {
    public Vector.ImmutableDouble playerPos;

    public GameState() {}
    public GameState(InputStream is) throws IOException {
        var in = new ObjectInputStream(is);
        // order matters!
        playerPos = Vector.immutable(in.readDouble(), in.readDouble());
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
        out.writeDouble(playerPos.x);
        out.writeDouble(playerPos.y);
        out.flush();
    }
}
