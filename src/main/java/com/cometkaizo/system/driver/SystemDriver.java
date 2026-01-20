package com.cometkaizo.system.driver;

import com.cometkaizo.system.app.App;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Controls the setup, cleanup, and ticking of applications
 */
public abstract class SystemDriver {

    private final App app;


    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private final List<Supplier<ScheduledFuture<?>>> waitingTasks = new ArrayList<>(1);
    private final List<ScheduledFuture<?>> tasks = new ArrayList<>(1);
    private boolean isRunning = false;

    /// Creates a new SystemDriver
    protected SystemDriver(App app) {
        this.app = app;
    }

    /// Gets the console input stream
    public static InputStream getConsoleIn() {
        return System.in;
    }

    /// Starts this driver
    public void start() {
        if (isRunning) return;
        isRunning = true;

        setup();
        waitingTasks.forEach(task -> tasks.add(task.get()));
        waitingTasks.clear();
    }

    /// Stops this driver
    public void stop() {
        if (!isRunning) return;
        isRunning = false;

        cleanup();
        tasks.forEach(loop -> loop.cancel(false));
        tasks.clear();
    }

    /// sets up this driver
    protected void setup() {
        app.setup();
    }

    /// cleans up this driver
    protected void cleanup() {
        app.cleanup();
    }


    /// Adds a looping task
    protected final void addLoop(Runnable task, long period, TimeUnit unit, ExceptionManager exceptionManager) {
        addTask(() -> executor.scheduleAtFixedRate(new LoopTask(task, exceptionManager), 0, period, unit));
    }

    /// A task that loops
    private record LoopTask(Runnable task, ExceptionManager exceptionManager) implements Runnable {

        @Override
        public void run() {
            try {
                task.run();
            } catch (Exception e) {
                Throwable newEx = exceptionManager.handleException(e);
                if (newEx != null) throw newEx instanceof RuntimeException r ? r : new RuntimeException(newEx);
            } catch (Error err) {
                Throwable newEx = exceptionManager.handleError(err);
                if (newEx != null) throw newEx instanceof RuntimeException r ? r : new RuntimeException(newEx);
                throw err;
            }
        }
    }

    /// Adds a looping task
    protected final void addLoop(Runnable task, long period, TimeUnit unit) {
        addLoop(task, period, unit, new ExceptionManager() {
            @Override
            public Throwable handleException(Exception e) {
                System.err.println("Encountered exception");
                e.printStackTrace();
                return null;
            }

            @Override
            public Error handleError(Error err) {
                System.err.println("Encountered fatal exception");
                err.printStackTrace();
                return err;
            }
        });
    }

    /// Adds a task
    protected final void addTask(Supplier<ScheduledFuture<?>> task) {
        waitingTasks.add(task);
    }

    /// Gets the app
    public App getApp() {
        return app;
    }

    /// Returns whether this driver is currently running
    public boolean isRunning() {
        return isRunning;
    }

}
