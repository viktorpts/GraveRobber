package Renderer;

import Game.Main;
import Models.Sprite;
import World.Dungeon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * For testing only
 */
public class QuickView {
    // Grid size
    static public final int gridSize = 25;

    static public void drawGrid(GraphicsContext gc) {
        // Line properties
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(0.5);

        // Vertical lines
        for (int i = 0; i < 800 / gridSize; i++) {
            gc.strokeLine(i * gridSize, 0, i * gridSize, 600);
        }
        // Horizontal lines
        for (int i = 0; i < 600 / gridSize; i++) {
            gc.strokeLine(0, i * gridSize, 800, i * gridSize);
        }
    }

    static public void setBlock(GraphicsContext gc, int x, int y, int type) {
        /**
         * Type:
         * 0 - empty (void)
         * 1 - white (walls)
         * 2 - grey (floor)
         */
        switch (type) {
            case 0:
                gc.setFill(Color.BLACK);
                break;
            case 1:
                gc.setFill(Color.WHITE);
                break;
            case 2:
                gc.setFill(Color.GREY);
                break;
        }
        gc.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
    }

    static public void renderDungeon(GraphicsContext gc, ArrayList<Dungeon> list) {
        for (Dungeon dungeon : list) {
            // Boundary
            int bx1 = dungeon.getX();
            int by1 = dungeon.getY();
            int bx2 = dungeon.getWidth() + bx1;
            int by2 = dungeon.getHeight() + by1;
            for (int i = bx1; i < bx2; i++) {
                setBlock(gc, i, by1, 2);
                setBlock(gc, i, by2 - 1, 2);
            }
            for (int j = by1; j < by2; j++) {
                setBlock(gc, bx1, j, 2);
                setBlock(gc, bx2 - 1, j, 2);
            }
            //// Room
            //int x1 = dungeon.dungeon.getX();
            //int y1 = dungeon.dungeon.getY();
            //int x2 = dungeon.dungeon.getWidth() + x1;
            //int y2 = dungeon.dungeon.getHeight() + y1;
            //for (int i = x1; i < x2; i++) {
            //    setBlock(gc, i, y1, 1);
            //    setBlock(gc, i, y2 - 1, 1);
            //}
            //for (int j = y1; j < y2; j++) {
            //    setBlock(gc, x1, j, 1);
            //    setBlock(gc, x2 - 1, j, 1);
            //}
        }
    }

    static public void renderSprite(double x, double y, double dir) {
        GraphicsContext gc = Main.game.getGc();
        // temp constants
        double size = gridSize / 2;
        // Translate direction indicator
        // /!\ Inverted coordinate system!
        // Convert position to pixels
        x *= gridSize;
        y *= gridSize;
        double dirX = x - size * 1.2 * Math.cos(dir);
        double dirY = y - size * 1.2 * Math.sin(dir);

        gc.setStroke(Color.WHITE);
        gc.setFill(Color.GREY);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.strokeOval(x - size / 2, y - size / 2, size, size);
        gc.strokeLine(x, y, dirX, dirY);
    }

    static public void renderDot(double x, double y) {
        GraphicsContext gc = Main.game.getGc();
        // temp constants
        double size = 10;

        gc.setStroke(Color.WHITE);
        gc.setFill(Color.GREY);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.strokeOval(x - size / 2, y - size / 2, size, size);
    }

    /**
     * Obsolete
     */
    static public void renderScene(double dir) {
        GraphicsContext gc = Main.game.getGc();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);
        drawGrid(gc);
        renderSprite(400, 300, dir);
        // Draw other stuff
    }

    // TODO add method for rendering bitmaps using javafx.Image -> WritableImage -> PixelWriter
}
