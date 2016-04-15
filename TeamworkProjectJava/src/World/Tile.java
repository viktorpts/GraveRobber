package World;

import Enumerations.TileType;

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
	private final byte TILE_SIZE = 1;
	private int width, height;
	private int id;
	private static int nextID = 0;
	private int x, y;
	private TileType tileType;

	public Tile(int x, int y, TileType tileType) {
		this.setX(x);
		this.setY(y);
		this.setTileType(tileType);
		this.id = nextID + 1;
		this.width = TILE_SIZE;
		this.height = TILE_SIZE;
		nextID++;
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

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

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
