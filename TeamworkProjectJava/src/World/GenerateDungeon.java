package World;

import Renderer.QuickView;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GenerateDungeon {

    private static Random rnd = new Random();


    public static ArrayList<Dungeon> sampleMake() {
        ArrayList<Dungeon> rectangles = new ArrayList<>(); // flat rectangle store to help pick a random one
        Dungeon root = new Dungeon(0, 0, 120, 120); //
        // add root to array and pass it on
        rectangles.add(root);
        return rectangles;
    }

    /**
     * This method creates dungeons iteratively and outputs them to the grid
     */
    public static void sampleStep(ArrayList<Dungeon> rectangles) {
        ArrayList<Dungeon> listCopy = new ArrayList<>(rectangles);
        for (Dungeon rectangle : listCopy) {
            if (rectangle.getLeftChild() != null) continue;
            if (rectangle.split()) { //attempt to split
                rectangles.add(rectangle.getLeftChild());
                rectangles.add(rectangle.getRightChild());
            }
        }
    }

    /**
     * @param list ArrayList of Dungeon to be trimmed to leaves only
     * @return The trimmed list
     */
    public static ArrayList<Dungeon> filterTree(ArrayList<Dungeon> list) {
        ArrayList<Dungeon> result = new ArrayList<>();
        for (Dungeon dungeon : list) {
            if (dungeon.dungeon == null && dungeon.getHallway() == null) continue;
            result.add(dungeon);
        }
        return result;
    }

    private static void printDungeons(ArrayList<Dungeon> rectangles) {
        byte[][] lines = new byte[60][];
        for (int i = 0; i < 60; i++) {
            lines[i] = new byte[120];
            for (int j = 0; j < 120; j++)
                lines[i][j] = -1;
        }
        byte dungeonCount = -1;
        for (Dungeon r : rectangles) {
            if (r.dungeon == null)
                continue;
            Dungeon d = r.dungeon;
            dungeonCount++;
            for (int i = 0; i < d.getHeight(); i++) {
                for (int j = 0; j < d.getWidth(); j++)

                    lines[d.getX() + i][d.getY() + j] = dungeonCount;
            }
        }
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 120; j++) {
                if (lines[i][j] == -1)
                    System.out.print('.');
                else
                    System.out.print(lines[i][j]);
            }
            System.out.println();
        }
    }
}