package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.WeighableItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;
import com.cometkaizo.world.Direction;
import com.cometkaizo.world.Light;
import com.cometkaizo.world.entity.RaSculpture;

import java.util.function.BiConsumer;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the anubis sculpture
 */
public class AnubisOverlay extends Overlay {
    public static final int FEATHER_WEIGHT = 50;
    public static final int SCALE_ARM_RADIUS = 37;
    private WeighableItem weighed;
    private Clickable weighedClickable;
    private final BiConsumer<WeighableItem, WeighResult> onWeigh;

    public AnubisOverlay(GameApp app, WeighableItem weighed, BiConsumer<WeighableItem, WeighResult> onWeigh) {
        super(app);
        weighedClickable = new ItemClickable();
        this.weighed = weighed;
        this.onWeigh = onWeigh;
    }

    private void clickWeigh() {
        var inventory = app.getGame().getInventory();
        if (weighed != null) {
            inventory.add(weighed);
            setWeighed(null);
        } else {
            app.setOverlay(new InventoryOverlay(app, item -> {
                if (item instanceof WeighableItem weighable) {
                    setWeighed(weighable);
                    app.narrate("The scale tips...", AnubisOverlay.this);
                } else {
                    app.narrate("This item cannot be weighed", AnubisOverlay.this);
                }
            }, this));
        }
    }

    private void setWeighed(WeighableItem weighable) {
        weighed = weighable;
        if (weighed != null) app.getGame().getInventory().remove(weighed);

        onWeigh.accept(weighed, getWeighResult());

        if (!(app.getGame().room.getBlockOrEntity("statue of ra") instanceof RaSculpture ra)) return;

        if (getWeighResult() == AnubisOverlay.WeighResult.OBJECT_HEAVIER) {
            ra.setDirection(Direction.UP);
            next = new NarrationOverlay(app, "The Statue of Ra turns to the left.");
        } else if (getWeighResult() == AnubisOverlay.WeighResult.OBJECT_LIGHTER) {
            ra.setDirection(Direction.DOWN);
            next = new NarrationOverlay(app, "The Statue of Ra turns to the bottom.");
        }
    }

    private WeighResult getWeighResult() {
        return weighed == null ? WeighResult.NO_OBJECT :
                weighed.weight() > FEATHER_WEIGHT ? WeighResult.OBJECT_HEAVIER :
                weighed.weight() < FEATHER_WEIGHT ? WeighResult.OBJECT_LIGHTER :
                WeighResult.OBJECT_EQUAL;
    }
    private double scaleAngle() {
        return switch (getWeighResult()) {
            case NO_OBJECT -> Math.toRadians(-8);
            case OBJECT_EQUAL -> Math.toRadians(0);
            case OBJECT_HEAVIER -> Math.toRadians(25);
            case OBJECT_LIGHTER -> Math.toRadians(-25);
        };
    }
    private int weighX(boolean reversed) {
        return -18 + (reversed ? -1 : 1) * (int) (Math.cos(scaleAngle()) * SCALE_ARM_RADIUS);
    }
    private int weighY(boolean reversed) {
        return 33 + (reversed ? 1 : -1) * (int) (Math.sin(scaleAngle()) * SCALE_ARM_RADIUS); // y reversed
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/sculpture/anubis/regular"));
        canvas.renderCenteredImage(Assets.texture("gui/sculpture/anubis/" + getWeighResult().textureName));
        canvas.renderImage(Assets.texture("gui/sculpture/anubis/feather"), canvas.halfWidth() + canvas.scale(weighX(false)), canvas.halfHeight() + canvas.scale(weighY(false)));

        weighedClickable.render(canvas);
    }

    public enum WeighResult {
        OBJECT_LIGHTER("3"), OBJECT_HEAVIER("2"), OBJECT_EQUAL("0"), NO_OBJECT("1");
        public final String textureName;
        WeighResult(String textureName) {
            this.textureName = textureName;
        }
    }

    @Override
    public void tick() {
        super.tick();
        weighedClickable.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        weighedClickable.onClick(click);
    }

    public class ItemClickable extends ImageClickable {
        public ItemClickable() {
            super(AnubisOverlay.this.app, AnubisOverlay.this::clickWeigh,
                    w -> w/2 + weighX(true), h -> h/2 + weighY(true),
                    _ -> 32, _ -> 32,
                    null, -2, -2);
            this.texturePath = () -> weighed == null ? "gui/sculpture/anubis/empty_slot" + (isHovered() ? "_highlighted" : "") : weighed.getTexturePath();
        }

        @Override
        protected boolean isOutlined() {
            return super.isOutlined() && weighed != null;
        }
    }
}
