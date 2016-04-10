package AI;

import Enumerations.AIState;
import Interfaces.IBehaviour;
import Models.Creature;

/**
 * Base class for all AI behaviour
 */
abstract public class Behaviour implements IBehaviour {

    protected final Creature owner; // reference to creature affected
    protected AIState state;
    protected double elapsedTime; // time since behaviour started, seconds
    protected int weight; // priority, if multiple behaviours are in conflict; can be used to sort while streaming

    public Behaviour(Creature owner, int weight) {
        this.owner = owner; // only place to set this, owner never changes
        state = AIState.STOPPED;
        elapsedTime = 0;
        this. weight = weight;
    }

    @Override
    public void start() {
        state = AIState.THINKING;
    }

    @Override
    public void pause() {
        state = AIState.PAUSED;
    }

    @Override
    public void stop() {
        state = AIState.STOPPED;
        elapsedTime = 0;
    }

    @Override
    public void reset() {
        elapsedTime = 0;
    }

    @Override
    public boolean update(double time) {
        elapsedTime += time;
        return think(time);
    }

    public AIState getState() {
        return state;
    }

    public int getWeight() {
        return weight;
    }
}
