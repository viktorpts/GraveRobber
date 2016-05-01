package Game;

import Renderer.Sequence;
import Renderer.Sprite;
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

import java.io.File;

public class TestSprite extends Application {

    final static public double horizontalRes = 800;
    final static public double verticalRes = 600;

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

        Sprite sprite = new Sprite("./resources/warrior.ini");

        /* Input


        scene.setOnMouseDragged(event -> {
            game.getPlayer().updateMouse(event.getX(), event.getY());
        });
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) game.getPlayer().setMouseLeft(true);
            if (event.isSecondaryButtonDown()) game.getPlayer().setMouseRight(true);
        });
        scene.setOnMouseReleased(event -> {
            if (!event.isPrimaryButtonDown()) game.getPlayer().setMouseLeft(false);
            if (!event.isSecondaryButtonDown()) game.getPlayer().setMouseRight(false);
        });
        */

        // Event handler for keyboard input
        final double[] currentDir = {1.5};
        scene.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.SPACE) {
                currentDir[0] += 0.1;
            }
        });
        scene.setOnKeyReleased(ke -> {
            // Key released
        });

        // Event handler for mouse position and input
        double[] mouse = {0, 0};
        scene.setOnMouseMoved(event -> {
            mouse[0] = event.getX();
            mouse[1] = event.getY();
        });

        long lastTick = System.nanoTime();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double elapsed = (double) (currentNanoTime - lastTick) / 1_000_000_000;
                int index = (int) (elapsed * 10) % 8;
                gc.clearRect(0, 0, horizontalRes, verticalRes);
                gc.fillText(String.format("%.2f", elapsed), 10, 20);
                //currentDir[0] = 0;
                Sequence sequence = sprite.getSequence("walk", currentDir[0]);
                gc.drawImage(sequence.get(index).get(), mouse[0] - sprite.getOX(index), mouse[1] - sprite.getOY(index));
            }
        }.start();

        // Render stage
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Test Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
