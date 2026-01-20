package com.cometkaizo.app;

import com.cometkaizo.Main;
import com.cometkaizo.app.command.*;
import com.cometkaizo.command.CommandGroup;
import com.cometkaizo.command.CommandSyntaxException;
import com.cometkaizo.event.EventBus;
import com.cometkaizo.event.SimpleEventBus;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameSettings;
import com.cometkaizo.game.event.*;
import com.cometkaizo.input.*;
import com.cometkaizo.screen.GameRenderer;
import com.cometkaizo.screen.overlay.NarrationOverlay;
import com.cometkaizo.screen.overlay.Overlay;
import com.cometkaizo.screen.overlay.TitleScreen;
import com.cometkaizo.system.app.App;
import com.cometkaizo.util.FileUtils;
import com.cometkaizo.world.Tickable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.cometkaizo.util.FileUtils.exists;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: This class represents the game application.
*/
public class GameApp extends App implements Tickable {
    public static final String SAVE_DIR_NAME = "PuzzleGame";

    private final GameAppSettings settings;
    private final CommandGroup commandGroup;
    private File saveDir;

    private Game game;
    private Overlay overlay;

    private JFrame frame;
    private GameRenderer renderer;
    private RawInputListener gameInputListener, overlayInputListener;
    private EventBus overlayEventBus = new SimpleEventBus(); // use one single event bus for all screen overlays (eg title screen, interactable interfaces, ...)

    private long lastTickTime;

    /**
     * Description: Constructs a new GameApp instance
     */
    public GameApp() {
        super(new GameAppSettings());
        this.settings = (GameAppSettings) super.getSettings();
        this.commandGroup = new CommandGroup(
                () -> new ExitCommand(this),
                () -> new SaveCommand(this),
                () -> new GiveCommand(this),
                () -> new PosCommand(this),
                () -> new TPCommand(this)
        );
        this.game = new Game(this, new GameSettings());
        setOverlay(new TitleScreen(this));
    }

    /**
     * Description: Executes a command using the given input string
     */
    public void parseInput(String input) {
        try {
            commandGroup.execute(input);
        } catch (CommandSyntaxException e) {
            Main.log(e.getMessage());
        }
    }

    /**
     * Description: sets up the app
     */
    @Override
    public void setup() {
        super.setup();
        Main.log("App setting up with settings:\n" + settings + "\nand command group:\n" + commandGroup);
        makeSaveDirIn(FileUtils.getAppdataDir());

        InputBindings.GAME.register(this);
        InputBindings.OVERLAY.register(this);

        initWindow();
        game.setup();
    }

