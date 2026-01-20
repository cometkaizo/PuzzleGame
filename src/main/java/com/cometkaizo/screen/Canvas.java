package com.cometkaizo.screen;

import com.cometkaizo.util.MathUtils;
import com.cometkaizo.world.Vector;
import com.cometkaizo.world.entity.BoundingBox;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-15
 * Description:
 * A convenience wrapper class for Graphics2D which provides the ability to use common operations such as rendering
 * using world-space coordinates (in blocks) or rendering centered text. When required functionality is too specific
 * and does not exist within this class, the {@link #getGraphics()} method can be used to acquire the raw Graphics2D instance.
 */
public class Canvas {
    private boolean debug;
    private int screenWidth, screenHeight;
    private double cameraX, cameraY;
    private double coordToScreen;
    private double unscaledTileSize;
    private double renderScale;
    private Graphics2D g;
    private double partialTick;

    /// Creates a new canvas
    public Canvas(double cameraX, double cameraY, double unscaledTileSize, double renderScale, Graphics2D g) {
        this.unscaledTileSize = unscaledTileSize;
        this.coordToScreen = unscaledTileSize * renderScale;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.renderScale = renderScale;
        this.g = g;
    }
    /// Creates a new canvas
    public Canvas() {

    }

    /// Draws the given shape to the screen
    public void render(Shape shape) {
        g.draw(shape);
    }

    /// Draws the given image to the screen at the given location specified in blocks
    public void renderImage(Image image, double x, double y) {
        renderImage(image, x, y, 0, 0);
    }

    /// Draws the given image to the screen at the given location specified in blocks with an offset specified in percentage of the size of the image
    public void renderImage(Image image, double x, double y, double deltaXFactor, double deltaYFactor) {
        renderImage(image, toScreenX(x), toScreenY(y), deltaXFactor, deltaYFactor);
    }

    /// Draws the given image to the screen at the center of the screen
    public void renderCenteredImage(Image image) {
        renderImage(image, halfWidth(), halfHeight(), -0.5, -0.5);
    }

    /// Draws the given image to the screen at the given location specified in pixels
    public void renderImage(Image image, int x, int y) {
        renderImage(image, x, y, 0, 0);
    }

    /// Draws the given image to the screen at the given location specified in blocks with an offset specified in percentage of the size of the image
    public void renderImage(Image image, int x, int y, double deltaXFactor, double deltaYFactor) {
        double width = image.getWidth(null) * renderScale;
        double height = image.getHeight(null) * renderScale;

        double actualX = x + width * deltaXFactor;
        double actualY = y + height * deltaYFactor;

        if (isNotVisible(actualX, actualY, width, height)) return;

        g.drawImage(image,
                (int) actualX,
                (int) actualY,
                (int) width,
                (int) height,
                null);
    }

    /// Copies part of the given image to the given location, specified in blocks
    public void blitImage(Image image, double srcX, double srcY, double destX, double destY, double w, double h) {
        blitImage(image, toUnscaledScreenLength(srcX), toUnscaledScreenLength(srcY), toScreenX(destX), toScreenY(destY), toUnscaledScreenLength(w), toUnscaledScreenLength(h));
    }

    /// Copies part of the given image to the given location, specified in pixels
    public void blitImage(Image image, int srcX, int srcY, int destX, int destY, int w, int h) {
        int scaledW = scale(w);
        int scaledH = scale(h);

        if (isNotVisible(destX, destY, scaledW, scaledH)) return;

        g.drawImage(image,
                destX,
                destY,
                destX + scaledW,
                destY + scaledH,
                srcX,
                srcY,
                srcX + w,
                srcY + h,
                null);
    }

    /// Draws a string to the screen at the given location, specified in pixels
    public void renderString(String str, Font font, Color color, float x, float y, boolean centerX, boolean centerY) {
        var oF = g.getFont();
        var oCr = g.getColor();

        g.setFont(font);
        g.setColor(color);
        var fontMetrics = g.getFontMetrics();

        if (centerX) x += - fontMetrics.stringWidth(str) / 2F;
        if (centerY) y += - fontMetrics.getHeight() / 2F + fontMetrics.getAscent();

        g.drawString(str, x, y);

        g.setFont(oF);
        g.setColor(oCr);
    }

    /// Draws a string to the screen if debug mode is enabled
    public void renderDebugString(String str, Color color, float x, float y) {
        if (isDebug()) renderString(str, Assets.font("BoldPixels", 24), color, x, y, false, false);
    }

    /// Renders the bounding box in red for debug purposes
    public void renderDebugBoundingBox(BoundingBox boundingBox, Color color) {
        renderDebugRect(
                toScreenX(boundingBox.getX()),
                toScreenY(boundingBox.getY() + boundingBox.getHeight()),
                toScreenLength(boundingBox.getWidth()),
                toScreenLength(boundingBox.getHeight()),
                color
        );
    }
    /// Renders the block at the position in red for debug purposes
    public void renderDebugBlock(Vector.Int position, Color color) {
        renderDebugRect(
                toScreenX(position.getX()),
                toScreenY(position.getY() + 1),
                toScreenLength(1),
                toScreenLength(1),
                color
        );
    }
    /// Renders the bounding box in red for debug purposes
    public void renderDebugRect(int x, int y, int w, int h, Color color) {
        if (!debug) return;
        var oldColor = g.getColor();
        var oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(3));
        g.setColor(color);

        g.drawRect(x, y, w, h);

        g.setColor(oldColor);
        g.setStroke(oldStroke);
    }

    /// Fills the screen with the given color
    public void fillScreen(Color color) {
        var oldColor = g.getColor();

        g.setColor(color);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(oldColor);
    }

    /// Returns true if the given (x, y, width, height) rectangle is not on the screen, and false otherwise
    private boolean isNotVisible(double x, double y, double width, double height) {
        return x >= screenWidth || y >= screenHeight ||
                x + width <= 0 || y + height <= 0;
    }

    /// Converts a x world-coordinate in blocks to a screen coordinate in pixels
    public int toScreenX(double coordX) {
        return toScreenX(coordX, cameraX);
    }

    /// Converts a y world-coordinate in blocks to a screen coordinate in pixels
    public int toScreenY(double coordY) {
        return toScreenY(coordY, cameraY);
    }

    /// Converts a x world-coordinate in blocks to a screen coordinate in pixels, using the given origin
    public int toScreenX(double coordX, double originX) {
        return (int) ((coordX - originX) * coordToScreen + screenWidth / 2D);
    }

    /// Converts a y world-coordinate in blocks to a screen coordinate in pixels, using the given origin
    public int toScreenY(double coordY, double originY) {
        return screenHeight / 2 - (int) ((coordY - originY) * coordToScreen);
    }

    /// Scales the number of pixel-art pixels to the length of actual screen pixels
    public int scale(double pixels) {
        return (int) (pixels * renderScale);
    }
    /// Converts a world-space length in blocks to screen-length in pixels
    public int toScreenLength(double coordLen) {
        return (int) (coordLen * coordToScreen);
    }
    /// Converts a world-space length in blocks to unscaled screen-length in pixels (i.e. renderScale is not applied)
    private int toUnscaledScreenLength(double coordLen) {
        return (int) (coordLen * unscaledTileSize);
    }

    /// Gets the Graphics2D backing this Canvas
    public Graphics2D getGraphics() {
        return g;
    }

    /// Gets the screen width
    public int getWidth() {
        return screenWidth;
    }
    /// Gets the screen height
    public int getHeight() {
        return screenHeight;
    }

    /// Gets half the screen width
    public int halfWidth() {
        return screenWidth / 2;
    }
    /// Gets half the screen height
    public int halfHeight() {
        return screenHeight / 2;
    }

    /// Returns whether this canvas is in debug mode
    public boolean isDebug() {
        return debug;
    }
    /// Sets the debug mode
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /// Sets up the parameters required for the next render event
    void startRender(Graphics2D g, Vector.Double prevCameraPos, Vector.Double cameraPos, int width, int height, double partialTick) {
        this.g = g;
        this.screenWidth = width;
        this.screenHeight = height;
        this.partialTick = partialTick;
        this.cameraX = lerp(prevCameraPos.getX(), cameraPos.getX());
        this.cameraY = lerp(prevCameraPos.getY(), cameraPos.getY());

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /// Ends the render event
    void endRender() {
        this.g = null;
    }

    /// Performs a linear interpolation between from and to using the current partial tick
    public double lerp(double from, double to) {
        return MathUtils.lerp(partialTick, from, to);
    }

    /// Gets the current partial tick
    public double partialTick() {
        return partialTick;
    }

    /// Gets the render scale (size of one pixel in a texture)
    public double renderScale() {
        return renderScale;
    }

    /// Gets the width of the screen in unscaled pixels
    public int getPixelWidth() {
        return (int) (screenWidth / renderScale);
    }
    /// Gets the height of the screen in unscaled pixels
    public int getPixelHeight() {
        return (int) (screenHeight / renderScale);
    }
    /// Gets half the width of the screen in unscaled pixels
    public int halfPixelWidth() {
        return (int) (screenWidth / renderScale / 2);
    }
    /// Gets half the height of the screen in unscaled pixels
    public int halfPixelHeight() {
        return (int) (screenHeight / renderScale / 2);
    }
}
