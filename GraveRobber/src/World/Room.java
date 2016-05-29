package World;

public class Room {

    private int x;
    private int y;
    private int width;
    private int height;
    private int originX;
    private int originY;

    public Room(int x, int y, int width, int height) {
        this.width = LevelMaker.MINSIZE + LevelMaker.rand(LevelMaker.MAXSIZE - LevelMaker.MINSIZE);
        this.width = Math.min(this.width, width);
        this.height = LevelMaker.MINSIZE + LevelMaker.rand(LevelMaker.MAXSIZE - LevelMaker.MINSIZE);
        this.height = Math.min(this.height, height);

        if (width - this.width > 0)
            this.x = x + LevelMaker.rand(width - this.width);
        else
            this.x = x;
        if (height - this.height > 0)
            this.y = y + LevelMaker.rand(height - this.height);
        else
            this.y = y;

        originX = this.x + this.width / 2;
        originY = this.y + this.height / 2;
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

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }


    //endregion ==============================

}
