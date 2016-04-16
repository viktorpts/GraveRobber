package World;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Map {
	private Dungeon maze;

	private ArrayList<Tile> levelTiles;

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
