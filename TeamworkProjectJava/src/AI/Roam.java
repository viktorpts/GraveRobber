package AI;

import Enumerations.AIState;
import Models.Creature;
import World.Coord;
import World.Physics;

import java.util.Random;

/**
 * Random wandering about the maze, not keeping track of where the creature started. Passive.
 */
public class Roam extends Behaviour {

    private double nextUpdate; // when do we need to move again
    private double frequency; // how often movement occurs, actions per second
    private double progress; // how much is left of the operation, count down to zero
    private Random rnd;

    public Roam(Creature owner, double frequency) {
        super(owner, 1);
        this.frequency = frequency;
        progress = 0;
        rnd = new Random();
        prepareNext();
    }

    @Override
    public void think(double time) {
        if (state == AIState.PROCESSING) {
            progress -= time;
            if (progress <= 0) { // if action is complete, go back to idle
                progress = 0;
                state = AIState.GOING;
                reset();
            }
            act(time);
        } else if (elapsedTime > nextUpdate) { // if not already processing action, wait for trigger (time)
            // Take action
            // Decide whether to look around or to move forward
            if (rnd.nextInt(2) == 1) { // move
                state = AIState.PROCESSING;
                progress = 0.25 + rnd.nextDouble() / 2; // 0.25-0.75 seconds of movement
            } else { // look around
                owner.setDirection(rnd.nextDouble() * Math.PI * 2);
                prepareNext();
                reset();
            }
        }
    }

    private void act(double time) {
        // move forward
        Coord moveSome = new Coord(Physics.playerAcceleration * 0.92, 0.0); // 50% of net player acceleration
        // WARNING! entities are not bound by Physic.maxMoveSpeed! don't accelerate them for too long
        moveSome.setDirection(owner.getDirection());
        owner.accelerate(moveSome, time);
    }

    private void prepareNext() {
        nextUpdate = 3 + rnd.nextDouble() / this.frequency * 0.4; // 3-5 seconds
    }

}
