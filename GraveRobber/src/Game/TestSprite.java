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
    static String testPath = "./resources/warrior.ini";
    static int index[] = {0};

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

        final double[] elapsed = {0};

        // Event handler for keyboard input
        final int[] currentSequence = {0};
        scene.setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case SPACE:
                    currentSequence[0]++;
                    break;
                case ENTER:
                    initSprite(testPath);
                    break;
                case DIGIT1:
                    currentSequence[0] = 0;
                    break;
                case DIGIT2:
                    currentSequence[0] = 1;
                    break;
                case DIGIT3:
                    currentSequence[0] = 2;
                    break;
                case DIGIT4:
                    currentSequence[0] = 3;
                    break;
                case DIGIT5:
                    currentSequence[0] = 4;
                    break;
            }
            elapsed[0] = 0;
            index[0] = 0;
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
        Coord relative = new Coord(0, 0);

        final long[] lastTick = {System.nanoTime()};
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // Timing
                elapsed[0] += (double) (currentNanoTime - lastTick[0]) / 1_000_000_000;
                lastTick[0] = currentNanoTime;

                // Determine direction
                relative.setPos(mouse[0], mouse[1]);
                double direction = Coord.angleBetween(origin, relative);

                // Sequence
                int sequenceID = currentSequence[0] % 5;
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
                    case 3:
                        current = Sequences.GETHIT;
                        break;
                    case 4:
                        current = Sequences.DIE;
                        break;
                }
                Sequence sequence = sprite.getSequence(current, direction);

                // Select frame based on time
                int newIndex = (int) (elapsed[0] * 10) % sequence.length();
                if (newIndex != index[0]) {
                    index[0] = newIndex;
                }

                // Output
                gc.clearRect(0, 0, horizontalRes, verticalRes);
                String debug = String.format("Time: %.2f%n", elapsed[0]);
                debug += String.format("Sequence: %d %s%n", currentSequence[0], current);
                gc.fillText(debug, 10, 20);
                gc.strokeOval(origin.getX() - 10, origin.getY() - 10, 20, 20);
                gc.drawImage(sequence.get(index[0]).get(), origin.getX() - sequence.get(index[0]).getOX(), origin.getY() - sequence.get(index[0]).getOY());
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
