package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.*;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ItemClickable;
import com.cometkaizo.world.Light;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.entity.RaSculpture;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the anubis sculpture
 */
public class AnubisOverlay extends Overlay {
    public static final int FEATHER_WEIGHT = 50;
    public static final int SCALE_ARM_RADIUS = 70;
    private WeighableItem weighed;
    private Clickable weighedClickable;

    public AnubisOverlay(GameApp app) {
        super(app);
        weighedClickable = new ItemClickable(app, this::clickWeigh, w -> w/2 - weighX(), h -> h/2 - weighY(), () -> weighed, () -> "gui/sculpture/anubis/empty_slot");
    }

    private void clickWeigh() {
        var inventory = app.getGame().getInventory();
        if (weighed != null) {
            inventory.add(weighed);
            weighed = null;
        } else {
            app.setOverlay(new InventoryOverlay(app, item -> {
                if (item instanceof WeighableItem weighable) {
                    weigh(weighable);
                    app.narrate("The scale tips...", AnubisOverlay.this);
                } else {
                    app.narrate("This item cannot be weighed", AnubisOverlay.this);
                }
            }, this));
        }
    }

    private void weigh(WeighableItem weighable) {
        weighed = weighable;
        if (!(app.getGame().room.getBlockOrEntity("statue of ra") instanceof RaSculpture ra)) return;

        if (getWeighResult() == WeighResult.OBJECT_HEAVIER) {
            ra.setDirection(Light.Direction.W);
        } else if (getWeighResult() == WeighResult.OBJECT_LIGHTER) {
            ra.setDirection(Light.Direction.S);
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
            case NO_OBJECT -> Math.toRadians(-20);
            case OBJECT_EQUAL -> Math.toRadians(0);
            case OBJECT_HEAVIER -> Math.toRadians(50);
            case OBJECT_LIGHTER -> Math.toRadians(-50);
        };
    }
    private int weighX() {
        return (int) (Math.cos(scaleAngle()) * SCALE_ARM_RADIUS);
    }
    private int weighY() {
        return (int) (Math.sin(scaleAngle()) * SCALE_ARM_RADIUS);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/sculpture/anubis/regular"));
        canvas.renderImage(Assets.texture("gui/sculpture/anubis/feather"), canvas.halfWidth() + canvas.scale(weighX()), canvas.halfHeight() + canvas.scale(weighY()));

        weighedClickable.render(canvas);
    }

    enum WeighResult {
        OBJECT_LIGHTER, OBJECT_HEAVIER, OBJECT_EQUAL, NO_OBJECT
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
}
