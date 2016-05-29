package Game;

import Enumerations.TileType;
import World.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import Renderer.QuickView;

/**
 * Test level geometry generation from here. Zoom level is set to very low, so the entire map is visible.
 */
public class TestLevel extends Application {

    static int CURRENT_LEVEL = 1;
    public static String debug = "";
    public static GraphicsContext gc;
    private static LevelMaker maker;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup scene and nodes
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        Canvas canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE);
        root.getChildren().add(canvas);

        LevelMaker.init();
        maker = new LevelMaker(1);

        // Draw grid
        QuickView.gridSize = 5;
        //QuickView.drawGrid(gc);

        // Register event handler for key presses
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                // Advance generation
                if (ke.getCode() == KeyCode.SPACE) {
                    maker.nextLevel();
                }
                if (ke.getCode() == KeyCode.ENTER) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, 800, 600);
                    debug = "";
                    debug += String.format("%d%n", maker.getDepth());
                    // Initialize generator, make rooms within boundaries
                    maker.makeLevel();
                    // Render dungeon
                    maker.getBsp().forEach(part -> {
                        gc.strokeRect(part.getX() * 5, part.getY() * 5, part.getWidth() * 5, part.getHeight() * 5);
                    });
                    gc.setFill(Color.DARKGREEN);
                    maker.getHallways().forEach(hallway -> {
                        gc.fillRect(hallway.getX() * 5, hallway.getY() * 5, hallway.getWidth() * 5, hallway.getHeight() * 5);
                    });
                    gc.setFill(Color.GREY);
                    final int[] counter = {0};
                    maker.getRooms().forEach(room -> {
                        counter[0]++;
                        gc.fillRect(room.getX() * 5, room.getY() * 5, room.getWidth() * 5, room.getHeight() * 5);
                    });
                    debug += String.format("%d%n", counter[0]);
                    gc.setFill(Color.GREENYELLOW);
                    gc.fillText(debug, 400, 20);


                    // Draw tiles
                    maker.getTiles().values().stream()
                            .filter(tile -> tile.getTileType() == TileType.WALL)
                            .forEach(tile -> QuickView.setBlock(gc, (int) tile.getX(), (int) tile.getY(), 1));
                    //QuickView.drawGrid(gc);
                }
            }
        });

        // Render stage
        primaryStage.setResizable(false);
        primaryStage.setTitle("Test Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
