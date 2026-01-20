package com.cometkaizo.system.app;

import com.cometkaizo.world.Tickable;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: An application that can be setup and cleaned up, as well as ticked
 */
public abstract class App implements Tickable {

    private final AppSettings settings;

    /// Creates a new app
    protected App(AppSettings settings) {
        this.settings = settings;
    }

    /// Initializes the app
    public void setup() {

    }

    /// Cleans up the app for termination
    public void cleanup() {

    }

    /// Ticks the app
    public void tick() {

    }

    /// Get the settings associated with this app
    public AppSettings getSettings() {
        return settings;
    }
}
