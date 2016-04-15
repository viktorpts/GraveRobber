package World;

import Enumerations.TileType;
import javafx.scene.image.*;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.*;
import javafx.scene.image.Image;

public class Tile {
	/**
	 * Да се създаде клас, който представя квадратно поле с размер 1х1метър от игралната карта. Класа трябва да
	 * съхранява следната информация:
	 * - Пореден номер на полето (без повтаряне)
	 * - Координати на полето
	 * - Плътност (дали може да се преминава през него): solid (плътно), transparent(свободно преминаване, пода на
	 * играланта карта), clip (блокира преминаване на създания, не блокира ефекти)
	 * - Как изглежда полето (име на Sprite или път към изображение на харддиска)
	 * - Допълнителна информация за изобразяването на полето, ако е нужна
	 * <p>
	 * При подадена информация, обектът трябва да може да изпълнява следните функции:
	 * - Да променя своята плътност и/или външен вид
	 * <p>
	 * <p>
	 * Всички полета се съхраняват в масив, който да може да се обхожда и сортира чрез Stream API
	 */

	private int id;
	private static int nextID = 0;
	private int x, y;
	private int width, height;
	private TileType tileType;

	public Tile(int x, int y, int width, int height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = ++nextID;
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

	//Setters

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}


	//Getters

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TileType getTileType() {
		return tileType;
	}
}
