package com.cometkaizo.game;

import com.cometkaizo.Main;
import com.cometkaizo.app.GameApp;
import com.cometkaizo.app.GameDriver;
import com.cometkaizo.event.EventBus;
import com.cometkaizo.event.SimpleEventBus;
import com.cometkaizo.game.event.*;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.input.InputListener;
import com.cometkaizo.input.KeyBinding;
import com.cometkaizo.input.MouseButtonBinding;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.*;
import com.cometkaizo.world.entity.Collectible;
import com.cometkaizo.world.entity.Door;
import com.cometkaizo.world.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Game implements Tickable, Renderable, InputListener {
    public static final String GAME_STATE_FILENAME = "state.txt";
    private static final Font TIMER_FONT = Assets.font("BoldPixels").deriveFont(Font.PLAIN, 30);
    private static final Color TIMER_COLOR = new Color(154, 48, 26);
    private final GameApp app;
    private final GameSettings settings;
    private final GameState state;
    private final EventBus eventBus;
    private final Vector.MutableDouble cameraPosition, prevCameraPosition, targetCameraPosition;
    public boolean hasLuggage;
    // todo: currently only has one save slot
    public String name = "save";
    private double cameraSpeed;
    private World world;
    public Room room;
    private Player player;
    public long tick = 0;
    private Dialogue dialogue;
    private Set<Collectible> collectedCollectibles = new HashSet<>();
    private boolean ended;
    private final int endFadeInDuration = 100, endFadeInFinishDuration = 50, endFadeOutDuration = 100,
            endFadeOutStartDuration = 20, endDialogueStartDuration = 80;
    private int endFadeInTime = -1, endFadeOutTime = -1;
    public Door paintingsDoor, sculpturesDoor, modernDoor, artifactsDoor;

    /// Reads in a game from a previous save file
    public Game(GameApp app, Path path) throws IOException {
        var state = new GameState(path.resolve(GAME_STATE_FILENAME));
        this(app, new GameSettings(), state);
    }

    /// Writes this game to a save file location
    public boolean write(Path path) {
        try {
            path.toFile().mkdirs();
            state.write(path.resolve(GAME_STATE_FILENAME));
            return true;
        } catch (IOException e) {
            Main.log("Failed to save world to '" + path + "'; reason:");
            e.printStackTrace();
        }
        return false;
    }

    /// Creates a new game
    public Game(GameApp app, GameSettings settings) {
        this(app, settings, new GameState());
    }

    private Game(GameApp app, GameSettings settings, GameState state) {
        this.app = app;
        this.settings = settings;
        this.state = state;
        this.eventBus = new SimpleEventBus();
        eventBus.register(PlayerDeathEvent.class, this::onPlayerDeath);
        eventBus.register(RoomSwitchEvent.class, this::onRoomSwitch);
        eventBus.register(KeyPressedEvent.class, this::toggleDebug);

        try {
            world = new World(this, Path.of("/world"));
            room = world.getRoom("lobby");

            if (room.getCheckpoints().isEmpty()) throw new IllegalStateException("No respawn position");
            if (state.playerPos == null) state.playerPos = Vector.mutableDouble(room.getFirstCheckpoint().pos());

            player = room.player = new Player(room.walls, state.playerPos, new Args(""));

            this.cameraPosition = Vector.mutable(0D, 0D);
            this.prevCameraPosition = Vector.mutable(0D, 0D);
            this.targetCameraPosition = Vector.mutable(0D, 0D);
            teleportCamera();

            paintingsDoor = (Door) room.getBlockOrEntity("d_paintings");
            sculpturesDoor = (Door) room.getBlockOrEntity("d_sculptures");
            modernDoor = (Door) room.getBlockOrEntity("d_modern");
            artifactsDoor = (Door) room.getBlockOrEntity("d_artifacts");
            if (paintingsDoor == null || sculpturesDoor == null || modernDoor == null || artifactsDoor == null)
                throw new IllegalStateException("Not all doors are present");
        } catch (Exception e) {
            throw new RuntimeException("Game failed to load", e);
        }
    }

    private void onPlayerDeath(PlayerDeathEvent event) {
        if (event.player() != player) throw new IllegalStateException("Different players: " + player + " and " + event.player());
        room.reset();
    }

    private void onRoomSwitch(RoomSwitchEvent event) {
        if (event.player() != player) throw new IllegalStateException("Different players: " + player + " and " + event.player());
        this.room = event.to();
    }

    private void toggleDebug(KeyPressedEvent click) {
        if (click.input() == InputBindings.TOGGLE_DEBUG.get()) app.toggleDebug();
    }


    @Override
    public void tick() {
        Sound.tick();
        if (getDialogue() != null) getDialogue().tick();

        if (world != null) world.tick();
        if (room != null) room.tick();
        tickCameraPos();
        if (!ended) tick ++;
        else tickEnd();
    }

    private void tickCameraPos() {
        targetCameraPosition.setX(player.getX());
        targetCameraPosition.setY(player.getY() + 0.5);
        targetCameraPosition.add(player.getLastMotion().normalized());
        room.lockCamera(targetCameraPosition);

        var toTarget = targetCameraPosition.subtract(cameraPosition);
        double desiredSpeed = toTarget.length() * 0.3;
        cameraSpeed = Math.clamp(desiredSpeed, cameraSpeed - 0.1, cameraSpeed + 0.05);
        prevCameraPosition.set(cameraPosition);
        cameraPosition.add(toTarget.scale(cameraSpeed));
    }

    private void tickEnd() {
        if (endFadeInTime > endFadeInDuration) {
            if (dialogue == null && endFadeOutTime == -1) endFadeOutTime = 0;
        } else if (endFadeInTime >= 0) endFadeInTime ++;

        if (endFadeOutTime > endFadeOutDuration) {
            Main.stop(0);
        } else {
            if (endFadeOutTime >= 0) {
                endFadeOutTime++;
            }
        }

        if (endFadeInTime == endDialogueStartDuration) {
            if (finishedInTime()) setDialogue(Player.dialogue("I made it!", "0",
                    Player.dialogue("Thanks for playing!", "0", null)));
            else if (finishedWayTooLate()) setDialogue(Player.dialogue("AHH! The plane took off already! And it's landed already too!", "2",
                    Player.dialogue("ASO2 3DI;[FJO WM C_OWO[D;; K3 0f23F @#0KDSO 023KKSOD __=3= s33SDFEfff", "2", null)));
            else setDialogue(Player.dialogue("AHH! The plane took off already!", "2",
                    Player.dialogue("ASO2 3DI;[FJO WM C_OWO[D;; K3 0f23F @#0KDSO 023KKSOD __=3= s33SDFEfff", "2", null)));
        }
    }

    public void teleportCamera() {
        this.cameraPosition.set(player.getPosition()).add(0D, 0.5D);
        this.prevCameraPosition.set(player.getPosition()).add(0D, 0.5D);
        this.targetCameraPosition.set(player.getPosition()).add(0D, 0.5D);
    }


    @Override
    public void render(Canvas canvas) {
        if (world != null) world.render(canvas);
        if (room != null) room.render(canvas);
//        for (var c : collectedCollectibles) c.renderOverlay(canvas);
        if (ended) renderEndScreen(canvas);
        renderTimer(canvas);
        if (getDialogue() != null) getDialogue().render(canvas);
    }

    private void renderTimer(Canvas canvas) {
        var g = canvas.getGraphics();
        var oF = g.getFont();
        var oCr = g.getColor();

        g.setFont(TIMER_FONT);
        g.setColor(TIMER_COLOR);

        double milliseconds = (double) tick % GameDriver.TPS / GameDriver.TPS;
        long seconds = tick / GameDriver.TPS;
        long minutes = seconds / 60;
        g.drawString("%02d:%02d.%03d".formatted(minutes, seconds % 60, (int) (milliseconds * 1000)), canvas.getWidth() - 180, 60);

        g.setFont(oF);
        g.setColor(oCr);
    }

    private void renderEndScreen(Canvas canvas) {
        if (endFadeInTime > endFadeInFinishDuration) {
            canvas.renderImage(Assets.texture("gui/end"), 0, 0);
            if (endFadeInTime > endDialogueStartDuration/* && dialogue == null*/) {
                canvas.renderImage(finishedInTime() ? Assets.texture("gui/win") : Assets.texture("gui/lose"), 0, 0);
            }
        }

        var g = canvas.getGraphics();
        var oC = g.getComposite();

        { // fades
            double alpha;
            if (endFadeOutTime != -1) {
                alpha = (endFadeOutTime - endFadeOutStartDuration + canvas.partialTick()) / (endFadeInDuration - endFadeOutStartDuration);
            } else if (endFadeInTime != -1) {
                alpha = endFadeInTime < 10 ? (endFadeInTime + canvas.partialTick()) / 10D :
                        endFadeInTime > endFadeInFinishDuration ?
                                1 - (endFadeInTime + canvas.partialTick() - endFadeInFinishDuration) / (endFadeInDuration - endFadeInFinishDuration) :
                                1;
            } else alpha = 0;
            alpha = Math.clamp(alpha, 0, 1);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            canvas.renderImage(Assets.texture("gui/black"), 0, 0);
            g.setComposite(oC);
        }

    }

    @Override
    public void keyPressed(KeyBinding key) {
        eventBus.post(new KeyPressedEvent(key));
    }
    @Override
    public void keyDown(KeyBinding key) {
        eventBus.post(new KeyDownEvent(key));
    }
    @Override
    public void keyReleased(KeyBinding key) {
        eventBus.post(new KeyReleasedEvent(key));
    }

    @Override
    public void mousePressed(MouseButtonBinding button, int x, int y) {
        eventBus.post(new MousePressedEvent(button, toCoordX(x), toCoordY(y), x, y));
    }

    @Override
    public void mouseDown(MouseButtonBinding button, int x, int y) {
        eventBus.post(new MouseDownEvent(button, toCoordX(x), toCoordY(y), x, y));
    }

    @Override
    public void mouseReleased(MouseButtonBinding button, int x, int y) {
        eventBus.post(new MouseReleasedEvent(button, toCoordX(x), toCoordY(y), x, y));
    }

    @Override
    public void mouseMoved(int x, int y) {
        eventBus.post(new MouseMovedEvent(toCoordX(x), toCoordY(y), x, y));
    }

    public double toCoordX(int screenX) {
        return cameraPosition.x + (screenX - app.getPanelSize().width / 2D) / (double) settings.tileSize;
    }

    public double toCoordY(int screenY) {
        return cameraPosition.y + (app.getPanelSize().height / 2D - screenY) / (double) settings.tileSize;
    }

    public void setup() {
        app.getGameInputListener().addInputListener(this);
    }

    public void cleanup() {
        app.getGameInputListener().removeInputListener(this);
    }

    public void end() {
        if (ended) return;
        ended = true;
        endFadeInTime = 0;
    }
    public boolean ended() {
        return ended;
    }

    private boolean finishedInTime() {
        return tick <= GameDriver.TPS * 6 * 60;
    }

    private boolean finishedWayTooLate() {
        return tick <= GameDriver.TPS * 90 * 60;
    }


    public GameApp getApp() {
        return app;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setWorld(World world) {
        this.world = world;
        this.room = world.getRooms().values().stream().findFirst().orElse(null);
    }

    public Vector.MutableDouble getPrevCameraPosition() {
        return prevCameraPosition;
    }
    public Vector.MutableDouble getCameraPosition() {
        return cameraPosition;
    }
    public Vector.MutableDouble getTargetCameraPosition() {
        return targetCameraPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public void advanceDialogue() {
        if (!hasDialogue()) return;
        if (dialogue.isFinished()) dialogue = dialogue.next;
        else dialogue.finish();
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
        player.onInteract();
    }

    public boolean hasDialogue() {
        return dialogue != null;
    }

    public void collect(Collectible collectible) {
        collectedCollectibles.add(collectible);
    }

    public GameState getState() {
        return state;
    }
}
