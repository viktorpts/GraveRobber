package World;
import Renderer.QuickView;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;


public class GenerateDungeon {

	private static Random rnd = new Random();


	public static void makeSample() {
		ArrayList<Dungeon> rectangles = new ArrayList<>(); // flat rectangle store to help pick a random one
		Dungeon root = new Dungeon(0, 0, 60, 120); //
		rectangles.add(root); //populate rectangle store with root area
		while (rectangles.size() < 19) { // this will give us 10 leaf areas
			int splitIdx = rnd.nextInt(rectangles.size()); // choose a random element
			Dungeon toSplit = rectangles.get(splitIdx);
			if (toSplit.split()) { //attempt to split
				rectangles.add(toSplit.getLeftChild());
				rectangles.add(toSplit.getRightChild());
			}

		}
		root.generateDungeon(); //generate dungeons

		//printDungeons(rectangles); //this is just to test the output
	}

    /**
     * This method creates dungeons iteratively and outputs them to the grid
     */
	public static ArrayList<Dungeon> sampleStep() {
		ArrayList<Dungeon> rectangles = new ArrayList<>(); // flat rectangle store to help pick a random one
		Dungeon root = new Dungeon(0, 0, 60, 120); //
		rectangles.add(root); //populate rectangle store with root area
		while (rectangles.size() < 19) { // this will give us 10 leaf areas
			int splitIdx = rnd.nextInt(rectangles.size()); // choose a random element
			Dungeon toSplit = rectangles.get(splitIdx);
			if (toSplit.split()) { //attempt to split
				rectangles.add(toSplit.getLeftChild());
				rectangles.add(toSplit.getRightChild());
			}

		}
		root.generateDungeon(); //generate dungeons

        return rectangles;
	}

    /**
     *
     * @param list ArrayList of Dungeon to be trimmed to leaves only
     * @return The trimmed list
     */
    public static ArrayList<Dungeon> filterTree(ArrayList<Dungeon> list) {
        ArrayList<Dungeon> result = new ArrayList<Dungeon>();
        for (Dungeon dungeon : list) {
            if (dungeon.dungeon == null) continue;
            result.add(dungeon);
        }
        return  result;
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