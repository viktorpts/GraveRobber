package Factories;

import Enumerations.TileType;
import World.Tile;

public class TileFactory {

    public static Tile getTile(int x, int y, TileType type) {
        return new Tile(x, y, type);
    }

    public static Tile getTile(int x, int y, int type) {
        switch (type) {
            case 1:
                return new Tile(x, y, TileType.WALL);
            case 2:
                return new Tile(x, y, TileType.FLOOR);
            case 3:
                return new Tile(x, y, TileType.DOOR);
            default:
                return null;
        }
    }

}
