package World;

import Enumerations.TileType;
import Factories.TileFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Stream;

public class LevelMaker {

    private final int REFSIZE = 15;
    private int depth;
    private int width;
    private int height;
    private Partition bsp;
    private LinkedList<Partition> queue;
    private HashMap<Long, Tile> levelMatrix;

    //========================================
    //region Properties

    public int getDepth() {
        return depth;
    }

    public Partition getRoot() {
        return bsp;
    }

    public HashMap<Long, Tile> getTiles() {
        return levelMatrix;
    }

    public long getIndex(int x, int y) {
        return (long) y * width + x;
    }

    //endregion ==============================

    //========================================
    //region Initialization

    public LevelMaker(int depth) {
        this.depth = depth;
        calcSize();
        makeLevel();
    }

    public void nextLevel() {
        depth += (int)Math.max(2, (depth) * 0.2);
        calcSize();
        makeLevel();
    }

    public void makeLevel() {
        bsp = new Partition(0, 0, width, height, null);
        queue = new LinkedList<>();
        queue.addLast(bsp);
        for (int i = 0; i < depth; i++) {
            Partition current = queue.removeFirst();
            if (current.trySplit()) {
                queue.addLast(current.getLeftLeaf());
                queue.addLast(current.getRightLeaf());
            }
        }
        bsp.makeRoom();
        bsp.makeHallway();
        processTiles();
        setTransparency();
    }

    private void calcSize() {
        width = REFSIZE * (int) Math.sqrt(depth + 1);
        height = (int) (width * 0.67);
    }

    //endregion ==============================

    //========================================
    //region Extract geometry

    public Stream<Partition> getBsp() {
        ArrayList<Partition> partitions = new ArrayList<>();
        LinkedList<Partition> q = new LinkedList<>();

        q.addLast(bsp);
        while (q.size() > 0) {
            Partition current = q.removeFirst();
            if (current.hasLeaves()) {
                q.addLast(current.getLeftLeaf());
                q.addLast(current.getRightLeaf());
            } else {
                partitions.add(current);
            }
        }
        return partitions.stream();
    }

    public Stream<Room> getRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        LinkedList<Partition> q = new LinkedList<>();

        q.addLast(bsp);
        while (q.size() > 0) {
            Partition current = q.removeFirst();
            if (current.hasLeaves()) {
                q.addLast(current.getLeftLeaf());
                q.addLast(current.getRightLeaf());
            } else {
                rooms.add(current.getRoom());
            }
        }
        return rooms.stream();
    }

    public Stream<Hallway> getHallways() {
        ArrayList<Hallway> hallways = new ArrayList<>();
        LinkedList<Partition> q = new LinkedList<>();

        q.addLast(bsp);
        while (q.size() > 0) {
            Partition current = q.removeFirst();
            if (current.hasLeaves()) {
                q.addLast(current.getLeftLeaf());
                q.addLast(current.getRightLeaf());
            }
            if (current.getHallway() != null) {
                hallways.add(current.getHallway());
            }
        }
        return hallways.stream();
    }

    public void processTiles() {
        levelMatrix = new HashMap<>();

        getRooms().forEach(room -> {
            int startX = room.getX();
            int startY = room.getY();
            int endX = startX + room.getWidth();
            int endY = startY + room.getHeight();
            for (int col = startX; col < endX; col++) {
                for (int row = startY; row < endY; row++) {
                    long currentIndex = getIndex(col, row, width);
                    if (row == startY || row == endY - 1 || col == startX || col == endX - 1) {
                        if (!levelMatrix.containsKey(currentIndex)) {
                            levelMatrix.put(currentIndex, TileFactory.getTile(col, row, 1));
                        }
                    } else {
                        levelMatrix.put(currentIndex, TileFactory.getTile(col, row, 2));
                    }
                }
            }
        });
        getHallways().forEach(hallway -> {
            int startX = hallway.getX();
            int startY = hallway.getY();
            int endX = startX + hallway.getWidth();
            int endY = startY + hallway.getHeight();
            for (int col = startX; col < endX; col++) {
                for (int row = startY; row < endY; row++) {
                    long currentIndex = getIndex(col, row, width);
                    if (row == startY || row == endY - 1 || col == startX || col == endX - 1) {
                        if (!levelMatrix.containsKey(currentIndex)) {
                            levelMatrix.put(currentIndex, TileFactory.getTile(col, row, 1));
                        }
                    } else {
                        levelMatrix.put(currentIndex, TileFactory.getTile(col, row, 2));
                    }
                }
            }
        });
    }

    private void setTransparency() {
        levelMatrix.values().stream()
                .filter(tile -> tile.getTileType() == TileType.WALL)
                .forEach(tile -> {
                    long currentIndex = getIndex((int) tile.getX(), (int) tile.getY(), width);
                    if (levelMatrix.containsKey(currentIndex - width) &&
                            levelMatrix.get(currentIndex - width).getTileType() == TileType.FLOOR) {
                        tile.overlay();
                    } else if (levelMatrix.containsKey(currentIndex - width * 2) &&
                            levelMatrix.get(currentIndex - width * 2).getTileType() == TileType.FLOOR) {
                        tile.overlay();
                    }
                });
    }

    //endregion ==============================

    //========================================
    //region Static parameters

    final public static int MINSIZE = 7;
    final public static int MAXSIZE = 15;
    final public static float MINRATIO = 0.33f;
    final public static float MAXRATIO = 0.66f;
    final public static int HALLSIZE = 5;
    private static Random rnd;

    public static void init() {
        rnd = new Random();
    }

    public static void init(long seed) {
        rnd = new Random(seed);
    }

    public static int rand(int bound) {
        return rnd.nextInt(bound);
    }

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
