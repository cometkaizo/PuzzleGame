package com.cometkaizo.app;

import com.cometkaizo.screen.GameRenderer;
import com.cometkaizo.system.app.AppSettings;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Settings for the game application
 */
public class GameAppSettings extends AppSettings {

    public int defaultWidth = 1280;
    public int defaultHeight = 720;
    public String name = "Puzzle Game";
    public Color defaultBackgroundColor = new Color(35, 34, 57);
    public GameRenderer.Settings defaultRendererSettings = new GameRenderer.Settings(new Dimension(defaultWidth, defaultHeight), defaultBackgroundColor);

}
