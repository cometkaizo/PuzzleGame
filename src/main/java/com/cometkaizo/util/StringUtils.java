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

    public static List<String> createLines(String text, FontMetrics font, int width) {
        var lines = new ArrayList<String>();
        for (String existingLine : text.split("\n")) {
            lines.addAll(createLinesNoNewline(existingLine, font, width));
        }
        return lines;
    }

    private static List<String> createLinesNoNewline(String text, FontMetrics font, int width) {
        String[] words = text.split(" ");

        List<String> lines = new ArrayList<>();
        lines.add("");

        for (String word : words) {
            String line = lines.getLast();
            String extendedLine = line.isEmpty() ? word : line + " " + word;
            if (font.stringWidth(extendedLine) > width) { // move word to next line
                lines.add(word);
            } else {
                lines.set(lines.size() - 1, extendedLine);
            }
        }

        return lines;
    }

}
