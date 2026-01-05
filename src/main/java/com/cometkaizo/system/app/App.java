package com.cometkaizo.system.app;

import com.cometkaizo.world.Tickable;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: An application that can be setup and cleaned up, as well as ticked
 */
public abstract class App implements Tickable {

    private final AppSettings settings;

    protected App(AppSettings settings) {
        this.settings = settings;
    }

    public void setup() {

    }

    public void cleanup() {

    }

    public void tick() {

    }


    public AppSettings getSettings() {
        return settings;
    }
}
