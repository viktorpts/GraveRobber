package Renderer;

import Renderer.QuickView;

public class Animation {
    // TODO: Add a container and methods to load image frames from disk into memory and output them to a display interface
    int state;
    double progress;
    double framerate; // frames per second

    public Animation(double framerate) {
        state = 0;
        progress = 0.0;
        this.framerate = framerate;
    }

    /**
     * Output directly todisplay interface
     */
    public void output(int selector, double x, double y, double direction) {
        // check state an output accordingly
        QuickView.renderSprite(selector, x, y, direction);
    }

    public void advance(double time) {
        progress += time / framerate;
    }

    public void setState(int state) {
        this.state = state;
    }

    // TODO: a method that returns the length of animation and special cue points to ability callers
    // for instance, attack has three sections - init (unchangeable, interruptable); resolution; wind down (changeable)

    /**
     * Get the length of animation sequence
     * @param animation target sequence name
     * @return length of sequence in seconds
     */
    public double getLength(String animation) {
        return 0.0;
    }

    /**
     * Get the moment in time where resolution occurs
     * @param animation target sequence name
     * @param index ID of cue point, if more than one
     * @return moment of resolution since beginning of sequence
     */
    public double getCue(String animation, int index) {
        return 0.0;
    }
    public double getCue(String animation) {
        return 0.0;
    }
}
