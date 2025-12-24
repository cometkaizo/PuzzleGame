package com.cometkaizo.screen;

import com.cometkaizo.util.MathUtils;
import com.cometkaizo.world.Vector;
import com.cometkaizo.world.entity.BoundingBox;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-24
 * Description:
 * A convenience wrapper class for Graphics2D which provides the ability to use common operations such as rendering
 * using world-space coordinates (in blocks) or rendering centered text. When required functionality is too specific
 * and does not exist within this class, the {@link #getGraphics()} method can be used to acquire the raw Graphics2D instance.
 */
public class Canvas {
    private boolean debug = true;
    private int screenWidth, screenHeight;
    private double cameraX, cameraY;
    private double coordToScreen;
    private double renderScale;
    private Graphics2D g;
    private double partialTick;

    public Canvas(double coordToScreen, double cameraX, double cameraY, double renderScale, Graphics2D g) {
        this.coordToScreen = coordToScreen;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.renderScale = renderScale;
        this.g = g;
    }
    public Canvas() {

    }

    public void render(Shape shape) {
        g.draw(shape);
    }

    public void renderImage(Image image, double x, double y) {
        renderImage(image, x, y, 0, 0);
    }

    public void renderImage(Image image, double x, double y, double deltaXFactor, double deltaYFactor) {
        renderImage(image, toScreenX(x), toScreenY(y), deltaXFactor, deltaYFactor);
    }

    public void renderCenteredImage(Image image) {
        renderImage(image, halfWidth(), halfHeight(), -0.5, -0.5);
    }

    public void renderImage(Image image, int x, int y) {
        renderImage(image, x, y, 0, 0);
    }

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

    private boolean isNotVisible(double x, double y, double width, double height) {
        return x >= screenWidth || y >= screenHeight ||
                x + width <= 0 || y + height <= 0;
    }

    public int toScreenX(double coordX) {
        return toScreenX(coordX, cameraX);
    }

    public int toScreenY(double coordY) {
        return toScreenY(coordY, cameraY);
    }

    public int toScreenX(double coordX, double originX) {
        return (int) ((coordX - originX) * coordToScreen + screenWidth / 2D);
    }

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

    public Graphics2D getGraphics() {
        return g;
    }

    public int getWidth() {
        return screenWidth;
    }
    public int getHeight() {
        return screenHeight;
    }

    public int halfWidth() {
        return screenWidth / 2;
    }
    public int halfHeight() {
        return screenHeight / 2;
    }

    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    void startRender(Graphics2D g, Vector.Double prevCameraPos, Vector.Double cameraPos, int width, int height, double partialTick) {
        this.g = g;
        this.screenWidth = width;
        this.screenHeight = height;
        this.partialTick = partialTick;
        this.cameraX = lerp(prevCameraPos.getX(), cameraPos.getX());
        this.cameraY = lerp(prevCameraPos.getY(), cameraPos.getY());

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    void endRender() {
        this.g = null;
    }

    public double lerp(double from, double to) {
        return MathUtils.lerp(partialTick, from, to);
    }

    public double partialTick() {
        return partialTick;
    }

    public double renderScale() {
        return renderScale;
    }

    public int getPixelWidth() {
        return (int) (screenWidth / renderScale);
    }
    public int getPixelHeight() {
        return (int) (screenHeight / renderScale);
    }
    public int halfPixelWidth() {
        return (int) (screenWidth / renderScale / 2);
    }
    public int halfPixelHeight() {
        return (int) (screenHeight / renderScale / 2);
    }
}
