package Renderer;

import World.Dungeon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * For testing only
 */
public class QuickView {
    // Grid size
    static final int gridSize = 4;

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
            int x1 = dungeon.getX();
            int y1 = dungeon.getY();
            int x2 = dungeon.getWidth() + x1;
            int y2 = dungeon.getHeight() + y1;
            for (int i = x1; i < x2; i++) {
                setBlock(gc, i, y1, 2);
                setBlock(gc, i, y2-1, 2);
            }
            for (int j = y1; j < y2; j++) {
                setBlock(gc, x1, j, 2);
                setBlock(gc, x2-1, j, 2);
            }
        }
    }
}
