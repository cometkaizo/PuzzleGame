package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Tickable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-16
 * Description: Screen overlay for the brain of the thinker
 */
public class BrainOverlay extends Overlay {
    private final Text text = new Text("How to achieve 100% neuron activation?", Assets.font(40), Color.WHITE, w -> w/2, h -> h/2 + 77, 200, true, false);
    private final Neuron[][] neurons = new Neuron[4][6];
    private final List<Button> buttons = new ArrayList<>();
    private final Runnable solveAction;

    /// Creates a new overlay
    public BrainOverlay(GameApp app, Runnable solveAction, Overlay prev) {
        super(app, prev);
        this.solveAction = solveAction;
        for (int r = 0; r < neurons.length; r ++) {
            for (int c = 0; c < neurons[r].length; c ++) {
                if (hasNeuronAt(r, c)) neurons[r][c] = new Neuron(r, c);
                if (isValidButtonPos(r, c, true)) buttons.add(new Button(r, c, true));
                if (isValidButtonPos(r, c, false)) buttons.add(new Button(r, c, false));
            }
        }
    }
    /// Returns whether there is a neuron at the given coordinates
    private boolean hasNeuronAt(int r, int c) {
        if (r < 0 || r >= neurons.length || c < 0 || c >= neurons[0].length) return false;
        return !((r == 0 && c == 0) || (r == 1 && c == 0) || (r == 3 && c == 0) || (r == 0 && c == 5) || (r == 3 && c == 5));
    }
    /// Returns whether there can be a button placed at the given coordinates
    public boolean isValidButtonPos(int r, int c, boolean otherTwoNeuronsAbove) {
        return hasNeuronAt(r, c) &&
                hasNeuronAt(r + (otherTwoNeuronsAbove ? -1 : 1), c + (r % 2 == 1 ? -1 : 0)) &&
                hasNeuronAt(r + (otherTwoNeuronsAbove ? -1 : 1), c + (r % 2 == 1 ? 0 : 1));
    }
    /// Returns the neuron at the given coordinates starts active
    private boolean neuronStartsOnAt(int r, int c) {
        return switch (r) {
            case 0 -> switch (c) {case 1, 3 -> true; default -> false;};
            case 1 -> switch (c) {case 2, 5 -> true; default -> false;};
            case 2 -> switch (c) {case 0, 1, 3, 5 -> true; default -> false;};
            case 3 -> switch (c) {case 1, 3 -> true; default -> false;};
            default -> false;
        };
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/sculpture/thinker/brain_closeup"));
        for (var button : buttons) button.render(canvas);
        for (var row : neurons)
            for (var neuron : row)
                if (neuron != null) neuron.render(canvas);

        text.render(canvas);
    }

    /// Ticks this screen overlay
    @Override
    public void tick() {
        super.tick();
        for (var row : neurons)
            for (var neuron : row)
                if (neuron != null) neuron.tick();
        for (var button : buttons) button.tick();
    }

    /// Called when the mouse is clicked
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var button : buttons) button.onClick(click);
    }

    /// Represents a single neuron in the brain
    public class Neuron implements Renderable, Tickable {
        public boolean on;
        public boolean hovered;
        public final int r, c;

        /// Creates a neuron at the given coordinates
        public Neuron(int r, int c) {
            this.r = r;
            this.c = c;
            on = neuronStartsOnAt(r, c);
        }

        /// Renders this neuron to the screen
        @Override
        public void render(Canvas canvas) {
            int x = canvas.scale(canvas.halfPixelWidth() + getDX(r, c));
            int y = canvas.scale(canvas.halfPixelHeight() + getDY(r, c));
            var texture = isOutlined() ? Assets.textureOutlined(getTexturePath()) : Assets.texture(getTexturePath());
            canvas.renderImage(texture, x, y);
            canvas.renderDebugString("(" + r + ", " + c + ")", Color.YELLOW, x + canvas.scale(14), y + canvas.scale(14));
        }

        /// Gets the y position of the neuron at the given coords relative to the center of the screen
        private static int getDY(int r, int c) {
            return - 68 + r * 28;
        }

        /// Gets the x position of the neuron at the given coords relative to the center of the screen
        private static int getDX(int r, int c) {
            return - 88 + c * 28 + (r % 2 == 1 ? -14 : 0);
        }

        /// Gets the texture path for this neuron
        private String getTexturePath() {
            return "gui/sculpture/thinker/neuron" + (on ? "_on" : "_off");
        }
        /// Returns whether this neuron is outlined
        private boolean isOutlined() {
            return hovered;
        }

        /// toggles this neuron from on/off to off/on
        public void toggle() {
            on = !on;
        }

        /// Resets this neuron to not-hovered every tick
        @Override
        public void tick() {
            hovered = false;
        }
    }

    /// A button to turn toggle 3 neurons at a time
    public class Button extends Clickable {
        private final int r, c;
        private final boolean otherTwoNeuronsAbove;

        /// Creates a new button
        public Button(int r, int c, boolean otherTwoNeuronsAbove) {
            super(BrainOverlay.this.app, () -> {}, w -> w/2 + Neuron.getDX(r, c) + 11, h -> h/2 + Neuron.getDY(r, c) + (otherTwoNeuronsAbove ? -14 : 14), _ -> 13, _ -> 28);
            this.action = this::toggleSurroundingNeurons;
            this.r = r;
            this.c = c;
            this.otherTwoNeuronsAbove = otherTwoNeuronsAbove;
        }

        /// Gets the neurons turned on/off by this button
        private Neuron[] getNeurons() {
            return new Neuron[] {
                    neurons[r][c],
                    neurons[r + (otherTwoNeuronsAbove ? -1 : 1)][c + (r % 2 == 1 ? -1 : 0)],
                    neurons[r + (otherTwoNeuronsAbove ? -1 : 1)][c + (r % 2 == 1 ? 0 : 1)]
            };
        }

        /// Toggles the affected neurons
        private boolean toggleSurroundingNeurons() {
            for (var neuron : getNeurons()) neuron.toggle();
            checkIfSolved();
            return true;
        }

        /// Updates the hover status of the affected neurons
        @Override
        public void tick() {
            super.tick();
            if (!isHovered()) return;
            for (var neuron : getNeurons()) neuron.hovered = true;
        }
    }

    /// Checks if the brain is solved
    private void checkIfSolved() {
        if (solved()) {
            solveAction.run();
        }
    }

    /// Returns whether all the neurons are solved
    private boolean solved() {
        for (var row : neurons)
            for (var neuron : row)
                if (neuron != null && !neuron.on) return false;
        return true;
    }
}
