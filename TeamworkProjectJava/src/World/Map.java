package World;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Map {
	private ArrayList<Dungeon> maze;

	private ArrayList<Tile> levelTiles;

	public Map() {
		this.maze = new ArrayList<Dungeon>();
		dungeonGenerate();
	}

	public void dungeonGenerate(){
		maze = new ArrayList<>();
		Dungeon root = new Dungeon(2, 2, 160, 120); //
		// add root to array and pass it on
		maze.add(root);

		for (int i = 0; i < Level.CURRENT_LEVEL + 1; i++) {
			ArrayList<Dungeon> listCopy = new ArrayList<Dungeon>(maze);
			for (Dungeon rectangle : listCopy) {
				if (rectangle.getLeftChild() != null) continue;
				if (rectangle.split()) { //attempt to split
					maze.add(rectangle.getLeftChild());
					maze.add(rectangle.getRightChild());
				}
			}
		}
		maze.get(0).generateDungeon();
		ArrayList<Dungeon> result = new ArrayList<>();
		for (Dungeon dungeon : maze) {
			if (dungeon.getDungeon() == null && dungeon.getHallway() == null) continue;
			result.add(dungeon);
		}
		maze = result;
	}

	public ArrayList<Dungeon> getMaze() {
		return maze;
	}

	public static void getMap(){
		List<Dungeon> currentMap = GenerateDungeon.generateLevel();
		Image floor;
		for (Dungeon dungeon : currentMap) {
			if (dungeon.getDungeon() != null) {
				// Room
				int x1 = dungeon.getDungeon().getX();
				int y1 = dungeon.getDungeon().getY();
				int x2 = dungeon.getDungeon().getWidth() + x1;
				int y2 = dungeon.getDungeon().getHeight() + y1;
				Image img1 = new Image("/room.png");
				/*
                    for (int i = x1; i < x2; i++) {
                        setBlock(gc, i, y1, 1);
                        setBlock(gc, i, y2 - 1, 1);
                    }
                    for (int j = y1; j < y2; j++) {
                        setBlock(gc, x1, j, 1);
                        setBlock(gc, x2 - 1, j, 1);
                    }
                    */
			} else {
				// Hallway
				if (dungeon.getHallway() != null) {
					int hx1 = dungeon.getHallway().getX();
					int hy1 = dungeon.getHallway().getY();
					int hx2 = dungeon.getHallway().getWidth() + hx1;
					int hy2 = dungeon.getHallway().getHeight() + hy1;
				}
			}
		}
	}
}
