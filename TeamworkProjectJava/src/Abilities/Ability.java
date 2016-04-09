package Abilities;

import Enumerations.AbilityState;
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

}
