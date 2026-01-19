package com.cometkaizo;

import com.cometkaizo.app.GameDriver;
import com.cometkaizo.util.NoSuchResourceException;

import java.io.InputStream;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-22
 * Description: Starting point for the application
 */
public class Main {
    private static final GameDriver driver = new GameDriver(System.in);

    /**
     * Author: Andy Wang
     * Date Modified: 2025-12-19
     * Description: Starting point for the application
     */
    public static void main(String[] args) {
        start();
    }

    /**
     * Author: Andy Wang
     * Date Modified: 2025-12-19
     * Description: Starts the program
     */
    public static void start() {
        driver.start();
    }
    /**
     * Author: Andy Wang
     * Date Modified: 2025-12-19
     * Description: Stops the program
     */
    public static void stop(int exitCode) {
        driver.stop();
        System.exit(exitCode);
    }

    /**
     * Author: Andy Wang
     * Date Modified: 2025-12-19
     * Description: Prints a message to the console
     */
    public static void log(String message) {
        System.out.println(message);
    }
    /**
     * Author: Andy Wang
     * Date Modified: 2025-12-19
     * Description: Prints an error message to the console
     */
    public static void err(String message) {
        System.err.println(message);
    }

    /**
     * Author: Andy Wang
     * Date Modified: 2025-12-22
     * Description: Gets a resource from the resources folder
     */
    public static InputStream getResource(String p) {
        var resource = Main.class.getResourceAsStream(p.replaceAll("\\\\", "/"));
        if (resource == null) throw new NoSuchResourceException("Cannot find resource: " + p);
        return resource;
    }
}