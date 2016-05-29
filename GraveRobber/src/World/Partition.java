package World;

public class Partition {

    private Partition parent;
    private Partition leftLeaf;
    private Partition rightLeaf;

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean direction; // false = horizontal split; true = vertical split

    private Room room;
    private Hallway hallway;

    public Partition(int x, int y, int width, int height, Partition parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    //========================================
    //region Properties

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHorizontal() {
        return direction;
    }

    public Partition getLeftLeaf() {
        return leftLeaf;
    }

    public Partition getRightLeaf() {
        return rightLeaf;
    }

    public Room getRoom() {
        return room;
    }

    public Hallway getHallway() {
        return hallway;
    }

    public boolean hasLeaves() {
        return leftLeaf != null;
    }

    //endregion ==============================

    //========================================
    //region Generation

    public boolean trySplit() {
        if (hasLeaves()) {
            return leftLeaf.trySplit() || rightLeaf.trySplit();
        } else {
            return split();
        }
    }

    private boolean split() {
        direction = false; // false = horizontal, true = vertical
        if (width / height > LevelMaker.MAXRATIO) {
            direction = true;
        } else if (height / width > LevelMaker.MAXRATIO) {
            direction = false;
        } else {
            direction = LevelMaker.rand(1) == 1; // 0 = horizontal, 1 = vertical
        }
        if (!direction) { // top and bottom leaves
            if (height <= 2 * LevelMaker.MINSIZE) return false;
            int half = LevelMaker.MINSIZE + LevelMaker.rand(height - 2 * LevelMaker.MINSIZE);
            half = (int) Math.max(half, LevelMaker.MINRATIO * height);
            half = (int) Math.min(half, LevelMaker.MAXRATIO * height);
            leftLeaf = new Partition(x, y, width, half, this);
            rightLeaf = new Partition(x, y + half, width, height - half, this);
        } else { // left and right leaves
            if (width <= 2 * LevelMaker.MINSIZE) return false;
            int half = LevelMaker.MINSIZE + LevelMaker.rand(width - 2 * LevelMaker.MINSIZE);
            half = (int) Math.max(half, LevelMaker.MINRATIO * width);
            half = (int) Math.min(half, LevelMaker.MAXRATIO * width);
            leftLeaf = new Partition(x, y, half, height, this);
            rightLeaf = new Partition(x + half, y, width - half, height, this);
        }
        return true;
    }

    public void makeRoom() {
        if (hasLeaves()) {
            leftLeaf.makeRoom();
            rightLeaf.makeRoom();
        } else {
            room = new Room(x, y, width, height);
        }
    }

    public void makeHallway() {
        if (!hasLeaves()) { // Connect room to parent partition origin
            hallway = new Hallway(room, parent, parent.isHorizontal());
        } else { // Connect leaf partitions to each other
            leftLeaf.makeHallway();
            rightLeaf.makeHallway();
            hallway = new Hallway(leftLeaf, rightLeaf, direction);
        }
    }

    //endregion ==============================

}
