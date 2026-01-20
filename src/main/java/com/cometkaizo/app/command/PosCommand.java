package com.cometkaizo.app.command;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.command.nodes.Command;
import com.cometkaizo.game.Game;

import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Specification of a command that gets the position of the player
 */
public class PosCommand extends Command {

    private final GameApp app;

    /// Creates a new position command
    public PosCommand(GameApp app) {
        this.app = app;
        rootNode.executes(this::printPlayerPos);
    }

    /// prints the player position
    private void printPlayerPos() {
        Game game = app.getGame();
        if (game != null) log(game.getPlayer().getPosition().toString());
    }

    /// gets the list of names for this command
    @Override
    public List<String> getNames() {
        return List.of("pos");
    }
}
