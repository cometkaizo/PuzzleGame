package com.cometkaizo.app.command;

import com.cometkaizo.Main;
import com.cometkaizo.app.GameApp;
import com.cometkaizo.command.nodes.Command;

import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Specification of a command that saves the game
 */
public class SaveCommand extends Command {

    private final GameApp app;

    /// Creates a new save command
    public SaveCommand(GameApp app) {
        this.app = app;
        rootNode.executes(this::save);
    }

    /// save the game
    private void save() {
        Main.log("Saving game...");

        boolean success = app.saveGame();

        if (success) Main.log("Successfully saved world");
        else Main.log("Failed to save world");
    }

    /// gets the list of names for this command
    @Override
    public List<String> getNames() {
        return List.of("game");
    }
}
