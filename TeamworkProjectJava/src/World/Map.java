package World;

import Enumerations.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Map {
	private ArrayList<Dungeon> maze;

	private ArrayList<Tile> levelTiles;

	public Map() {
		this.maze = new ArrayList<Dungeon>();
		dungeonGenerate();
		getTiles();
	}

	public void dungeonGenerate() {
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

	public void getTiles() {
		levelTiles = new ArrayList<>();
		for (Dungeon dungeon : this.maze) {
			if (dungeon.getDungeon() != null) {
				// Room
				//x firstRow
				int x1 = dungeon.getDungeon().getX();
				//y firstCol
				int y1 = dungeon.getDungeon().getY();
				//width lastRow
				int x2 = dungeon.getDungeon().getWidth() + x1;
				//height lastCol
				int y2 = dungeon.getDungeon().getHeight() + y1;


				for (int r = x1; r < x2; r++) {
					for (int c = y1; c < y2; c++) {
						if (r == x1 || c == y1 || r == x2 || c == y2) {
							levelTiles.add(new Tile(x1, c, TileType.WALL));
						} else {
							levelTiles.add(new Tile(x1, c, TileType.FLOOR));
						}

					}
				}


			} else if (dungeon.getHallway() != null) {
				// Hallway

				int hx1 = dungeon.getHallway().getX();
				int hy1 = dungeon.getHallway().getY();
				int hx2 = dungeon.getHallway().getWidth() + hx1;
				int hy2 = dungeon.getHallway().getHeight() + hy1;

				for (int r = hx1; r < hx2; r++) {
					for (int c = hy1; c < hy2; c++) {
						if (r == hx1 || c == hy1 || r == hx2 || c == hy2) {
							levelTiles.add(new Tile(hx1, c, TileType.WALL));
						} else {
							levelTiles.add(new Tile(hx1, c, TileType.FLOOR));
						}

					}
				}
			}

		}
	}
}
