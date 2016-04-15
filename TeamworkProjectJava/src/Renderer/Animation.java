package Renderer;

import Enumerations.AnimationState;
import Enumerations.EntityState;
import Models.Entity;
import Models.Player;

public class Animation {
    // TODO: Add a container and methods to load image frames from disk into memory and output them to a display interface
    /**
     * class sprite
     * <p>
     * image - interface with ImageView (javafx)
     * <p>
     * arraylist name of sequences
     * for each sequence start index and end index
     * method for sequence length
     * <p>
     * x,y offset
     */
    AnimationState state;
    double progress;
    double framerate; // frames per second

    public Animation(double framerate) {
        state = AnimationState.IDLE;
        progress = 0.0;
        this.framerate = framerate;
        //this.framerate = 1; // todo /!\ change back when finished testing /!\
    }

    /**
     * Output directly to display interface
     */
    public void output(Entity sender, double x, double y, double direction) {
        // check type and state an output accordingly
        int selector = 0;
        if (sender.hasState(EntityState.DEAD)) selector = 4;
        if (sender instanceof Player) {
            selector = 1;
            if (state == AnimationState.IDLE) QuickView.renderSword(x, y, direction, 0.0);
        }
        if (state == AnimationState.ATTACKUP) {
            QuickView.renderSword(x, y, direction, progress);
            selector = 2;
        }
        if (state == AnimationState.ATTACKING) {
            // todo render swipe
            if (progress > 1) {
                QuickView.renderSwipe(x, y, direction, 1);
                QuickView.renderSword(x, y, direction, 4);
            } else {
                QuickView.renderSwipe(x, y, direction, progress);
                QuickView.renderSword(x, y, direction, progress + 3);
                selector = 2;
            }
        }
        if (state == AnimationState.ATTACKDOWN) {
            QuickView.renderSword(x, y, direction, progress + 4);
        }
        if (sender.getState().contains(EntityState.DAMAGED)) selector = 3;
        QuickView.renderSprite(selector, x, y, direction);
    }

    public void advance(double time) {
        progress += time * framerate;
        // decide if state change is needed
        switch (state) {
            // TODO: these times should be based on the actual length of the sequence of frames
            case ATTACKUP:
                // if enough time has passed, change to next state
                if (progress >= 3.0) {
                    progress = 0;
                    state = AnimationState.ATTACKING;
                }
                break;
            case ATTACKING:
                if (progress >= 3.0) {
                    progress = 0;
                    state = AnimationState.ATTACKDOWN;
                }
                break;
            case ATTACKDOWN:
                // if enough time has passed, change back to idle
                if (progress >= 3.0) {
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
    // for instance, attack has three sections - init (uncancelable, interruptable); resolution; wind down (cancelable)

    // note: might actually not be necessary, abilities can just get the state and decide if resolution has occurred

    /**
     * Get the length of animation sequence
     *
     * @param animation target sequence name
     * @return length of sequence in seconds
     */
    public double getLength(String animation) {
        return 0.0;
    }

    /**
     * Get the moment in time where resolution occurs
     *
     * @param animation target sequence name
     * @param index     ID of cue point, if more than one
     * @return moment of resolution since beginning of sequence
     */
    public double getCue(String animation, int index) {
        return 0.0;
    }

    public double getCue(String animation) {
        return 0.0;
    }
}
