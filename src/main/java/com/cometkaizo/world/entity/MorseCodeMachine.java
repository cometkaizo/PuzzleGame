package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.MachinePieceItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.overlay.InventoryOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Morse code machine that blocks light in a morse code sequence
 */
public class MorseCodeMachine extends Interactable {
    private static final int TICKS_PER_DOT = 5; // the length of one morse code dot
    private String pattern; // the pattern, where X means on and _ means off
    private boolean working;
    /// Creates a new morse code machine
    public MorseCodeMachine(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        pattern = originalArgs.next(" ");
        working = originalGameState.morseCodeWorking.getOrDefault(name, false);
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.morseCodeWorking.put(name, working);
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        if (working) app.narrate("The machine is spinning and whirring, sometimes blocking out the light.", null);
        else {
            app.narrate("The machine is missing a piece...", new InventoryOverlay(app, item -> {
                if (item instanceof MachinePieceItem) {
                    working = true;
                    game.getInventory().remove(item);
                    app.narrate("You slot the machine part into the machine. It starts whirring, sometimes blocking out the light...", null);
                } else {
                    app.narrate("This can't be placed here.", null);
                    Assets.sound("wrong").play();
                }
            }));
        }
    }

    /// Returns whether this entity blocks light from passing through
    @Override
    public boolean blocksLight() {
        if (!working) return false;
        int dots = (int) ((game.tick / TICKS_PER_DOT) % pattern.length());
        return pattern.charAt(dots) == '_'; // _ means off (no light)
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "morse_code";
    }
    /// Gets the x translation to be applied to the texture, in unscaled texture pixels
    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    /// Gets the y translation to be applied to the texture, in unscaled texture pixels
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }
}
