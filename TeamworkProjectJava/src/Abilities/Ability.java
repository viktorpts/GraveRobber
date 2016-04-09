package Abilities;

import Enumerations.AbilityState;
import Enumerations.EntityState;
import Interfaces.IAbility;
import Models.Creature;

abstract public class Ability implements IAbility {

    double remaining; // time to next use, seconds
    double cooldown; // time between uses, seconds
    AbilityState state; // state
    Creature owner;

    public Ability (Creature owner, double cooldown) {
        this.owner = owner; // passed by reference, we don't want a copy
        this.cooldown = cooldown;
    }

    public AbilityState getState() {
        return state;
    }

    public boolean isReady() {
        return state == AbilityState.READY;
    }

    @Override
    public void spend() {
        state = AbilityState.COOLING;
        remaining = cooldown;
    }

    /**
     * Some abilities do not have casting time or resolution point - those wont need an implementation of update(), so
     * we have to make sure they are cooled down in the general case
     * @param time Seconds since last update
     */
    @Override
    public void update(double time) {
        if (!isReady()) cool(time);
        if (!vrfyState()) {
            // if ability was initiated, put it in cool down mode, cancel further resolution
            if (state == AbilityState.CASTINGUP || state == AbilityState.CASTINGDOWN) state = AbilityState.COOLING;
        }
    }

    @Override
    public void cool(double time) {
        if (isReady()) return;
        remaining -= time;
        if (remaining <=0) reset();
    }

    @Override
    public void reset() {
        remaining = 0;
        state = AbilityState.READY;
    }

    /**
     * Check if entity has not cancelled the ability, for instance, by getting staggered or destroyed. This check MUST
     * occur in all abilities that have casting time and resolution point - if entity state has changed and ability is
     * not ready, set it to cooling, so further updates are cancelled.
     * @return Result of check, true when still committed
     */
    boolean vrfyState() {
        if (!owner.getState().contains(EntityState.CASTINGINIT) && !owner.getState().contains(EntityState.CASTING)) {
            // entity state has changed from external source, cancel further updates
            state = AbilityState.COOLING;
            return false;
        }
        return true;
    }

}
