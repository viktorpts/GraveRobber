package World;

public class Hallway {

    private int x;
    private int y;
    private int width;
    private int height;

    public Hallway(Room room, Partition parent, boolean horizontal) {
        int originX = room.getOriginX();
        int originY = room.getOriginY();
        int targetX = parent.getX() + parent.getWidth() / 2;
        int targetY = parent.getY() + parent.getHeight() / 2;
        if (!horizontal) {
            x = Math.min(originX, targetX) - LevelMaker.HALLSIZE / 2;
            y = originY - LevelMaker.HALLSIZE / 2;
            width = Math.abs(originX - targetX) + LevelMaker.HALLSIZE;
            height = LevelMaker.HALLSIZE;
        } else { // vertical
            x = originX - LevelMaker.HALLSIZE / 2;
            y = Math.min(originY, targetY) - LevelMaker.HALLSIZE / 2;
            width = LevelMaker.HALLSIZE;
            height = Math.abs(originY - targetY) + LevelMaker.HALLSIZE;
        }
    }

    public Hallway(Partition leftLeaf, Partition rightLeaf, boolean horizontal) {
        int originX;
        int originY;
        int targetX;
        int targetY;
        if (horizontal) {
            if (leftLeaf.getRoom() != null) {
                originX = leftLeaf.getRoom().getX() + leftLeaf.getRoom().getWidth() / 2;
            } else {
                originX = leftLeaf.getX() + leftLeaf.getWidth() / 2;
            }
            if (rightLeaf.getRoom() != null) {
                targetX = rightLeaf.getRoom().getX() + rightLeaf.getRoom().getWidth() / 2;
            } else {
                targetX = rightLeaf.getX() + rightLeaf.getWidth() / 2;
            }
            originY = leftLeaf.getY() + leftLeaf.getHeight() / 2;
            x = originX - LevelMaker.HALLSIZE / 2;
            y = originY - LevelMaker.HALLSIZE / 2;
            width = targetX - originX + LevelMaker.HALLSIZE;
            height = LevelMaker.HALLSIZE;
        } else { // vertical
            if (leftLeaf.getRoom() != null) {
                originY = leftLeaf.getRoom().getY() + leftLeaf.getRoom().getHeight() / 2;
            } else {
                originY = leftLeaf.getY() + leftLeaf.getHeight() / 2;
            }
            if (rightLeaf.getRoom() != null) {
                targetY = rightLeaf.getRoom().getY() + rightLeaf.getRoom().getHeight() / 2;
            } else {
                targetY = rightLeaf.getY() + rightLeaf.getHeight() / 2;
            }
            originX = leftLeaf.getX() + leftLeaf.getWidth() / 2;
            x = originX - LevelMaker.HALLSIZE / 2;
            y = originY - LevelMaker.HALLSIZE / 2;
            width = LevelMaker.HALLSIZE;
            height = targetY - originY + LevelMaker.HALLSIZE;
        }
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


    //endregion ==============================
}
