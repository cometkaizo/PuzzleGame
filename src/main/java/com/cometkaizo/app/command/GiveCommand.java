package com.cometkaizo.app.command;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.command.arguments.StringArgument;
import com.cometkaizo.command.nodes.ArgumentCommandNodeBuilder;
import com.cometkaizo.command.nodes.Command;
import com.cometkaizo.game.item.FeatherItem;
import com.cometkaizo.game.item.HeavyHeartItem;
import com.cometkaizo.game.item.LightHeartItem;
import com.cometkaizo.game.item.MachinePieceItem;

import java.util.List;

public class GiveCommand extends Command {

    private final GameApp app;

    public GiveCommand(GameApp app) {
        this.app = app;
        rootNode.split(
                new ArgumentCommandNodeBuilder(new StringArgument("item_name")).executes(this::give)
        );
    }

    private void give() {
        String name = (String) parsedArgs.get("item_name");
        switch (name) {
            case "feather" -> app.getGame().getInventory().add(new FeatherItem());
            case "heart1" -> app.getGame().getInventory().add(new HeavyHeartItem());
            case "heart2" -> app.getGame().getInventory().add(new LightHeartItem());
            case "machine" -> app.getGame().getInventory().add(new MachinePieceItem());
            case null, default -> log("Invalid argument");
        }
    }

    @Override
    public List<String> getNames() {
        return List.of("give");
    }
}
