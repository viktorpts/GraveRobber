package Models;

public class Sprite {
    // TODO: Add a container and methods to load image frames from disk into memory and output them to a display interface
    int state;
    double progress;
    double framerate; // frames per second

    public Sprite (double framerate) {
        state = 0;
        progress = 0.0;
        this.framerate = framerate;
    }

    public void advance(double time) {
        progress += time / framerate;
    }

    public void setState(int state) {
        this.state = state;
    }
}
