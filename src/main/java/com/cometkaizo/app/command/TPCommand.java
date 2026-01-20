package com.cometkaizo.app.command;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.command.arguments.DoubleArgument;
import com.cometkaizo.command.nodes.ArgumentCommandNodeBuilder;
import com.cometkaizo.command.nodes.Command;
import com.cometkaizo.game.Game;
import com.cometkaizo.world.Vector;

import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Specification of a command that teleports the player to a given location
 */
public class TPCommand extends Command {

    private final GameApp app;

    /// Creates a new teleport command
    public TPCommand(GameApp app) {
        this.app = app;
        rootNode.then(new ArgumentCommandNodeBuilder(new DoubleArgument("x")))
                .then(new ArgumentCommandNodeBuilder(new DoubleArgument("y")))
                .executes(this::tp);
    }

    /// teleports the player to the given coords
    private void tp() {
        Game game = app.getGame();
        double x = (Double) parsedArgs.get("x");
        double y = (Double) parsedArgs.get("y");

        game.getPlayer().setPosition(Vector.immutable(x, y));
    }


    /// gets the list of names for this command
    @Override
    public List<String> getNames() {
        return List.of("tp");
    }
}
