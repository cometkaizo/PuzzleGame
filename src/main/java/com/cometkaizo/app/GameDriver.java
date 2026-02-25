package com.cometkaizo.app;

import com.cometkaizo.system.driver.SystemDriver;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-05
 * Description: This class controls ticking the game application
 */
public class GameDriver extends SystemDriver {
    private final GameApp app;
    public static final int
            RENDERS_PER_TICK = 3, // how many times to render the game (approx) per tick of the game
            FPS = 60, // frames per second
            TPS = FPS / RENDERS_PER_TICK, // calculate ticks per second
            TICK_PERIOD = 1000 / TPS, // calculate the number of milliseconds (approx) between ticks
            TICK_BUFFER_DURATION = 10; // the number of milliseconds before a tick is supposed to occur do we allow a tick to occur?

    /**
     * Author: Andy Wang
     * Date Modified: 2026-01-05
     * Description: Constructs a new GameDriver with the given input stream
     * Adds the relevant loops for ticking, rendering, and command parsing
     */

    public class AppParsingRunnable implements Runnable, AutoCloseable{
        private final Scanner scanner;
        public AppParsingRunnable(InputStream input){
            scanner = new Scanner(input);
        }

        @Override
        public void run(){
            if (scanner.hasNextLine()) {
                app.parseInput(scanner.nextLine());
            }
        }

        @Override
        public void close(){
            scanner.close();
        }
    }
    public GameDriver(InputStream input) {
        super(new GameApp());
        this.app = (GameApp) getApp();

        addLoop(new AppParsingRunnable(input), 300, TimeUnit.MILLISECONDS);

        addLoop(() -> {
            long millisSinceLastTick = System.currentTimeMillis() - app.getLastTickTime();
            if (millisSinceLastTick >= TICK_PERIOD - TICK_BUFFER_DURATION) app.tick();
            app.render();
        }, 1000 / FPS, TimeUnit.MILLISECONDS);
    }
}
