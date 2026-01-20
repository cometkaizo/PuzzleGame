package com.cometkaizo.app.command;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.command.arguments.StringArgument;
import com.cometkaizo.command.nodes.ArgumentCommandNodeBuilder;
import com.cometkaizo.command.nodes.Command;
import com.cometkaizo.game.item.*;

import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-15
 * Description: Specification of a command that gives the player an item
 */
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
            case "notes" -> {
                app.getGame().getInventory().add(new NoteItem(0));
                app.getGame().getInventory().add(new NoteItem(1));
                app.getGame().getInventory().add(new NoteItem(2));
                app.getGame().getInventory().add(new NoteItem(3));
            }
            case null, default -> log("Invalid argument");
        }
    }

    @Override
    public List<String> getNames() {
        return List.of("give");
    }
}
