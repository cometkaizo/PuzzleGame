package com.cometkaizo.app.command;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.command.arguments.StringArgument;
import com.cometkaizo.command.nodes.ArgumentCommandNodeBuilder;
import com.cometkaizo.command.nodes.Command;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.item.LightHeartItem;

import java.util.List;

public class PosCommand extends Command {

    private final GameApp app;

    public PosCommand(GameApp app) {
        this.app = app;
        rootNode.executes(this::printPlayerPos);
    }

    private void printPlayerPos() {
        Game game = app.getGame();
        if (game != null) log(game.getPlayer().getPosition().toString());
    }

    @Override
    public List<String> getNames() {
        return List.of("pos");
    }
}
