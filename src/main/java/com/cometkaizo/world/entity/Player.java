package com.cometkaizo.world.entity;

import com.cometkaizo.game.event.KeyPressedEvent;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.input.KeyBinding;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Dialogue;
import com.cometkaizo.util.MathUtils;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
import java.util.Objects;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Player entity which is controlled by the user
 */
public class Player extends MovableEntity {

    protected Vector.MutableDouble lastMotion = Vector.mutable(0D, 0D);
    protected double jumpAccel = 1, diagJumpAccel = jumpAccel * Math.cos(Math.toRadians(45));
    protected double maxJumpSpeed = 1, maxDiagJumpSpeed = maxJumpSpeed * Math.cos(Math.toRadians(45));
    protected int jumpDirection = -1;
    protected double walkAccel = 0.3, diagWalkAccel = walkAccel * Math.cos(Math.toRadians(45));
    protected double maxWalkSpeed = 0.2, maxDiagWalkSpeed = maxWalkSpeed * Math.cos(Math.toRadians(45));
    protected double friction = 0.1;
    protected int interactDuration = 5, dialogueInteractDuration = 2, jumpBufferDuration = 3;
    // jumpTime = -2 means jump not reset, -1 means jump reset
    protected int jumpTime = -1, walkTime = -1, interactTime = -1, dialogueInteractTime = -1, jumpBufferTime = -1;
    protected int prevWalkTime = -1;
    protected boolean facingRight = true;
    protected Collectible displayedCollectible;

