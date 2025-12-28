package com.cometkaizo.world;

import com.cometkaizo.util.MathUtils;

import java.util.Arrays;

public class Args {
    public static final Args EMPTY = new Args("");
    public String id;
    public String[] args;
    public int index;
    public Args(String s) {
        var parts = removeComments(s).split(";");
        args = Arrays.copyOfRange(parts, 1, parts.length);
        id = parts[0];
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

    public String next() {
        if (index == args.length) return "";
        return args[index ++];
    }
    public String next(String defaultVal) {
        var next = next();
        if (next.isBlank()) return defaultVal;
        else return next;
    }
    public Direction nextDirection(Direction def) {
        var next = next();
        if (next.isBlank()) return def;
        else return Direction.ofLetter(next);
    }
    public int nextInt(int def) {
        var next = next();
        if (next.isBlank()) return def;
        else return Integer.parseInt(next);
    }
    public int nextSheetCol(int def) {
        var next = next();
        if (next.isBlank()) return def;
        else return MathUtils.getSheetCol(next);
    }

    public void reset() {
        index = 0;
    }

    public String id() {
        return id;
    }
}
