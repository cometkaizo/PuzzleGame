package com.cometkaizo.game;

import com.cometkaizo.Main;
import com.cometkaizo.app.GameApp;
import com.cometkaizo.event.EventBus;
import com.cometkaizo.event.SimpleEventBus;
import com.cometkaizo.game.event.*;
import com.cometkaizo.game.item.Inventory;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.input.InputListener;
import com.cometkaizo.input.KeyBinding;
import com.cometkaizo.input.MouseButtonBinding;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.*;
import com.cometkaizo.world.entity.Door;
import com.cometkaizo.world.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents a running game save
 */
public class Game implements Tickable, Renderable, InputListener {
    public static final String GAME_STATE_FILENAME = "state.txt";
    private final GameApp app;
    private final GameSettings settings;
    private final GameState state;
    private final EventBus eventBus;
    private final Vector.MutableDouble cameraPosition, prevCameraPosition, targetCameraPosition;
    // currently only has one save slot
    public String name = "save";
    private double cameraSpeed;
    private World world;
    public Room room;
    private Player player;
    public long tick = 0;
    public Door paintingsDoor, sculpturesDoor, modernDoor, artifactsDoor, artifactsHallDoor, libraryDoor, chessDoor, basementDoor;
    private final Inventory inventory = new Inventory();
    private boolean devMode;

    /// Reads in a game from a previous save file
    public Game(GameApp app, Path path) throws IOException {
        GameState state;
        try {
            state = new GameState(path.resolve(GAME_STATE_FILENAME));
        } catch (Exception e) {
            Main.err("Failed to load state file: " + path.resolve(GAME_STATE_FILENAME) + " due to " + e + "\nLoading fresh save...");
            state = new GameState();
        }
        this(app, new GameSettings(), state);
    }

