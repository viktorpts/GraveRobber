package World;

import Enumerations.TileType;
import Factories.TileFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DungeonMaker {

    final static int refSize = 25;
    private int mazeWidth;
    private int mazeHeight;
    private int depth;

    // TODO: replace level building array with simulated matrix
    private ArrayList<Dungeon> maze;
    private Map<Long, Tile> levelMatrix;

    public DungeonMaker(int depth) {
        this.depth = depth;
        mazeWidth = refSize * (int) Math.pow(2, (depth - 1) / 2);
        mazeHeight = mazeWidth * ((depth - 1) % 2 + 1);
        dungeonGenerate();
        processTiles();
    }

    public void dungeonGenerate() {
        maze = new ArrayList<>();
        Dungeon root = new Dungeon(0, 0, mazeWidth, mazeHeight); //
        // add root to array and pass it on
        maze.add(root);

        for (int i = 0; i <= depth; i++) {
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

    public Map<Long, Tile> getSimulatedMatrix() {
        return levelMatrix;
    }

    public ArrayList<Tile> getLevelTiles() {
        return levelMatrix.values().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    private void processTiles() {
        // Simulated matrix
        levelMatrix = new HashMap<>();

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
                    long currentIndex = getIndex(col, row, mazeWidth);
                    if (row == y || row == y + height - 1 || col == x || col == x + width - 1) { // place walls
                        if (!levelMatrix.containsKey(currentIndex)) { // only place walls in empty spaces
                            levelMatrix.put(currentIndex, TileFactory.getTile(col, row, 1));
                        }
                    } else { // place floor
                        levelMatrix.put(currentIndex, TileFactory.getTile(col, row, 2));
                    }
                }
            }
        }
        generateEndRoom(); // Generate boss room
        setTransparency() ;// Set wall transparency
    }

    private void setTransparency() {
        levelMatrix.values().stream()
                .filter(tile -> tile.getTileType() == TileType.WALL)
                .forEach(tile -> {
                    long currentIndex = getIndex((int) tile.getX(), (int) tile.getY(), mazeWidth);
                    if (levelMatrix.containsKey(currentIndex - mazeWidth) &&
                            levelMatrix.get(currentIndex - mazeWidth).getTileType() == TileType.FLOOR) {
                        tile.overlay();
                    } else if (levelMatrix.containsKey(currentIndex - mazeWidth * 2) &&
                            levelMatrix.get(currentIndex - mazeWidth * 2).getTileType() == TileType.FLOOR) {
                        tile.overlay();
                    }
                });
    }

    private void generateEndRoom() {
        // This code adds a hardcoded room outside the level with a corridor leading into it from the center
        boolean leftEnd = false, rightEnd = false;
        for (int i = 0; i < mazeHeight; i++) { // start from the top of the level
            if (leftEnd && rightEnd) {
                break;
            }
            if (levelMatrix.containsKey(getIndex(11, i, mazeWidth)) &&
                    levelMatrix.get(getIndex(11, i, mazeWidth)).getTileType() == TileType.FLOOR &&
                    !leftEnd)
                leftEnd = true;
            if (levelMatrix.containsKey(getIndex(13, i, mazeWidth)) &&
                    levelMatrix.get(getIndex(13, i, mazeWidth)).getTileType() == TileType.FLOOR &&
                    !rightEnd)
                rightEnd = true;
            // Walls
            if (!levelMatrix.containsKey(getIndex(10, i, mazeWidth)))
                levelMatrix.put(getIndex(10, i, mazeWidth), TileFactory.getTile(10, i, 1));
            if (!levelMatrix.containsKey(getIndex(14, i, mazeWidth)))
                levelMatrix.put(getIndex(14, i, mazeWidth), TileFactory.getTile(14, i, 1));
            // Floor
            levelMatrix.put(getIndex(11, i, mazeWidth), TileFactory.getTile(11, i, 2));
            levelMatrix.put(getIndex(12, i, mazeWidth), TileFactory.getTile(12, i, 2));
            levelMatrix.put(getIndex(13, i, mazeWidth), TileFactory.getTile(13, i, 2));
        }

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 16; j++) {
                Tile current;
                long currentIndex = getIndex(i + 6, j - 17, mazeWidth);
                if (i == 0 || i == 12 || j == 0 || (j == 15 && (i < 5 || i > 7)))
                    current = new Tile(i + 6, j - 17, TileType.WALL);
                else
                    current = new Tile(i + 6, j - 17, TileType.FLOOR);
                levelMatrix.put(currentIndex, current);
            }
        }
        levelMatrix.put(getIndex(10, -1, mazeWidth), TileFactory.getTile(10, -1, 1));
        levelMatrix.put(getIndex(11, -1, mazeWidth), TileFactory.getTile(11, -1, 3));
        levelMatrix.put(getIndex(12, -1, mazeWidth), TileFactory.getTile(12, -1, 3));
        levelMatrix.put(getIndex(13, -1, mazeWidth), TileFactory.getTile(13, -1, 3));
        levelMatrix.put(getIndex(14, -1, mazeWidth), TileFactory.getTile(14, -1, 1));
    }

    //========================================
    //region Static methods

    /**
     * Turn x,y coordinates in ID to be used in a linear structure. This means we can access a specific tile directly,
     * without scanning the entire level.
     *
     * @param x     Horizontal coordinate (zero-based)
     * @param y     Vertical coordinate (zero-based)
     * @param width Width of the simulated matrix
     * @return Unique zero-based index of the tile at the given coordinate x,y.
     */
    public static long getIndex(int x, int y, int width) {
        return (long) y * width + x;
    }

    //endregion ==============================

}
