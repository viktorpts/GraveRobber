package Abilities;

import Models.Creature;

/**
 * Quickly move a short distance in the direction of movement. If not moving, wait for some movement.
 */
public class Dash extends Ability {

    private double force;

    public Dash(Creature owner, double cooldown, double force) {
        super(owner, cooldown, 0.0); // instant resolution
        this.force = force;
    }

    @Override
    public void use() {
        spend();
        resolve();
    }

    @Override
    public void resolve() {
        if (owner.getVelocity().getMagnitude() == 0) {
            owner.moveForward(1.0); // if not moving, dash forward
        }
        owner.getVelocity().setMagnitude(force);
    }

}
