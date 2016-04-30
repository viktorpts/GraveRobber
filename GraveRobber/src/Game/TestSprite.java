package Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        gc.setFill(Color.BLACK);
        gc.setLineWidth(2.0);
        root.getChildren().addAll(canvas);

        /* Input
        // Event handler for keyboard input
        scene.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.ESCAPE) {
                if (game.getGameState() == GameState.LIVE) game.setGameState(GameState.MENU);
                else if (game.getGameState() == GameState.MENU) game.setGameState(GameState.LIVE);
            } else
                game.getPlayer().addKey(ke.getCode());
        });
        scene.setOnKeyReleased(ke -> {
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
        */

        new AnimationTimer() {
            public void handle(long currentNanoTime) {

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
