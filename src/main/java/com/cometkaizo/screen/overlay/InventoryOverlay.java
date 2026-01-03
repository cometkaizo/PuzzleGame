package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.Item;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Math.ceilDiv;
import static java.lang.Math.max;

public class InventoryOverlay extends Overlay {
    private static final int WIDTH_IN_ITEMS = 3;
    private final List<Clickable> items = new ArrayList<>();
    private final Consumer<Item> onClick;
    private final int rowCount;

    public InventoryOverlay(GameApp app) {
        this(app, _ -> {});
    }
    public InventoryOverlay(GameApp app, Consumer<Item> onClick) {
        this(app, onClick, null);
    }
    public InventoryOverlay(GameApp app, Overlay prev) {
        this(app, _ -> {}, prev);
    }
    public InventoryOverlay(GameApp app, Consumer<Item> onClick, Overlay next) {
        super(app, next);
        this.onClick = onClick;
        var inventory = app.getGame().getInventory();
        rowCount = max(1, ceilDiv(inventory.size(), WIDTH_IN_ITEMS));

        int r = 0, c = 0;
        for (int itemId = 0; itemId < inventory.size(); itemId ++) {
            var item = inventory.get(itemId);
            items.add(new Slot(r, c, item));

            c ++;
            if (c == WIDTH_IN_ITEMS) {
                c = 0;
                r ++;
            }
        }

        // complete the row with empty slots
        for (; c < WIDTH_IN_ITEMS; c ++) {
            items.add(new Slot(r, c, null));
        }
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderString("Items in Inventory", Assets.font("BoldPixels", 24), Color.WHITE,
                canvas.halfWidth() + canvas.scale(xOffset() + 2), canvas.halfHeight() + canvas.scale(yOffset()) - 30,
                false, false);
        for (var item : items) item.render(canvas);
    }

    private int xOffset() {
        return -(Slot.SIZE * WIDTH_IN_ITEMS + Slot.PADDING * (WIDTH_IN_ITEMS - 1)) / 2;
    }
    private int yOffset() {
        return -(Slot.SIZE * rowCount + Slot.PADDING * (rowCount - 1)) / 2;
    }

    class Slot extends ImageClickable {
        public static final int SIZE = 38;
        public static final int PADDING = 4;

        public Slot(int r, int c, Item item) {
            super(InventoryOverlay.this.app, item == null ? () -> {} : () -> onClick.accept(item),
                    w -> w/2 + xOffset() + c * (SIZE + PADDING), h -> h/2 + yOffset() + r * (SIZE + PADDING),
                    _ -> SIZE, _ -> SIZE, item == null ? () -> null : item::getTexturePath, 3, 3);
        }

        @Override
        public void render(Canvas canvas) {
            canvas.renderImage(Assets.texture("gui/item/slot"), lastX - 2, lastY - 2);
            super.render(canvas);
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (var item : items) item.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var item : items) item.onClick(click);
    }
}