    public Player(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        this.boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(0.6D, 0.6D));
        eventBus.register(KeyPressedEvent.class, this::onKeyPressed);
    }

    @Override
    public void reset() {
        super.reset();
        game.getState().playerPos = position;
    }

    @Override
    public void tick() {
        tickMotion();
        tickTrigger();
        tickCheckpoint();
        super.tick();

        tickJumpBuffer();
        tickJumpTime();
        tickInteractTime();
        tickDialogueInteractTime();
    }

    private void tickJumpBuffer() {
        if (canJump() && jumpBufferTime > -1) {
            jump();
            jumpBufferTime = -1;
        }
        if (jumpBufferTime > -1) jumpBufferTime --;
    }

    private void tickCheckpoint() {
        var prevCheckpoint = originalPosition;
        for (var checkpoint : room.checkpoints) {
            if (checkpoint.activationArea().contains(position) && canActivateCheckpoint())
                originalPosition = checkpoint.pos();
        }
        if (!Objects.equals(prevCheckpoint, originalPosition)) {
            // collect checkpoint
//            Assets.sound("notify").play();
        }
    }
    private boolean canActivateCheckpoint() {
        return true;
    }

    private void tickTrigger() {
        for (var trigger : room.triggers) {
            if (trigger.activationArea().contains(position)) {
                trigger.activate(this);
            }
        }
    }
    private void tickJumpTime() {
        if (jumpTime >= 2 || collidedHorizontally && collidedVertically) {
            jumpTime = -2;
            jumpDirection = -1;
        }
        if (jumpTime == -2 && isAboveGround()) jumpTime = -1;
        if (jumpTime >= 0) jumpTime ++;
    }
    private void tickInteractTime() {
        if (game.getDialogue() != null) interactTime = interactDuration;
        else if (interactTime >= 0) interactTime --;
    }
    private void tickDialogueInteractTime() {
        if (dialogueInteractTime >= 0) dialogueInteractTime --;
    }

    private boolean isAboveGround() {
        return true;
        //        return room.ground.containsSolid(boundingBox, this);
    }

    private boolean isJumping() {
        return jumpTime > -1;
    }

    private boolean isJumpAvailable() {
        return jumpTime == -1;
    }

    private void tickMotion() {

        motion.x *= friction;
        motion.y *= friction;

        boolean right = InputBindings.RIGHT.get().isDown;
        boolean left = InputBindings.LEFT.get().isDown;
        boolean up = InputBindings.UP.get().isDown;
        boolean down = InputBindings.DOWN.get().isDown;
        boolean diagonal = (right || left) && (up || down);

        prevWalkTime = walkTime;
        if (!walkDisabled()) {
            if (right || left || up || down) walkTime = walkTime + 1;
            else {
                if (walkTime > 7) {
                    walkTime %= 7;
                    prevWalkTime = walkTime;
                }
                if (walkTime >= 0) walkTime--;
            }
        } else walkTime = 0;

        double accel, maxVelocity = Double.MAX_VALUE;

        if (!isJumping()) {
            if (walkDisabled()) return;

            accel = diagonal ? this.diagWalkAccel : this.walkAccel;
            maxVelocity = diagonal ? this.maxDiagWalkSpeed : this.maxWalkSpeed;

            double x = 0, y = 0;

            if (right) {
                x += accel;
            }
            if (left) {
                x -= accel;
            }
            if (up) {
                y += accel;
            }
            if (down) {
                y -= accel;
            }

            if (x != 0 || y != 0) lastMotion.set(x, y);

            motion.x += x;
            motion.y += y;
        } else {
            if (jumpTime == 1) {
                if (right) {
                    if (down) jumpDirection = 1;
                    else if (up) jumpDirection = 7;
                    else jumpDirection = 0;
                }
                else if (down) {
                    if (left) jumpDirection = 3;
                    else jumpDirection = 2;
                }
                else if (left) {
                    if (up) jumpDirection = 5;
                    else jumpDirection = 4;
                }
                else if (up) {
                    jumpDirection = 6;
                }
                else jumpDirection = facingRight ? 0 : 4; // if not holding any direction
            }
            if (jumpTime >= 1) {
                accel = diagonal ? this.diagJumpAccel : this.jumpAccel;
                maxVelocity = diagonal ? this.maxDiagJumpSpeed : this.maxJumpSpeed;

                switch (jumpDirection) {
                    case 0 -> motion.x += accel;
                    case 1 -> {
                        motion.x += accel;
                        motion.y -= accel;
                    }
                    case 2 -> motion.y -= accel;
                    case 3 -> {
                        motion.x -= accel;
                        motion.y -= accel;
                    }
                    case 4 -> motion.x -= accel;
                    case 5 -> {
                        motion.x -= accel;
                        motion.y += accel;
                    }
                    case 6 -> motion.y += accel;
                    case 7 -> {
                        motion.x += accel;
                        motion.y += accel;
                    }
                }
            }
        }

        motion.x = Math.min(Math.max(motion.x, -maxVelocity), maxVelocity);
        motion.y = Math.min(Math.max(motion.y, -maxVelocity), maxVelocity);

        if (!MathUtils.almostEquals(motion.x, 0)) facingRight = motion.x > 0;

        if (walkTime % 5 == 0) Assets.sound("step").play();
    }

    private boolean walkDisabled() {
        return displayingCollectible() || game.ended() || game.hasDialogue();
    }
    private boolean jumpDisabled() {
        return displayingCollectible() || game.ended() || game.hasDialogue();
    }

    @Override
    protected void tickBoundingBox() {
        // center the player's hitbox horizontally on the player's position
        double width = boundingBox.getWidth();
        boundingBox.position.x = position.x - width / 2;
        boundingBox.position.y = position.y;
    }

    private void onKeyPressed(KeyPressedEvent event) {
        KeyBinding input = event.input();
        if (!jumpDisabled() && input == InputBindings.JUMP.get()) {
            jump();
        }
        if (input == InputBindings.INTERACT.get()) {
            if (canConfirmCollectible()) {
                displayedCollectible = null;
            }
            else if (canInteractDialogue()) game.advanceDialogue();
        }
    }
    public boolean canConfirmCollectible() {
        return interactTime == -1 && displayingCollectible();
    }
    public boolean canInteractDialogue() {
        return dialogueInteractTime == -1 && !displayingCollectible();
    }
    public boolean canInteract() {
        return interactTime == -1 && !displayingCollectible();
    }

    public void jump() {
        if (!canJump()) {
            jumpBufferTime = jumpBufferDuration;
            return;
        }

        jumpTime = 0;

//        Assets.sound("jump").play();
    }

    public boolean canJump() {
        return isJumpAvailable()/* && !isFloating()*/;
    }

    /// Called when the player interacts with an object. Paired with canInteract() 
    /// to ensure that only one object is interacted with per key click
    public void onInteract() {
        interactTime = interactDuration;
        dialogueInteractTime = dialogueInteractDuration;
    }

    @Override
    protected boolean canBlip() {
        return isJumping();
    }

    public boolean displayingCollectible() {
        return displayedCollectible != null;
    }

    @Override
    protected String getTexturePath() {
        if (displayingCollectible()) return "player/paws_up";
        if (jumpTime >= 0 && jumpTime < 5) return "player/jump";
        return "player/normal";
    }

    @Override
    public void render(Canvas canvas) {
        var g = canvas.getGraphics();
        var oT = g.getTransform();
        var oC = g.getComposite();

        int screenX = canvas.toScreenX(canvas.lerp(getOldX(), getX()));
        int screenY = canvas.toScreenY(canvas.lerp(getOldY(), getY()) + 0.3);

        double angle = 0;
        double translateX = 0, translateY = 0;
        double alpha = 1;
        if (walkTime >= 0) {
            angle = Math.sin((canvas.lerp(prevWalkTime, walkTime))) * 0.2;
            translateY = -Math.abs(Math.sin(canvas.lerp(prevWalkTime, walkTime)) * 25);
        }

        {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            g.translate(translateX, translateY);
            g.translate(screenX, screenY);
            if (angle != 0)
                g.rotate(angle);
            if (!facingRight && !displayingCollectible()) g.scale(-1, 1);
            g.translate(-screenX, -screenY);
        }

        super.render(canvas);

        g.setTransform(oT);
        g.setComposite(oC);

        canvas.renderDebugBoundingBox(boundingBox, Color.WHITE);
    }

    @Override
    protected double getTextureDeltaXFactor() {
        return -0.5;
    }

    public static Dialogue dialogue(String msg, String textureVariation, Dialogue next) {
        return new Dialogue(msg, "gui/player/" + textureVariation, next);
    }

    public Vector.Double getLastMotion() {
        return lastMotion;
    }
}
