package Game;

import Enumerations.EnemyTypes;
import Enumerations.EntityState;
import Enumerations.GameState;
import Factories.CreatureFactory;
import Models.Enemy;
import Renderer.DebugView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import Renderer.QuickView;
import javafx.stage.StageStyle;

public class Main extends Application {

    // Application parameters
    public static Game game; // sssContainer for all of the things
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
        game = new Game(gc, System.nanoTime());
        QuickView.adjustRes(50); // set zoom level
        //CreatureFactory.init(); //Initialise list of creatures

        //Load cursor image
        QuickView.loadMouseImage();

        // Event handler for keyboard input
        scene.setOnKeyPressed(ke -> {
            game.getControlState().addKey(ke.getCode());
        });
        scene.setOnKeyReleased(ke -> {
            game.getControlState().removeKey(ke.getCode());
        });

        // Event handler for mouse position and input
        scene.setOnMouseMoved(event -> {
            game.getControlState().update(event.getX(), event.getY());
        });
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) game.getControlState().setMouseLeft(true);
            if (event.isSecondaryButtonDown()) game.getControlState().setMouseRight(true);
        });
        scene.setOnMouseReleased(event -> {
            if (!event.isPrimaryButtonDown()) game.getControlState().setMouseLeft(false);
            if (!event.isSecondaryButtonDown()) game.getControlState().setMouseRight(false);
        });

        final long startNanoTime = System.nanoTime();

        new AnimationTimer() {
            public void handle(long currentNanoTime) {

                    double[] mousePos = game.getControlState().getMouse();
                    double offsetX = QuickView.toWorldX(mousePos[0]) - game.getLevel().getPlayer().getX();
                    double offsetY = QuickView.toWorldY(mousePos[1]) - game.getLevel().getPlayer().getY();
                    double dir = Math.atan2(offsetY, offsetX);
                    // Don't let the player look around if he's committed to an animation
                    if (game.getPlayer().isReady()) game.getPlayer().setDirection(dir);
                if (game.getGameState() != GameState.MENU) {
                    // Update state

                    game.update(currentNanoTime);
                    game.handleInput();
                    // Output
                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, horizontalRes, verticalRes);
                    QuickView.moveCamera(game.getLevel().getPlayer().getX(), // focus camera on player
                            game.getLevel().getPlayer().getY());
                    QuickView.drawGrid(gc);
                    if (game.getGameState() != GameState.MENU) {
                        game.render();
                    }

                    // Health bar
                    gc.save();
                    gc.setFill(Color.RED);
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    gc.strokeRect(10, 10, 220, 20);
                    gc.fillRect(12, 12, 216 * game.getPlayer().getHealthPoints() / 100, 16);
                    gc.restore();

                    if (game.getPlayer().hasState(EntityState.DEAD)) {
                        gc.save();
                        gc.setFill(Color.RED);
                        gc.setStroke(Color.BLACK);
                        gc.setLineWidth(1);
                        gc.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 72));
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.strokeText("WASTED", horizontalRes / 2, verticalRes / 2 - QuickView.gridSize);
                        gc.fillText("WASTED", horizontalRes / 2, verticalRes / 2 - QuickView.gridSize);
                        gc.restore();
                    } else {
                        QuickView.renderArrow(mousePos[0], mousePos[1], dir);
                    }
                }
                else{
                    QuickView.renderMenuBackground();
                    QuickView.renderStartButton(100, 100);
                    QuickView.renderExitButton(100, 200);
                    QuickView.renderMenuCursor(game.getControlState().getMouseX(),game.getControlState().getMouseY());
                }
                /**
                 * Debug info
                 */
                DebugView.clear();
                DebugView.showPlayerInfo(); // Player position and movement
                DebugView.showControlInfo(); // Input state
                DebugView.showEntityData();
                DebugView.showInfo(); // Custom data from other objects
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
