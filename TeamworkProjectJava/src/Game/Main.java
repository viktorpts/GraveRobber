package Game;

import Enumerations.EntityState;
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
import javafx.stage.Stage;

import Renderer.QuickView;
import javafx.stage.StageStyle;

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
        root.setPrefSize(horizontalRes,verticalRes);
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

        // Event handler for keyboard input
        scene.setOnKeyPressed(ke -> { game.getControlState().addKey(ke.getCode()); });
        scene.setOnKeyReleased(ke -> { game.getControlState().removeKey(ke.getCode()); });

        // Event handler for mouse position and input
        scene.setOnMouseMoved(event -> { game.getControlState().update(event.getX(), event.getY()); });
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) game.getControlState().mouseLeft = true;
            if (event.isSecondaryButtonDown()) game.getLevel().getEntities()
                    .add( new Enemy( 100, 10, 0, QuickView.toWorldX(event.getX()), QuickView.toWorldY(event.getY()) ) );
        } );
        scene.setOnMouseReleased(event -> {
            if (game.getControlState().isMouseLeft() && !event.isPrimaryButtonDown()) {
                game.getControlState().mouseLeft = false;
            } } );

        final long startNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double[] mousePos = game.getControlState().getMouse();
                double offsetX = QuickView.toWorldX(mousePos[0]) - game.getLevel().getPlayer().getX();
                double offsetY = QuickView.toWorldY(mousePos[1]) - game.getLevel().getPlayer().getY();
                double dir = Math.atan2(offsetY, offsetX);
                // Don't let the player look around if he's committed to an animation
                if (!game.getPlayer().hasState(EntityState.CASTUP) &&
                        !game.getPlayer().hasState(EntityState.CASTING) &&
                        !game.getPlayer().hasState(EntityState.CASTDOWN)) game.getPlayer().setDirection(dir);

                // Update state
                game.update(currentNanoTime);
                game.handleInput();
                // Output
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, horizontalRes, verticalRes);
                QuickView.moveCamera(game.getLevel().getPlayer().getX(), // focus camera on player
                        game.getLevel().getPlayer().getY());
                QuickView.drawGrid(gc);
                game.render();
                QuickView.renderArrow(mousePos[0], mousePos[1], dir);

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
