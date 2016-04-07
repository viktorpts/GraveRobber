package Abilities;

import Interfaces.IAbility;
import Models.Creature;

abstract public class Ability implements IAbility {

    double remaining; // time to next use, seconds
    double cooldown; // time between uses, seconds
    boolean ready; // readiness
    Creature owner;

    public Ability (Creature owner, double cooldown) {
        this.owner = owner; // passed by reference, we don't want a copy
        this.cooldown = cooldown;
    }

    public boolean isReady() {
        return ready;
    }

    @Override
    public void spend() {
        ready = false;
        remaining = cooldown;
    }

    @Override
    public void cool(double time) {
        if (ready) return;
        remaining -= time;
        if (remaining <=0) reset();
    }

    @Override
    public void reset() {
        remaining = 0;
        ready = true;
    }

}
