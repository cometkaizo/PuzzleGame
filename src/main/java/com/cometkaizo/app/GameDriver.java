package com.cometkaizo.app;

import com.cometkaizo.system.driver.SystemDriver;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Author: Andy Wang
 * Date Modified: TODO
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
     * Date Modified: TODO
     * Description: Constructs a new GameDriver with the given input stream
     */
    public GameDriver(InputStream input) {
        super(new GameApp());
        this.app = (GameApp) getApp();

        addLoop(new Runnable() {
            private final Scanner scanner = new Scanner(input);
            @Override
            public void run() {
                if (scanner.hasNextLine()) {
                    app.parseInput(scanner.nextLine());
                }
            }
        }, 300, TimeUnit.MILLISECONDS);

        /*double tickTime = 1000D / TPS;
        addLoop(app::tick, (long) tickTime, TimeUnit.MILLISECONDS);
        addLoop(new Runnable() {
            private int renderCnt = 0;
            @Override
            public void run() {
                renderCnt++;
                app.render(renderCnt % RENDERS_PER_TICK / (double)RENDERS_PER_TICK);
            }
        }, (long) (tickTime / RENDERS_PER_TICK), TimeUnit.MILLISECONDS);*/
        addLoop(() -> {
            long millisSinceLastTick = System.currentTimeMillis() - app.getLastTickTime();
            if (millisSinceLastTick >= TICK_PERIOD - TICK_BUFFER_DURATION) app.tick();
            app.render();
        }, 1000 / FPS, TimeUnit.MILLISECONDS);
    }
}
