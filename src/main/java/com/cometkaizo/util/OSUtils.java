package com.cometkaizo.util;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-21
 * Description: Useful OS-related methods
 */
public class OSUtils {

    /// Returns the name of the operating system
    public static String os() {
        return System.getProperty("os.name").toUpperCase();
    }

    /// Returns whether the system is running on Windows
    public static boolean isWindows() {
        return os().contains("WIN");
    }
    /// Returns whether the system is running on Mac
    public static boolean isMac() {
        return os().contains("MAC");
    }
    /// Returns whether the system is running on Linux
    public static boolean isLinux() {
        return os().contains("LINUX");
    }
}