    /**
     * Description: initializes the app window
     */
    private void initWindow() {
        frame = new JFrame(settings.name);

        gameInputListener = new RawInputListenerImpl(InputBindings.GAME, () -> !shouldTickOrRenderOverlay() && frame.isFocused());
        overlayInputListener = new RawInputListenerImpl(InputBindings.OVERLAY, () -> shouldTickOrRenderOverlay() && frame.isFocused());
        overlayInputListener.addInputListener(new OverlayInputListener());

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addKeyListener(gameInputListener);
        frame.addKeyListener(overlayInputListener);
        frame.addWindowListener(new WindowCloseListener());

        renderer = new GameRenderer(settings.defaultRendererSettings, this);
        renderer.addMouseListener(gameInputListener);
        renderer.addMouseMotionListener(gameInputListener);
        renderer.addMouseListener(overlayInputListener);
        renderer.addMouseMotionListener(overlayInputListener);

        frame.add(renderer);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Description: initializes the save directory
     */
    private void makeSaveDirIn(File parent) {
        try {
            if (exists(parent)) {
                saveDir = new File(parent, SAVE_DIR_NAME);
            } else {
                saveDir = new File(FileUtils.thisProgramLocation(), SAVE_DIR_NAME);
            }
            if (!saveDir.exists() && !saveDir.mkdirs())
                throw new IllegalStateException("Could not create data folder at " + saveDir.getPath());
        } catch (SecurityException e) {
            saveDir = null;
            throw new IllegalStateException("Could not create data folder in " + parent.getPath());
        }
    }

    /**
     * Description: cleans up the app
     */
    @Override
    public void cleanup() {
        super.cleanup();
        Main.log("App cleaning up...");
        game.cleanup();

        frame.remove(renderer);
        frame.pack();
    }

    /**
     * Description: runs every tick to update the game
     */
    @Override
    public void tick() {
        lastTickTime = System.currentTimeMillis();
        renderer.tick(lastTickTime);

        super.tick();

        gameInputListener.tick();
        overlayInputListener.tick();

        if (shouldTickGame()) game.tick();
        if (shouldTickOrRenderOverlay()) overlay.tick();

        overlayEventBus.tick();
    }

    /**
     * Description: Renders the game and waits for the render to finish
     */
    public void render() {
        if (frame != null) {
            scheduleRender();
            try {
                // wait for the async render to finish by submitting an empty task and waiting
                SwingUtilities.invokeAndWait(() -> {});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Description: Schedules an asynchronous render of the game
     */
    private void scheduleRender() {
        frame.repaint();
    }

    /**
     * Description: saves the game to the given path
     */
    public boolean saveGameTo(Path path) {
        Main.log("Saving game to '" + path + "'");
        return game.write(path);
    }

    /**
     * Description: saves the game to the default path
     */
    public boolean saveGame() {
        return saveGameTo(saveDir.toPath().resolve(game.name));
    }

    /**
     * Description: loads the game from the given path
     */
    public boolean loadGameFrom(Path path) {
        Main.log("Loading game from '" + path + "'");
        try {
            setGame(new Game(this, path));
            return true;
        } catch (IOException e) {
            Main.log("Failed with exception");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Description: loads the game from the default path
     */
    public boolean loadGame() {
        return loadGameFrom(saveDir.toPath().resolve(game.name));
    }

    /**
     * Description: sets the current game
     */
    private void setGame(Game game) {
        if (game == this.game) return;
        this.game.cleanup();
        game.setup();
        this.game = game;
    }

    /**
     * Description: toggles debug mode
     */
    public void toggleDebug() {
        renderer.toggleDebug();
    }

    /**
     * Description: listens to input events for the screen overlay
     */
    private class OverlayInputListener implements InputListener {
        /// posts an event for a key press
        @Override
        public void keyPressed(KeyBinding key) {
            overlayEventBus.post(new KeyPressedEvent(key));
        }
        /// posts an event for a key down
        @Override
        public void keyDown(KeyBinding key) {
            overlayEventBus.post(new KeyDownEvent(key));
        }
        /// posts an event for a key release
        @Override
        public void keyReleased(KeyBinding key) {
            overlayEventBus.post(new KeyReleasedEvent(key));
        }
        /// posts an event for a mouse press
        @Override
        public void mousePressed(MouseButtonBinding button, int x, int y) {
            // there are no "world coordinates" for screen overlay events, so use NaN
            overlayEventBus.post(new MousePressedEvent(button, Double.NaN, Double.NaN, x, y));
        }
        /// posts an event for a mouse press
        @Override
        public void mouseDown(MouseButtonBinding button, int x, int y) {
            overlayEventBus.post(new MouseDownEvent(button, Double.NaN, Double.NaN, x, y));
        }
        /// posts an event for a mouse release
        @Override
        public void mouseReleased(MouseButtonBinding button, int x, int y) {
            overlayEventBus.post(new MouseReleasedEvent(button, Double.NaN, Double.NaN, x, y));
        }
        /// posts an event for a mouse move
        @Override
        public void mouseMoved(int x, int y) {
            overlayEventBus.post(new MouseMovedEvent(Double.NaN, Double.NaN, x, y));
        }
    }
    /**
     * Description: listens for window closing
     */
    private class WindowCloseListener extends WindowAdapter {
        /// Asks the user for confirmation before exiting the game when the "X" is pressed on the window
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            if (JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to exit?", "Consider very carefully...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                saveGame();
                Main.stop(0);
            }
        }
    }

    // SETTERS & GETTERS

    public RawInputListener getGameInputListener() {
        return gameInputListener;
    }
    public RawInputListener getOverlayInputListener() {
        return overlayInputListener;
    }
    public EventBus getOverlayEventBus() {
        return overlayEventBus;
    }

    @Override
    public GameAppSettings getSettings() {
        return settings;
    }

    public Game getGame() {
        return game;
    }
    public Overlay getOverlay() {
        return overlay;
    }
    public void setOverlay(Overlay overlay) {
        if (this.overlay != null) this.overlay.cleanup();
        this.overlay = overlay;
        if (this.overlay != null) this.overlay.setup();
    }

    /// sets the current overlay to a NarrationOverlay with the given text and given subsequent screen
    public void narrate(String narration, Overlay next) {
        setOverlay(new NarrationOverlay(this, narration, next));
    }
    public boolean shouldTickGame() {
        return game != null && (overlay == null || overlay.shouldTickGame());
    }
    public boolean shouldRenderGame() {
        return game != null && (overlay == null || overlay.shouldRenderGame());
    }
    public boolean shouldTickOrRenderOverlay() {
        return overlay != null;
    }

    public Dimension getPanelSize() {
        return renderer.getSize();
    }

    public int getMouseX() {
        return renderer.getMouseX();
    }
    public int getMouseY() {
        return renderer.getMouseY();
    }
    public boolean isMouseDown() {
        return renderer.isMouseDown();
    }

    public long getLastTickTime() {
        return lastTickTime;
    }
}
