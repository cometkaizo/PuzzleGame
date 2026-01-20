package com.cometkaizo.util;

import com.cometkaizo.Main;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-21
 * Description: Useful file-related methods
 */
public class FileUtils {

    /// Prompts the user for a file path
    public static File promptDir(File defaultPath, Component parent) {
        var chooser = new JFileChooser();
        chooser.setCurrentDirectory(defaultPath);
        chooser.setDialogTitle("");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else return null;
    }

    /// Gets the user home file
    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    /// Gets the user's appdata directory
    public static File getAppdataDir() {
        String userHomeDir = System.getProperty("user.home", ".");
        if (OSUtils.isWindows() && System.getenv("APPDATA") != null)
            return new File(System.getenv("APPDATA"));
        if (OSUtils.isMac())
            return new File(new File(userHomeDir, "Library"), "Application Support");
        return new File(userHomeDir);
    }

    /// gets the desktop directory
    public static File getDesktopDir() {
        return FileSystemView.getFileSystemView().getHomeDirectory();
    }

    /// Gets the location of this program
    public static File thisProgramLocation() {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /// Runs the given file
    public static void run(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    /// Runs the given batch file
    public static Process runBat(File file) throws IOException {
        return Runtime.getRuntime().exec("cmd /c start \"\" \"" + file.getAbsolutePath() + "\"");
    }

    /// Returns whether the given file is not null and exists
    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    /// Reads the string from the given file
    public static String readStr(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
