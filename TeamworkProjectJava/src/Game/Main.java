package Game;

import Models.Enemy;
import Models.Player;
import Models.Sprite;
import World.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
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
import java.util.StringJoiner;

public class Main extends Application {

    // Application parameters
    public static Game game; // Container for all of the things
    final static double horizontalRes = 800;
    final static double verticalRes = 600;
    // Debug view
    public static GraphicsContext debugc;

    public static void main(String[] args) {
        // Redirect
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup scene and nodes
        Pane root = new Pane();
        root.setPrefSize(horizontalRes,verticalRes);
        Scene scene = new Scene(root, horizontalRes + 300, verticalRes, Color.BLACK);
        Canvas canvas = new Canvas(horizontalRes, verticalRes); // Main canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setCursor(Cursor.NONE);
        gc.setFill(Color.BLACK);
        Canvas debugCanvas = new Canvas(300, verticalRes); // Main view
        debugCanvas.setLayoutX(horizontalRes);
        GraphicsContext debugc = debugCanvas.getGraphicsContext2D();
        debugc.setFill(Color.WHITE);
        debugc.fillRect(0, 0, 300, verticalRes);
        root.getChildren().addAll(canvas, debugCanvas);

        // Initialize Game
        game = new Game(gc, System.nanoTime());
        QuickView.gridSize = 60;

        // Event handler for keyboard input
        scene.setOnKeyPressed(ke -> { game.getControlState().addKey(ke.getCode()); });
        scene.setOnKeyReleased(ke -> { game.getControlState().removeKey(ke.getCode()); });

        // Event handler for mouse position
        scene.setOnMouseMoved(event -> { game.getControlState().update(event.getX(), event.getY()); });
        scene.setOnMousePressed(event -> {
            game.getControlState().mouseLeft = true;
            if (event.isSecondaryButtonDown()) game.getLevel().getEntities()
                    .add(new Enemy(100, 10, 0, event.getX() / QuickView.gridSize, event.getY() / QuickView.gridSize, true));
        } );
        scene.setOnMouseReleased(event -> { game.getControlState().mouseLeft = false; } );

        final long startNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double[] mousePos = game.getControlState().getMouse();
                double offsetX = mousePos[0] - game.getLevel().getPlayer().getX() * QuickView.gridSize;
                double offsetY = mousePos[1] - game.getLevel().getPlayer().getY() * QuickView.gridSize;
                double dir = Math.atan2(offsetY, offsetX);
                game.getPlayer().setDirection(dir);

                // Update state
                game.update(currentNanoTime);
                game.handleInput();
                // Output
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, horizontalRes, verticalRes);
                QuickView.drawGrid(gc);
                game.render();
                QuickView.renderArrow(mousePos[0], mousePos[1], dir);

                /**
                 * Debug info
                 */
                debugc.setFill(Color.BLACK);
                debugc.setStroke(Color.GREENYELLOW);
                debugc.fillRect(0, 0, 300, verticalRes);
                debugc.strokeRect(1, 1, 298, verticalRes - 2);
                debugc.setFill(Color.GREENYELLOW);
                // Mouse position relative to player
                String mouseData = String.format("%.2f -> %.2f", mousePos[0], offsetX);
                mouseData += String.format("%n%.2f -> %.2f", mousePos[1], offsetY);
                //gc.fillText(mouseData, mousePos[0] + 20, mousePos[1]);
                // Player position and movement
                String playerData = String.format("Position %.2f, %.2f", game.getLevel().getPlayer().getX(), game.getLevel().getPlayer().getY());
                playerData += String.format("%nVelocity %.2f", game.getLevel().getPlayer().getVelocity().getMagnitude());
                playerData += String.format("%n%.2f %.2f", game.getLevel().getPlayer().getVelocity().getX(), game.getLevel().getPlayer().getVelocity().getY());
                // Control state
                playerData += String.format("%n");
                playerData += String.format("%n%s", game.getControlState().mouseLeft);
                for (KeyCode key : game.getControlState().getCombo()) {
                    playerData += String.format("%n%s", key.toString());
                }
                debugc.fillText(playerData, 5, 15);
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
