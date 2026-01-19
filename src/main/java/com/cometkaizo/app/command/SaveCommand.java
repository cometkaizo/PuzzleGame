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

    public SaveCommand(GameApp app) {
        this.app = app;
        rootNode.executes(this::save);
    }

    private void save() {
        Main.log("Saving game...");

        boolean success = app.saveGame();

        if (success) Main.log("Successfully saved world");
        else Main.log("Failed to save world");
    }

    @Override
    public List<String> getNames() {
        return List.of("game");
    }
}
