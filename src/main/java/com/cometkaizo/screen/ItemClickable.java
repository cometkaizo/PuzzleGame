package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.item.Item;

import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public class ItemClickable extends ImageClickable {
    public ItemClickable(GameApp app, Runnable action, IntUnaryOperator x, IntUnaryOperator y, Supplier<Item> item, Supplier<String> emptyTexturePath) {
        this(app, () -> {
            action.run();
            return true;
        }, x, y, item, emptyTexturePath);
    }
    public ItemClickable(GameApp app, BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, Supplier<Item> item, Supplier<String> emptyTexturePath) {
        super(app, action, x, y, _ -> 16, _ -> 16, () -> {
            var i = item.get();
            return i == null ? emptyTexturePath.get() : i.getTexturePath();
        }, -2, -2);
    }
}
