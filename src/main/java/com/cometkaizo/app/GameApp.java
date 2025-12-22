package com.cometkaizo.app;

import com.cometkaizo.Main;
import com.cometkaizo.app.command.ExitCommand;
import com.cometkaizo.app.command.GameCommand;
import com.cometkaizo.app.command.TPCommand;
import com.cometkaizo.command.CommandGroup;
import com.cometkaizo.command.CommandSyntaxException;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameSettings;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.input.InputListener;
import com.cometkaizo.input.RawInputListener;
import com.cometkaizo.input.RawInputListenerImpl;
import com.cometkaizo.io.data.DataTypes;
import com.cometkaizo.screen.GameRenderer;
import com.cometkaizo.screen.Overlay;
import com.cometkaizo.screen.TitleScreen;
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

public class GameApp extends App implements Tickable {
    public static final String SAVE_DIR_NAME = "PuzzleGame";

    private final GameAppSettings settings;
    private final CommandGroup commandGroup;
    private File saveDir;

    private Game game;
    private Overlay overlay;

    private JFrame frame;
    private GameRenderer renderer;
    private RawInputListener rawInputListener;

    public GameApp(GameAppSettings settings, CommandGroup commandGroup) {
        super(settings);
        this.settings = settings;
        this.commandGroup = commandGroup;
        this.game = new Game(this, new GameSettings());
        this.overlay = new TitleScreen(this);
    }

    public GameApp() {
        super(new GameAppSettings());
        this.settings = (GameAppSettings) super.getSettings();
        this.commandGroup = new CommandGroup(
                () -> new ExitCommand(this),
                () -> new GameCommand(this),
                () -> new TPCommand(this)
        );
        this.game = new Game(this, new GameSettings());
        this.overlay = new TitleScreen(this);
    }

    public void parseInput(String input) {
        try {
            commandGroup.execute(input);
        } catch (CommandSyntaxException e) {
            Main.log(e.getMessage());
        }
    }

    @Override
    public void setup() {
        super.setup();
        Main.log("App setting up with settings:\n" + settings + "\nand command group:\n" + commandGroup);
        makeSaveDirIn(FileUtils.getAppdataDir());

        InputBindings.INPUT_BINDINGS.register(this);
        DataTypes.DATA_TYPES.register(this);

        initWindow();
        game.setup();
    }

    private void initWindow() {
        rawInputListener = new RawInputListenerImpl(InputBindings.INPUT_BINDINGS);

        frame = new JFrame(settings.name);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addKeyListener(rawInputListener);
        frame.addWindowListener(new WindowCloseListener());

        renderer = new GameRenderer(settings.defaultRendererSettings, this);
        renderer.addMouseListener(rawInputListener);
        renderer.addMouseMotionListener(rawInputListener);

        frame.add(renderer);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void addInputListener(InputListener listener) {
        rawInputListener.addInputListener(listener);
    }
    public void removeInputListener(InputListener listener) {
        rawInputListener.removeInputListener(listener);
    }

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

    @Override
    public void cleanup() {
        super.cleanup();
        Main.log("App cleaning up...");
        game.cleanup();

        frame.remove(renderer);
        frame.pack();
    }

    @Override
    public void tick() {
        super.tick();

        if (shouldTickGame()) game.tick();
        if (shouldTickOrRenderOverlay()) overlay.tick();
    }

    public void render(double partialTick) {
        renderer.setPartialTick(partialTick);
        if (frame != null) frame.repaint();
    }

    public boolean saveGameTo(Path path) {
        Main.log("Saving game to '" + path + "'");
        return game.write(path);
    }

    public boolean saveGame() {
        return saveGameTo(saveDir.toPath().resolve(game.name));
    }

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

    public boolean loadGame() {
        return loadGameFrom(saveDir.toPath().resolve(game.name));
    }

    private void setGame(Game game) {
        if (game == this.game) return;
        this.game.cleanup();
        game.setup();
        this.game = game;
    }

    public void toggleDebug() {
        renderer.toggleDebug();
    }

    private class WindowCloseListener extends WindowAdapter {
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
}
