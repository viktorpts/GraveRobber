package Renderer;

import Enumerations.AnimationState;
import Enumerations.EntityState;
import Models.Entity;
import Models.Player;

public class Animation {
    // TODO: Add a container and methods to load image frames from disk into memory and output them to a display interface
    /**
     * class sprite
     *
     * image - interface with ImageView (javafx)
     *
     * arraylist name of sequences
     * for each sequence start index and end index
     * method for sequence length
     *
     * x,y offset
     *
     */
    AnimationState state;
    double progress;
    double framerate; // frames per second

    public Animation(double framerate) {
        state = AnimationState.IDLE;
        progress = 0.0;
        this.framerate = framerate;
    }

    /**
     * Output directly to display interface
     */
    public void output(Entity sender, double x, double y, double direction) {
        // check type and state an output accordingly
        int selector = 0;
        if (sender instanceof Player) {
            selector = 1;
            if (state == AnimationState.IDLE) QuickView.renderSword(x, y, direction, 0.0);
        }
        if (state == AnimationState.ATTACKINGINIT) {
            // render swipe
            QuickView.renderSword(x, y, direction, progress);
            //QuickView.renderSwipe(x, y, direction, (progress * Math.PI / 6) - Math.PI / 4);
            selector = 2;
        }
        if (state == AnimationState.ATTACKING) {
            // render swipe
            QuickView.renderSword(x, y, direction, progress + 3);
            //QuickView.renderSwipe(x, y, direction, Math.PI / 4 - (progress * Math.PI / 10));
        }
        if (sender.getState().contains(EntityState.DAMAGED)) selector = 3;
        QuickView.renderSprite(selector, x, y, direction);
    }

    public void advance(double time) {
        progress += time * framerate;
        // decide if state change is needed
        switch (state) {
            // TODO: these times should be based on the actual length of the sequence of frames
            case ATTACKINGINIT:
                // if enough time has passed, change to next state
                if (progress >= 3.0) {
                    progress = 0;
                    state = AnimationState.ATTACKING;
                }
                break;
            case ATTACKING:
                // if enough time has passed, change back to idle
                if (progress >= 2.0) {
                    progress = 0;
                    state = AnimationState.IDLE;
                }
                break;
            // TODO: other cases
        }
    }

    public void setState(AnimationState state) {
        this.state = state;
        progress = 0;
    }
    public AnimationState getState() {
        return state;
    }

    // TODO: a method that returns the length of animation and special cue points to ability callers
    // for instance, attack has three sections - init (unchangeable, interruptable); resolution; wind down (changeable)

    // note: might actually not be necessary, abilities can just get the state and decide if resolution has occurred
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
