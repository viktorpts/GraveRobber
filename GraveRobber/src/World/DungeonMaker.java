package World;

import Enumerations.TileType;

import java.util.ArrayList;

public class DungeonMaker {

    public static int mazeWidth = 25;
    public static int mazeHeight = 25;

    private ArrayList<Dungeon> maze;
    private ArrayList<Tile> levelTiles;

    public DungeonMaker() {
        dungeonGenerate();
        processTiles();
    }

    public void dungeonGenerate() {
        maze = new ArrayList<>();
        Dungeon root = new Dungeon(0, 0, mazeWidth, mazeHeight); //
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

    public ArrayList<Tile> getLevelTiles() {
        return levelTiles;
    }

    public ArrayList<Tile> processTiles() {
        levelTiles = new ArrayList<>(); // initialize

        // temp matrix
        byte[][] levelMatrix = new byte[mazeWidth][mazeHeight];
        // fill matrix with void
        for (int i = 0; i < levelMatrix.length; i++) {
            for (int j = 0; j < levelMatrix[i].length; j++) {
                levelMatrix[i][j] = 0;
            }
        }

        for (Dungeon dungeon : this.maze) {
            if (dungeon.getDungeon() == null && dungeon.getHallway() == null) continue; // current instance is a parent
            int x = 0;
            int y = 0;
            int width = 0;
            int height = 0;
            if (dungeon.getDungeon() != null) {
                // Room
                x = dungeon.getDungeon().getX(); // x firstCol
                y = dungeon.getDungeon().getY(); // y firstRow
                width = dungeon.getDungeon().getWidth(); // width lastCol
                height = dungeon.getDungeon().getHeight(); // height lastRow
            } else if (dungeon.getHallway() != null) {
                // Hallway
                x = dungeon.getHallway().getX();
                y = dungeon.getHallway().getY();
                width = dungeon.getHallway().getWidth();
                height = dungeon.getHallway().getHeight();
            }

            for (int row = y; row < y + height; row++) {
                for (int col = x; col < x + width; col++) {
                    if (row == y || row == y + height - 1 || col == x || col == x + width - 1) {
                        if (levelMatrix[col][row] != 2) // don't place walls on th floor
                            levelMatrix[col][row] = 1; // wall
                    } else {
                        levelMatrix[col][row] = 2; // floor
                    }
                }
            }
        }

        // This code adds a hardcoded room outside the level with a corridor leading into it from the center
        boolean leftEnd = false, rightEnd = false;
        for (int i = 0; i < mazeHeight; i++) { // start from the top of the level
            if (leftEnd && rightEnd) {
                break;
            }
            levelMatrix[12][i] = 2;
            if (!leftEnd) {
                if (levelMatrix[10][i] == 0) levelMatrix[10][i] = 1;
                if ((levelMatrix[11][i] == 2))
                    leftEnd = true;
                levelMatrix[11][i] = 2;
            }
            if (!rightEnd) {
                if (levelMatrix[14][i] == 0) levelMatrix[14][i] = 1;
                if (levelMatrix[13][i] == 2)
                    rightEnd = true;
                levelMatrix[13][i] = 2;
            }
        }

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 16; j++) {
                Tile current;
                if (i == 0 || i == 12 || j == 0 || (j == 15 && (i < 5 || i > 7)))
                    current = new Tile(i + 6, j - 17, TileType.WALL);
                else
                    current = new Tile(i + 6, j - 17, TileType.FLOOR);
                levelTiles.add(current);
            }
        }
        levelTiles.add(new Tile(10, -1, TileType.WALL));
        levelTiles.add(new Tile(11, -1, TileType.DOOR));
        levelTiles.add(new Tile(12, -1, TileType.DOOR));
        levelTiles.add(new Tile(13, -1, TileType.DOOR));
        levelTiles.add(new Tile(14, -1, TileType.WALL));

        // Convert matrix to usable tiles
        for (int i = 0; i < levelMatrix.length; i++) {
            for (int j = 0; j < levelMatrix[i].length; j++) {
                if (levelMatrix[i][j] == 0) continue;
                Tile current = new Tile(i, j, levelMatrix[i][j] == 1 ? TileType.WALL : TileType.FLOOR);
                if (current.getTileType() == TileType.WALL) {
                    if (j > 0 && levelMatrix[i][j - 1] == 2) {
                        current.overlay();
                    } else if (j > 1 && levelMatrix[i][j - 2] == 2) {
                        current.overlay();
                    }
                }
                levelTiles.add(current);
            }
        }

        return levelTiles;
    }
}
