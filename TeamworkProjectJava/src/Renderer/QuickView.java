package Renderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * For testing only
 */
public class QuickView {
    // Grid size
    static final int gridSize = 20;

    static public void drawGrid(GraphicsContext gc) {
        // Line properties
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(0.5);

        // Vertical lines
        for (int i = 0; i < 80; i++) {
            gc.strokeLine(i * gridSize, 0, i * gridSize, 600);
        }
        // Horizontal lines
        for (int i = 0; i < 60; i++) {
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
}
