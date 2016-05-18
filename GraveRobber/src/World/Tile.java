package World;

import Enumerations.TileType;
import java.util.List;

import Interfaces.IRenderable;
import Renderer.QuickView;
import javafx.scene.image.Image;

public class Tile implements IRenderable {

	private int id;
	private static int nextID = 0;
	private int x, y;
	private TileType tileType;
	private Image img;
    private boolean transparent = false;

	public Tile(int x, int y, TileType tileType) {

		this.x = x;
		this.y = y;
		this.id = ++nextID;
		this.tileType = tileType;
		this.img = setImageTile(tileType); // removed to prevent exception error on missing files
	}

    // TODO: fix paths
	public Image setImageTile(TileType tileType){
		Image img;
		if (tileType == TileType.WALL){
			img = new Image("wall.jpg");
		}else{
			img = new Image("floor.jpg");
		}
		return img;
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

    public void overlay() {
        transparent = true;
    }


	//Getters
	public int getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public TileType getTileType() {
		return tileType;
	}

    public boolean isTransparent() {
        return transparent;
    }

    public void render() {
        QuickView.renderBlock(this.img, x-1, y-1, tileType == TileType.WALL ? 1 : 2, transparent);
    }
}
