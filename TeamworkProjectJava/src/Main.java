import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import World.Generator;
import Renderer.QuickView;

public class Main extends Application {
    public static void main(String[] args) {
        // Add functionality to display generated map, like a foreach. Use the output of the following method:
        Generator.Generate();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup nodes
        Group root = new Group();

        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Draw some stuff
        QuickView.drawGrid(gc);
        QuickView.setBlock(gc, 2, 2, 2);

        root.getChildren().add(canvas);

        // Render scene
        primaryStage.setResizable(false);
        primaryStage.setTitle("Test Window");
        primaryStage.setScene(new Scene(root, 800, 600, Color.BLACK));
        primaryStage.show();
    }
}
