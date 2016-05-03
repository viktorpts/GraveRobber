package Renderer;

import Enumerations.AnimationState;
import Enumerations.EntityState;
import Enumerations.Sequences;
import Game.Main;
import Models.Creature;
import Models.Entity;

public class Animation {

    private Sprite sprite;
    private Sequences state;
    private Sequence currentSequence;
    private Frame currentFrame;
    private double currentDirection;
    private boolean looping = true;
    private double progress;
    private int currentIndex;
    private double framerate; // frames per second

    public Animation(Sprite sprite, double startingDirection) {
        this.sprite = sprite;
        state = Sequences.IDLE;
        currentDirection = startingDirection;
        progress = 0;
        currentIndex = 0;
        framerate = sprite.getFramerate();
        //this.framerate = 1; // todo /!\ change back when finished testing /!\

        updateSequence();
        updateFrame();
    }

    public void update(double time, double direction) {
        progress += time * framerate;
        currentDirection = direction;
        // See if sequence or direction has changed
        if (!vrfySequence()) updateSequence();
        // Loop
        if (progress >= currentSequence.length()) {
            if (!looping) {
                reset();
            } else if (state != Sequences.DIE) {
                progress = 0;
                currentIndex = 0;
            } else {
                progress = currentSequence.length() - 1;
                currentIndex = (int) progress;
            }
        }
        // Change frame
        if ((int) progress > currentIndex) {
            currentIndex++;
            updateFrame();
        }
        // Debug info
        Main.debugInfo += String.format("Progress: %.2f%n", progress);
        Main.debugInfo += String.format("Index: %d", currentIndex);
    }

    public void render(double x, double y) {
        Main.game.getGc().drawImage(currentFrame.get(),
                QuickView.toCanvasX(x) - currentFrame.getOX(),
                QuickView.toCanvasY(y) - currentFrame.getOY());
    }

    private void updateSequence() {
        currentSequence = sprite.getSequence(state, currentDirection);
    }

    private void updateFrame() {
        currentFrame = currentSequence.get(currentIndex);
    }

    private boolean vrfySequence() {
        if (currentSequence.getName() != state ||
                !currentSequence.isDir(currentDirection))
            return false;
        return true;
    }

    /**
     * Start animation sequence
     *
     * @param name Name of sequence ot be played
     * @param loop True for looping animation. If set to false, at end of animation, IDLE will be played
     */
    public void play(Sequences name, boolean loop) {
        state = name;
        looping = loop;
        progress = 0;
        currentIndex = 0;
        updateSequence();
        updateFrame();
    }

    public void reset() {
        state = Sequences.IDLE;
        looping = true;
        progress = 0;
        currentIndex = 0;
        updateSequence();
        updateFrame();
    }

    public Sequences getState() {
        return state;
    }

    public boolean ended() {
        return currentIndex >= currentSequence.length() - 1;
    }

}
