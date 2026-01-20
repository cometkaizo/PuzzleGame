package com.cometkaizo.util;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-22
 * Description: Useful math-related methods
 */
public class MathUtils {
    /// Performs a linear interpolation between from and to using the percentage value a (usually from 0 to 1)
    public static double lerp(double a, double from, double to) {
        return from + (a * (to - from));
    }

    /// Returns whether two numbers are almost equal. This is useful for dealing with floating point inaccuracies.
    public static boolean almostEquals(double a, double b) {
        return Math.abs(b - a) < 1.0E-5D;
    }

    /**
     * Converts a Google sheets column letter string into the 0-indexed column index
     */
    public static int getSheetCol(String str) {
        str = str.toUpperCase();
        int col = 0;
        for (int i = 0; i < str.length(); i ++) {
            col *= 26;
            col += str.charAt(i) - 'A' + 1;
        }
        return col - 1; // make it 0-indexed
    }

    /**
     * Converts a 0-indexed column index into the Google sheets letter equivalent
     */
    public static String toSheetCol(int col) {
        String str = "";
        while (col >= 0) {
            char digit = (char) ('A' + (col % 26)); // convert current "digit" into a letter
            str = digit + str; // prepend it
            col /= 26;
            col --;
        }
        return str;
    }

    /// Returns whether a number is between two other numbers
    public static boolean isBetween(double n, double a, double b) {
        if (a < b) return (a - 1E-5) < n && n < (b + 1E-5);
        else return (b - 1E-5) < n && n < (a + 1E-5);
    }
}