    /// Writes this game to a save file location
    public boolean write(Path path) {
        try {
            path.toFile().mkdirs();
            inventory.write(state);
            world.write(state);
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

    /// Creates a new game
    private Game(GameApp app, GameSettings settings, GameState state) {
        this.app = app;
        this.settings = settings;
        this.state = state;
        this.eventBus = new SimpleEventBus();
        eventBus.register(PlayerDeathEvent.class, this::onPlayerDeath);
        eventBus.register(KeyPressedEvent.class, this::toggleDebug);

        try {
            inventory.set(state.inventory);

            world = new World(this, Path.of("/world"));
            room = world.getRoom("lobby");

            if (room.getCheckpoints().isEmpty()) throw new IllegalStateException("No respawn position");
            if (state.playerPos == null) state.playerPos = Vector.mutableDouble(room.getFirstCheckpoint().pos());

            player = new Player(room.walls, Vector.mutableDouble(state.playerPos), new Args(""));
            room.setPlayer(player);

            this.cameraPosition = Vector.mutable(0D, 0D);
            this.prevCameraPosition = Vector.mutable(0D, 0D);
            this.targetCameraPosition = Vector.mutable(0D, 0D);
            teleportCamera();

            paintingsDoor = (Door) room.getBlockOrEntity("d_paintings");
            sculpturesDoor = (Door) room.getBlockOrEntity("d_sculptures");
            modernDoor = (Door) room.getBlockOrEntity("d_modern");
            artifactsDoor = (Door) room.getBlockOrEntity("d_artifacts");
            artifactsHallDoor = (Door) room.getBlockOrEntity("h_artifacts");
            libraryDoor = (Door) room.getBlockOrEntity("d_library");
            chessDoor = (Door) room.getBlockOrEntity("d_chess");
            basementDoor = (Door) room.getBlockOrEntity("d_basement");
            if (paintingsDoor == null ||
                    sculpturesDoor == null ||
                    modernDoor == null ||
                    artifactsDoor == null ||
                    artifactsHallDoor == null ||
                    libraryDoor == null ||
                    chessDoor == null ||
                    basementDoor == null)
                throw new IllegalStateException("Not all doors are present");
        } catch (Exception e) {
            throw new RuntimeException("Game failed to load", e);
        }
    }

    /// Resets the room in the event of a player death
    private void onPlayerDeath(PlayerDeathEvent event) {
        if (event.player() != player) throw new IllegalStateException("Different players: " + player + " and " + event.player());
        room.reset();
    }

    /// Potentially toggles debug mode if the debug mode button is pressed
    private void toggleDebug(KeyPressedEvent click) {
        if (click.input() == InputBindings.TOGGLE_DEBUG.get()) if (devMode) app.toggleDebug();
    }


    /// Ticks this game
    @Override
    public void tick() {
        Sound.tick();

        if (world != null) world.tick();
        if (room != null) room.tick();
        tickCameraPos();

        eventBus.tick();

        tick ++;
    }

    /// Updates the camera position
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

    /// Teleports the camera to the player position
    public void teleportCamera() {
        this.cameraPosition.set(player.getPosition()).add(0D, 0.5D);
        this.prevCameraPosition.set(player.getPosition()).add(0D, 0.5D);
        this.targetCameraPosition.set(player.getPosition()).add(0D, 0.5D);
    }


    /// Renders the game to the screen
    @Override
    public void render(Canvas canvas) {
        if (world != null) world.render(canvas);
        if (room != null) room.render(canvas);
        renderEndRoom(canvas);
    }

    /// Renders the text that appears in the end room
    private void renderEndRoom(Canvas canvas) {
        canvas.renderString("The End", Assets.font(80), Color.WHITE, canvas.toScreenX(35), canvas.toScreenY(45), true, true);
        canvas.renderString("Thanks for playing", Assets.font(60), Color.WHITE, canvas.toScreenX(35), canvas.toScreenY(44.5), true, true);

        canvas.renderString("Andy Wang", Assets.font(40), Color.WHITE, canvas.toScreenX(32), canvas.toScreenY(43), true, true);
        canvas.renderString("Programming", Assets.font(30), Color.WHITE, canvas.toScreenX(32), canvas.toScreenY(42.5), true, false);
        canvas.renderString("Story", Assets.font(30), Color.WHITE, canvas.toScreenX(32), canvas.toScreenY(42.2), true, false);
        canvas.renderString("Visual Assets", Assets.font(30), Color.WHITE, canvas.toScreenX(32), canvas.toScreenY(41.9), true, false);
        canvas.renderString("Puzzle Design", Assets.font(30), Color.WHITE, canvas.toScreenX(32), canvas.toScreenY(41.6), true, false);

        canvas.renderString("Rowan Howell", Assets.font(40), Color.WHITE, canvas.toScreenX(38), canvas.toScreenY(43), true, true);
        canvas.renderString("Story", Assets.font(30), Color.WHITE, canvas.toScreenX(38), canvas.toScreenY(42.5), true, false);
        canvas.renderString("Visual Assets", Assets.font(30), Color.WHITE, canvas.toScreenX(38), canvas.toScreenY(42.2), true, false);
        canvas.renderString("Puzzle Design", Assets.font(30), Color.WHITE, canvas.toScreenX(38), canvas.toScreenY(41.9), true, false);
        canvas.renderString("Music", Assets.font(30), Color.WHITE, canvas.toScreenX(38), canvas.toScreenY(41.6), true, false);
    }

    /// Posts key-pressed events
    @Override
    public void keyPressed(KeyBinding key) {
        eventBus.post(new KeyPressedEvent(key));
    }
    /// Posts key-down events
    @Override
    public void keyDown(KeyBinding key) {
        eventBus.post(new KeyDownEvent(key));
    }
    /// Posts key-released events
    @Override
    public void keyReleased(KeyBinding key) {
        eventBus.post(new KeyReleasedEvent(key));
    }

    /// Posts mouse-pressed events
    @Override
    public void mousePressed(MouseButtonBinding button, int x, int y) {
        eventBus.post(new MousePressedEvent(button, toCoordX(x), toCoordY(y), x, y));
    }
    /// Posts mouse-down events
    @Override
    public void mouseDown(MouseButtonBinding button, int x, int y) {
        eventBus.post(new MouseDownEvent(button, toCoordX(x), toCoordY(y), x, y));
    }
    /// Posts mouse-released events
    @Override
    public void mouseReleased(MouseButtonBinding button, int x, int y) {
        eventBus.post(new MouseReleasedEvent(button, toCoordX(x), toCoordY(y), x, y));
    }
    /// Posts mouse-moved events
    @Override
    public void mouseMoved(int x, int y) {
        eventBus.post(new MouseMovedEvent(toCoordX(x), toCoordY(y), x, y));
    }

    /// Turns a screen position in pixels to a world position in blocks
    public double toCoordX(int screenX) {
        return cameraPosition.x + (screenX - app.getPanelSize().width / 2D) / (double) settings.tileSize;
    }
    /// Turns a screen position in pixels to a world position in blocks
    public double toCoordY(int screenY) {
        return cameraPosition.y + (app.getPanelSize().height / 2D - screenY) / (double) settings.tileSize;
    }

    /// Sets up the game
    public void setup() {
        app.getGameInputListener().addInputListener(this);
    }
    /// Cleans up the game for termination
    public void cleanup() {
        app.getGameInputListener().removeInputListener(this);
    }



    // SETTERS & GETTERS

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
    /// Gets the max-y value of the screen
    public int getCameraTop() {
        return (int) Math.ceil(cameraPosition.y + settings.heightInTiles / 2 + settings.cameraPaddingInTiles);
    }
    /// Gets the min-y value of the screen
    public int getCameraBottom() {
        return (int) Math.floor(cameraPosition.y - settings.heightInTiles / 2 - settings.cameraPaddingInTiles);
    }
    /// Gets the min-x value of the screen
    public int getCameraLeft() {
        return (int) Math.floor(cameraPosition.x - settings.widthInTiles / 2 - settings.cameraPaddingInTiles);
    }
    /// Gets the max-x value of the screen
    public int getCameraRight() {
        return (int) Math.ceil(cameraPosition.x + settings.widthInTiles / 2 + settings.cameraPaddingInTiles);
    }

    public Player getPlayer() {
        return player;
    }

    public GameState getState() {
        return state;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
    public boolean isDevMode() {
        return devMode;
    }
}
