package Renderer;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Frame {

    private final Image image;
    private final int offsetX;
    private final int offsetY;

    /**
     * Construct frame out of existing source
     * @param src Image
     * @param offsetX Origin x
     * @param offsetY Origin y
     */
    public Frame(Image src, int offsetX, int offsetY) {
        this.image = src;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Crop frame out of source image
     * @param src Source image ot be cropped
     * @param x Viewport left
     * @param y Viewport top
     * @param width Viewport width
     * @param height Viewport height
     * @param offsetX Origin x (based on cropped frame)
     * @param offsetY Origin y (based on cropped frame)
     */
    public Frame(Image src, int x, int y, int width, int height, int offsetX, int offsetY) {
        while (x + width >= src.getWidth()) width--;
        while (y + height >= src.getHeight()) height--;
        this.image = new WritableImage(src.getPixelReader(), x, y, width, height);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Image get() {
        return image;
    }

    public int getOX() {
        return offsetX;
    }

    public int getOY() {
        return offsetY;
    }

}
