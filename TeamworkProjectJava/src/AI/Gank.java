package AI;

import Enumerations.AIState;
import Models.Creature;
import World.Coord;

import java.util.Random;

/**
 * Causes affected creatures to gang up on the player, if within range. Passive (for now).
 */
public class Gank extends Behaviour {

    private double nextUpdate; // when do we need to move again, seconds
    private double range; // how often movement occurs, actions per second
    private double progress; // how much is left of the operation, count down to zero seconds
    private Random rnd;

    public Gank(Creature owner, double range) {
        super(owner, 5);
        this.range = range;
        progress = 0;
        rnd = new Random();
        prepareNext();
    }

    @Override
    public boolean think(double time) {
        if (Coord.subtract(Game.Main.game.getPlayer().getPos(), owner.getPos()).getMagnitude() <= range) {
            // face player
            owner.turnTo(Game.Main.game.getPlayer().getX(), Game.Main.game.getPlayer().getY());
            if (state == AIState.PROCESSING) {
                progress -= time;
                if (progress <= 0) { // if action is complete, go back to idle
                    progress = 0;
                    state = AIState.THINKING;
                    reset();
                    return false;
                }
                act(time);
                return true;
            } else if (elapsedTime > nextUpdate) { // if not already processing action, wait for trigger (time)
                state = AIState.PROCESSING;
                progress = 0.25 + rnd.nextDouble() / 2; // 0.25-0.75 seconds of movement
                prepareNext();
                return true;
            }
        }
        return false;
    }

    private void act(double time) {
        // move forward
        owner.moveForward(time);
    }

    private void prepareNext() {
        nextUpdate = rnd.nextDouble() / 3; // 0.0-0.33 seconds
    }

}
