package com.cometkaizo.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-22
 * Description: Useful string-related methods
 */
public class StringUtils {

    /// Handles line-breaking by restricting a possibly long, possibly multi-line string into
    /// multiple lines that are less than the given width
    public static List<String> createLines(String text, FontMetrics font, int width) {
        var lines = new ArrayList<String>();
        for (String existingLine : text.split("\n")) {
            lines.addAll(createLinesNoNewline(existingLine, font, width));
        }
        return lines;
    }

    /// Handles line breaking by restricting a single-line string into multiple lines that are less than the given width
    private static List<String> createLinesNoNewline(String text, FontMetrics font, int width) {
        String[] words = text.split(" ");

        List<String> lines = new ArrayList<>();
        lines.add("");

        for (String word : words) {
            String line = lines.getLast(); // get the last line
            // try to extend the last line
            String extendedLine = line.isEmpty() && word.isEmpty() ? " " : // both line and word are empty, append a space to allow leading whitespace
                    line.isEmpty() ? word : // if the line is empty, add the word
                            line + " " + word; // otherwise, add the word with a space
            if (font.stringWidth(extendedLine) > width) { // if the line becomes too long, move word to next line
                lines.add(word);
            } else { // otherwise, extend the line
                lines.set(lines.size() - 1, extendedLine);
            }
        }

        return lines;
    }

}
