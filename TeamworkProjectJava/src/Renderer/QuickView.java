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
    static public int gridSize = 5;

    static public void drawGrid(GraphicsContext gc) {
        // Line properties
        gc.save();
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
        gc.restore();
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

    static public void renderDungeon(GraphicsContext gc, ArrayList<Dungeon> list, boolean finalize) {
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
            if (finalize) {
                // Room
                int x1 = dungeon.dungeon.getX();
                int y1 = dungeon.dungeon.getY();
                int x2 = dungeon.dungeon.getWidth() + x1;
                int y2 = dungeon.dungeon.getHeight() + y1;
                for (int i = x1; i < x2; i++) {
                    setBlock(gc, i, y1, 1);
                    setBlock(gc, i, y2 - 1, 1);
                }
                for (int j = y1; j < y2; j++) {
                    setBlock(gc, x1, j, 1);
                    setBlock(gc, x2 - 1, j, 1);
                }
            }
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
        double dirX = x + size * 0.6 * Math.cos(dir);
        double dirY = y + size * 0.6 * Math.sin(dir);

        gc.setStroke(Color.WHITE);
        gc.setFill(Color.GREY);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.strokeOval(x - size / 2, y - size / 2, size, size);
        gc.strokeLine(x, y, dirX, dirY);

        /**
         * Debug info
         */
        String debug = String.format("%.2f, %.2f", x, y);
        debug += String.format("%n%.2f", dir);
        gc.setFill(Color.WHITE);
        gc.fillText(debug, x + gridSize / 2, y);
    }

    static public void renderDot(double x, double y) {
        GraphicsContext gc = Main.game.getGc();
        // temp constants
        double size = 15;
        gc.setFill(Color.GREY);
        if (Main.game.getControlState().isMouseLeft())
        {
            gc.setFill(Color.RED);
            size = 25;
        }
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    static public void renderArrow(double x, double y, double dir) {
        GraphicsContext gc = Main.game.getGc();
        double size = 5;
        gc.setFill(Color.WHITE);
        if (Main.game.getControlState().isMouseLeft())
        {
            gc.setFill(Color.RED);
            size = 6;
        }
        double[] aX = {
                size * 1 * Math.cos(dir),
                size * 2 * Math.cos(dir + Math.PI/2),
                size * 2.82 * Math.cos(dir + Math.PI*3/4),
                size * 1 * Math.cos(dir + Math.PI),
                size * 2.82 * Math.cos(dir + Math.PI*5/4),
                size * 2 * Math.cos(dir + Math.PI*3/2)
        };
        double[] aY = {
                size * 1 * Math.sin(dir),
                size * 2 * Math.sin(dir + Math.PI/2),
                size * 2.82 * Math.sin(dir + Math.PI*3/4),
                size * 1 * Math.sin(dir + Math.PI),
                size * 2.82 * Math.sin(dir + Math.PI*5/4),
                size * 2 * Math.sin(dir + Math.PI*3/2)
        };
        for (int i = 0; i < 6; i++) {
            aX[i] += x;
            aY[i] += y;
        }
        gc.fillPolygon(aX, aY, 6);
    }

    static public void renderSwipe(double x, double y) {
        GraphicsContext gc = Main.game.getGc();
        // temp constants
        double size = 10;
        // Convert position to pixels
        x *= gridSize;
        y *= gridSize;

        gc.setFill(Color.RED);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
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
