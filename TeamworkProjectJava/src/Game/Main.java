package Game;

import Models.Player;
import Models.Sprite;
import World.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import Renderer.QuickView;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class Main extends Application {

    // Application parameters
    public static Game game; // Container for all of the things
    final static double horizontalRes = 800;
    final static double verticalRes = 600;

    public static void main(String[] args) {
        // Redirect
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup scene and nodes
        Pane root = new Pane();
        root.setPrefSize(horizontalRes,verticalRes);
        Scene scene = new Scene(root, horizontalRes, verticalRes, Color.BLACK);
        Canvas canvas = new Canvas(horizontalRes, verticalRes);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        root.getChildren().add(canvas);

        // Initialize Game
        game = new Game(gc, System.nanoTime());

        // Event handler for keyboard input
        scene.setOnKeyPressed(ke -> { game.getControlState().addKey(ke.getCode()); });
        scene.setOnKeyReleased(ke -> { game.getControlState().removeKey(ke.getCode()); });

        // Event handler for mouse position
        scene.setOnMouseMoved(event -> { game.getControlState().update(event.getX(), event.getY()); });

        final long startNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                game.update(currentNanoTime);
                game.handleInput();

                double[] mousePos = game.getControlState().getMouse();
                double offsetX = mousePos[0] - game.getLevel().getPlayer().getX() * QuickView.gridSize;
                double offsetY = mousePos[1] - game.getLevel().getPlayer().getY() * QuickView.gridSize;
                double dir = Math.atan(offsetY / offsetX);
                if (offsetX >= 0) dir = dir + Math.PI;

                // Output
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, 800, 600);
                QuickView.drawGrid(gc);
                game.render();
                QuickView.renderDot(mousePos[0], mousePos[1]);

                /**
                 * Debug info
                 */
                // Mouse position relative to player
                gc.setFill(Color.WHITE);
                String mouseData = mousePos[0] + " -> " + offsetX + "\n" + mousePos[1] + " -> " + offsetY;
                gc.fillText(mouseData, mousePos[0] + 20, mousePos[1]);
                // Player position and movement
                String playerData = String.format("Position %.2f, %.2f", game.getLevel().getPlayer().getX(), game.getLevel().getPlayer().getY());
                playerData += String.format("%nVelocity %.2f", game.getLevel().getPlayer().getVelocity().getMagnitude());
                playerData += String.format("%n%.2f %.2f", game.getLevel().getPlayer().getVelocity().getX(), game.getLevel().getPlayer().getVelocity().getY());
                // Control state
                playerData += String.format("%n");
                for (KeyCode key : game.getControlState().getCombo()) {
                    playerData += String.format("%n%s", key.toString());
                }
                gc.fillText(playerData, 5, 15);
                /**
                 * End of Debug info
                 */
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
