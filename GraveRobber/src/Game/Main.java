package Game;

import Enumerations.AbilityTypes;
import Enumerations.EntityState;
import Enumerations.GameState;
import Factories.AnimationFactory;
import Renderer.DebugView;
import Renderer.UserInterface;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import Renderer.QuickView;
import javafx.stage.StageStyle;

/**
 * Program entry point. We rely on JavaFX to do all the heavy lifting, just define some parameters to keep everything
 * synced and easily configurable.
 */
public class Main extends Application {

    // Application parameters
    public static Game game; // Container for all of the things
    final static public double horizontalRes = 800;
    final static public double verticalRes = 600;
    // Debug view
    public static GraphicsContext debugc;
    public static String debugInfo = "";

    public static void main(String[] args) {
        // Redirect
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup scene and nodes
        Pane root = new Pane();
        root.setPrefSize(horizontalRes, verticalRes);
        Scene scene = new Scene(root, horizontalRes + 300, verticalRes, Color.BLACK);
        Canvas canvas = new Canvas(horizontalRes, verticalRes); // Main canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setCursor(Cursor.NONE);
        gc.setFill(Color.BLACK);
        gc.setLineWidth(2.0);
        Canvas debugCanvas = new Canvas(300, verticalRes); // Debug info panel
        debugCanvas.setLayoutX(horizontalRes);
        debugc = debugCanvas.getGraphicsContext2D();
        root.getChildren().addAll(canvas, debugCanvas);

        // Initialize Game
        AnimationFactory.init(); // WE NEED THIS BEFORE ENTITIES ARE MADE
        game = new Game(gc, System.nanoTime());
        QuickView.adjustRes(50); // set zoom level

        //Load cursor image
        UserInterface.loadMouseImage();

        hookEventListeners(scene);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (game.getGameState() != GameState.MENU) {
                    // Update game state and render
                    game.passTime(currentNanoTime);
                    game.handleInput();
                    game.update();
                    game.render();
                    if (game.getPlayer().hasState(EntityState.DEAD)) {
                        UserInterface.drawDeadScreen();
                    } else {
                        UserInterface.drawUI(game.getPlayer());
                    }
                } else { // Visualise start menu
                    UserInterface.drawMenu();
                }
                // Debug info
                DebugView.renderAll();
            }
        }.start();

        // Render stage
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Test Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void hookEventListeners(Scene scene) {
        // Event handler for keyboard input
        scene.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.ESCAPE) {
                if (game.getGameState() == GameState.LIVE) game.setGameState(GameState.MENU);
                else if (game.getGameState() == GameState.MENU) game.setGameState(GameState.LIVE);
            } else if (ke.getCode() == KeyCode.CONTROL) {
                game.setShowEnemyHealth(true);
            } else
                game.getPlayer().addKey(ke.getCode());
        });
        scene.setOnKeyReleased(ke -> {
            if (ke.getCode() == KeyCode.CONTROL) {
                game.setShowEnemyHealth(false);
            } else
                game.getPlayer().removeKey(ke.getCode());
        });

        // Event handler for mouse position and input
        scene.setOnMouseMoved(event -> {
            game.getPlayer().updateMouse(event.getX(), event.getY());
        });
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
    }

}
