package com.cometkaizo.world;

import com.cometkaizo.util.MathUtils;

import java.util.Arrays;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Arguments read in from Google Sheets, to be parsed by a block or entity
 */
public class Args {
    public static final Args EMPTY = new Args("");
    public String id;
    public String[] args;
    public int index;
    /// Creates a new Args instance by parsing the given string
    public Args(String s) {
        var parts = removeComments(s).split(";");
        args = Arrays.copyOfRange(parts, 1, parts.length);
        id = parts[0];
    }
    /// Creates a new Args instance with the given id and args
    public Args(String id, String... args) {
        this.id = id;
        this.args = args;
    }

    /**
     * Removes "comments" from the given string.
     * A "comment" in the Google sheet is defined as any text enclosed with round brackets.
     * Comments allow annotation of the blocks in the Google sheet.
     * (Or we could use real Google sheet comments too, but those a more annoying)
     */
    public static String removeComments(String str) {
        return str.replaceAll(" ?\\(.*?\\)", ""); // regex to check for comments and remove them
    }

    /// Gets the next argument and advances the iterator
    public String next() {
        if (index == args.length) return "";
        return args[index ++];
    }
    /// Gets the next argument and advances the iterator
    public String next(String defaultVal) {
        var next = next();
        if (next.isBlank()) return defaultVal;
        else return next;
    }
    /// Gets the next argument as a direction and advances the iterator
    public Direction nextDirection(Direction def) {
        var next = next();
        if (next.isBlank()) return def;
        else return Direction.ofLetter(next);
    }
    /// Gets the next argument as an int and advances the iterator
    public int nextInt(int def) {
        var next = next();
        if (next.isBlank()) return def;
        else return Integer.parseInt(next);
    }
    /// Gets the next argument as a Google sheets column and advances the iterator
    public int nextSheetCol(int def) {
        var next = next();
        if (next.isBlank()) return def;
        else return MathUtils.getSheetCol(next);
    }

    /// Resets this Arg's iterator
    public void reset() {
        index = 0;
    }

    /// Returns the id
    public String id() {
        return id;
    }

    /// Turns this Args instance into a string that can be read in again using the constructor
    @Override
    public String toString() {
        return id + ";" + String.join(";", args);
    }
}
