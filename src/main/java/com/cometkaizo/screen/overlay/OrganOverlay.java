package com.cometkaizo.screen.overlay;

import com.cometkaizo.Main;
import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.ImageClickable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class OrganOverlay extends Overlay {
    public static final int KEY_PIXEL_WIDTH = 7,
            WIDTH_IN_OCTAVES = 5,
            KEYS_PER_OCTAVE = 12,
            WIDTH_IN_KEYS = WIDTH_IN_OCTAVES * KEYS_PER_OCTAVE,
            HEIGHT_IN_KEYS = 3;
    public static final String[] CORRECT_COMBO = {
            "l","u","n","g","s",   "l","i","v","e","r",   "s","t","o","m","a","c","h"
    };

    private List<Key>[] whiteKeys = new List[HEIGHT_IN_KEYS];
    private List<Key>[] blackKeys = new List[HEIGHT_IN_KEYS];
    private Key lastHoveredWhiteKey, lastHoveredBlackKey;
    // todo: make the loose key not play audio, or play it weirdly
    private Queue<String> lastPressedKeys = new ArrayDeque<>();

    public boolean keyFallenOut;
    private final Runnable solveAction;

    public OrganOverlay(GameApp app, boolean keyFallenOut, Runnable solveAction) {
        super(app);
        this.keyFallenOut = keyFallenOut;
        this.solveAction = solveAction;

        for (int r = 0; r < HEIGHT_IN_KEYS; r ++) {
            whiteKeys[r] = new ArrayList<>();
            blackKeys[r] = new ArrayList<>();
            int c = 0;
            for (int octave = 0; octave < WIDTH_IN_OCTAVES; octave ++) {
                // init each octave
                addNewWhiteKey(r, c++, octave, 0);
                addNewBlackKey(r, c++, octave, 4);
                addNewWhiteKey(r, c++, octave, 7);
                addNewBlackKey(r, c++, octave, 12);
                addNewWhiteKey(r, c++, octave, 14);
                addNewWhiteKey(r, c++, octave, 21);
                addNewBlackKey(r, c++, octave, 25);
                addNewWhiteKey(r, c++, octave, 28);
                addNewBlackKey(r, c++, octave, 32);
                addNewWhiteKey(r, c++, octave, 35);
                addNewBlackKey(r, c++, octave, 40);
                addNewWhiteKey(r, c++, octave, 42);
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/organ/full"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        lastHoveredWhiteKey = renderKeys(whiteKeys, canvas);
        lastHoveredBlackKey = renderKeys(blackKeys, canvas);
    }

    /// Renders the list of the keys and returns the hovered key, if any
    private Key renderKeys(List<Key>[] keys, Canvas canvas) {
        Key hovered = null;
        // render all non-hovered keys
        for (var row : keys) for (var key : row) {
            if (!key.isHovered()) key.render(canvas);
            else hovered = key; // find hovered key
        }
        // render the hovered key over other keys to show hover border
        if (hovered != null) hovered.render(canvas);
        return hovered; // return hovered key
    }



    public void addNewWhiteKey(int r, int c, int octave, int x) {
        var key = new Key(true, r*WIDTH_IN_KEYS + c, w -> w/2-122 + octave*KEY_PIXEL_WIDTH*7 + x, h -> h/2-21 + r*34);
        whiteKeys[r].add(key);
    }
    public void addNewBlackKey(int r, int c, int octave, int x) {
        var key = new Key(false, r*WIDTH_IN_KEYS + c, w -> w/2-122 + octave*KEY_PIXEL_WIDTH*7 + x, h -> h/2-21 + r*34);
        blackKeys[r].add(key);
    }
    public class Key extends ImageClickable {
        private static final Font FONT = Assets.font("BoldPixels", 24);
        private static final Color FONT_COLOR = new Color(237, 113, 113);
        public final boolean white;
        private boolean wasPressedLastTick;
        public final int key;
        public final String writing;

        public Key(boolean white, int key, IntUnaryOperator x, IntUnaryOperator y) {
            super(OrganOverlay.this.app, () -> {}, x, y, _ -> white ? 7 : 4, _ -> white ? 32 : 19,
                    null, -2, 0);
            this.white = white;
            this.texturePath = () -> "gui/organ/key" + (isPressed() ? "_pressed/" : "/") + (white ? "white" : "black");
            this.key = key;
            this.writing = switch (key) {
                case 96 -> "a";
                case 35 -> "b";
                case 122 -> "c";
                case 165 -> "d";
                case 136 -> "e";
                case 115 -> "f";
                case 17 -> "g";
                case 105 -> "h";
                case 81 -> "i";
                case 9 -> "j";
                case 62 -> "k";
                case 158 -> "l";
                case 175 -> "m";
                case 143 -> "n";
                case 21 -> "o";
                case 41 -> "p";
                case 36 -> "q";
                case 28 -> "r";
                case 53 -> "s";
                case 98 -> "t";
                case 125 -> "u";
                case 83 -> "v";
                case 50 -> "w";
                case 60 -> "x";
                case 43 -> "y";
                case 11 -> "z";
                default -> "";
            };
        }

        @Override
        public void render(Canvas canvas) {
            if (hasFallenOut()) return; // if this key has fallen out, don't render it
            super.render(canvas);
            canvas.renderString(writing, FONT, FONT_COLOR, lastX + canvas.scale(3), lastY + canvas.scale(26 + (isPressed() ? 3 : 0)), true, false);
        }
        private boolean hasFallenOut() {
            return key == 81 && keyFallenOut;
        }

        private boolean isPressed() {
            return app.isMouseDown() && isHovered();
        }
        // override isHovered to account for other possibly hovered keys and only allow one to be hovered
        @Override
        public boolean isHovered() {
            if (hasFallenOut()) return false;
            if (!white) return super.isHovered() && (lastHoveredBlackKey == null || lastHoveredBlackKey == this);
            else return super.isHovered() && lastHoveredBlackKey == null && (lastHoveredWhiteKey == null || lastHoveredWhiteKey == this);
        }

        @Override
        public void tick() {
            super.tick();
            tickSound();
        }

        private void tickSound() {
            if (hasFallenOut()) return;
            // todo: continue sound if hold
            if (isPressed() && !wasPressedLastTick) {
                onFirstPress();
            }
            wasPressedLastTick = isPressed();
        }

        private void onFirstPress() {
            Main.log("play organ sound");
            pressKey(this);
        }
    }

    public void pressKey(Key key) {
        if (lastPressedKeys.size() == CORRECT_COMBO.length) lastPressedKeys.poll();
        lastPressedKeys.add(key.writing);
        if (!keyFallenOut && Arrays.equals(lastPressedKeys.toArray(), CORRECT_COMBO)) {
            solveAction.run();
        }
    }


    @Override
    public void tick() {
        super.tick();
        for (var row : whiteKeys) for (var key : row) key.tick();
        for (var row : blackKeys) for (var key : row) key.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var row : whiteKeys) for (var key : row) key.onClick(click);
        for (var row : blackKeys) for (var key : row) key.onClick(click);
    }
}
