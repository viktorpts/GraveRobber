package Abilities;

import Enumerations.Abilities;
import Enumerations.AbilityState;
import Enumerations.AnimationState;
import Models.Creature;

public class Defend extends Ability {

    private double health;
    private double currentHealth;

    public Defend(Creature owner, double health) {
        super(owner, 0); // raising the shield has no cooldown, it's health is what matters
        this.health = health;
        currentHealth = health;
    }

    @Override
    public void use() {
        state = AbilityState.ACTIVE;
        owner.changeAnimation(Abilities.DEFEND.getName());
    }

}
