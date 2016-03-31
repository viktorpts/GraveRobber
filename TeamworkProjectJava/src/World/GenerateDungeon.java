package World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class GenerateDungeon {

	private static Random rnd = new Random();


	public  static ArrayList<Rectangle> rooms;

	public static void main(String[] args) {
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

		rooms = getRoomParams(rectangles);// get rooms

		rectangles.removeAll(rectangles); //flush content

		printRooms(rooms); //this is just to test the output
	}

	private static ArrayList<Rectangle> getRoomParams(ArrayList<Dungeon> room) {
		ArrayList<Rectangle> rooms = new ArrayList<>();
		for (Dungeon r : room) {
			if (r.dungeon == null) {
				continue;
			}
			//get room x,y,width,height
			Dungeon d = r.dungeon;
			rooms.add(new Rectangle(d.getX(), d.getY(), d.getWidth(), d.getHeight()));
		}
		return rooms;
	}


	private static void printRooms(ArrayList<Rectangle> rectangles) {
		byte[][] lines = new byte[60][];
		for (int i = 0; i < 60; i++) {
			lines[i] = new byte[120];
			for (int j = 0; j < 120; j++)
				lines[i][j] = -1;
		}
		byte dungeonCount = -1;
		for (Rectangle r : rectangles) {
			if (r == null)
				continue;
			Rectangle d = r;
			dungeonCount++;
			for (int i = 0; i < d.getHeight(); i++) {
				for (int j = 0; j < d.getWidth(); j++) {
					lines[(int) d.getX() + i][(int) d.getY() + j] = dungeonCount;
				}
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

	public static ArrayList<Rectangle> getRooms() {
		return rooms;
	}
}