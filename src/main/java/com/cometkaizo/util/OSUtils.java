package com.cometkaizo.util;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-21
 * Description: Useful OS-related methods
 */
public class OSUtils {

    public static String os() {
        return System.getProperty("os.name").toUpperCase();
    }

    public static boolean isWindows() {
        return os().contains("WIN");
    }
    public static boolean isMac() {
        return os().contains("MAC");
    }
    public static boolean isLinux() {
        return os().contains("LINUX");
    }
}
