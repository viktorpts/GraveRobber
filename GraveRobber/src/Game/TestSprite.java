package Game;

import Enumerations.Sequences;
import Renderer.Sequence;
import Renderer.Sprite;
import World.Coord;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestSprite extends Application {

    final static public double horizontalRes = 800;
    final static public double verticalRes = 600;
    static Sprite sprite;
    static String testPath = "./resources/rat.ini";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup scene and nodes
        Pane root = new Pane();
        root.setPrefSize(horizontalRes, verticalRes);
        Scene scene = new Scene(root, horizontalRes, verticalRes, Color.BLACK);
        Canvas canvas = new Canvas(horizontalRes, verticalRes); // Main canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        root.getChildren().addAll(canvas);

        // Initialize sprite for testing
        initSprite(testPath);

        /* Input
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) game.getPlayer().setMouseLeft(true);
            if (event.isSecondaryButtonDown()) game.getPlayer().setMouseRight(true);
        });
        */

        // Event handler for keyboard input
        final int[] currentSequence = {0};
        scene.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.SPACE) {
                currentSequence[0]++;
            } else if (ke.getCode() == KeyCode.ENTER) {
                initSprite(testPath);
            }
        });
        scene.setOnKeyReleased(ke -> {
            // Key released
        });

        // Event handler for mouse position and input
        final double[] mouse = {0, 0};
        scene.setOnMouseMoved(event -> {
            mouse[0] = event.getX();
            mouse[1] = event.getY();
        });
        Coord origin = new Coord(horizontalRes / 2, verticalRes / 2);
        Coord relative = new Coord(0,0);

        long lastTick = System.nanoTime();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // Timing
                double elapsed = (double) (currentNanoTime - lastTick) / 1_000_000_000;

                // Determine direction
                relative.setPos(mouse[0], mouse[1]);
                double direction = Coord.angleBetween(origin, relative);

                // Sequence
                int sequenceID = currentSequence[0] % 3;
                Sequences current = Sequences.IDLE;
                switch (sequenceID) {
                    case 0:
                        current = Sequences.IDLE;
                        break;
                    case 1:
                        current = Sequences.WALK;
                        break;
                    case 2:
                        current = Sequences.ATTACK;
                        break;
                }
                Sequence sequence = sprite.getSequence(current, direction);

                // Select frame based on time
                int index = (int) (elapsed * 10) % sequence.length();

                // Output
                gc.clearRect(0, 0, horizontalRes, verticalRes);
                String debug = String.format("Time: %.2f%n", elapsed);
                debug += String.format("Sequence: %d %s%n", currentSequence[0], current);
                gc.fillText(debug, 10, 20);
                gc.strokeOval(origin.getX() - 10, origin.getY() - 10, 20, 20);
                gc.drawImage(sequence.get(index).get(), origin.getX() - sequence.get(index).getOX(), origin.getY() - sequence.get(index).getOY());
            }
        }.start();

        // Render stage
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Test Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void initSprite(String path) {
        sprite = new Sprite(path);
    }

}
