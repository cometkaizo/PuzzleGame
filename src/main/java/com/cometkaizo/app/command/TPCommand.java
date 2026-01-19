package com.cometkaizo.app.command;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.command.arguments.DoubleArgument;
import com.cometkaizo.command.nodes.ArgumentCommandNodeBuilder;
import com.cometkaizo.command.nodes.Command;
import com.cometkaizo.game.Game;
import com.cometkaizo.world.Vector;

import java.util.List;

public class TPCommand extends Command {

    private final GameApp app;

    public TPCommand(GameApp app) {
        this.app = app;
        rootNode.then(new ArgumentCommandNodeBuilder(new DoubleArgument("x")))
                .then(new ArgumentCommandNodeBuilder(new DoubleArgument("y")))
                .executes(this::tp);
    }

    private void tp() {
        Game game = app.getGame();
        double x = (double) parsedArgs.get("x");
        double y = (double) parsedArgs.get("y");

        game.getPlayer().setPosition(Vector.immutable(x, y));
    }


    @Override
    public List<String> getNames() {
        return List.of("tp");
    }
}
