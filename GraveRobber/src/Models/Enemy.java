package Models;

import AI.Behaviour;
import Enumerations.AIState;
import Enumerations.AbilityTypes;
import Enumerations.EntityState;
import Enumerations.Items;
import Factories.LootFactory;
import Game.Main;
import Renderer.Animation;
import Abilities.Ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Dedicated class that has a few extra things compared to it's inanimate parent, mainly AI.
 */
public class Enemy extends Creature {
    LootTable lootTable;
    ArrayList<Behaviour> brain;

    public Enemy(Animation animation, double x,
                 double y, double direction,
                 int healthPoints, int attackPower,
                 int armorValue, HashMap<AbilityTypes, Ability> abilities, double radius,
                 double maxSpeed, double maxAcceleration) {
        super(animation, x, y, direction, healthPoints, attackPower, armorValue, abilities, radius, maxSpeed, maxAcceleration);
        this.brain = new ArrayList<>();
    }

    /**
     * Add a new behaviour to the Enemy. Note the list sorted by priority each time.
     *
     * @param newBrain A unique Behaviour. This can't be shared, since it contains a reference to it's owner! OR, it can
     *                 be shared, or even point to another Enemy, for hilarious results.
     */
    public void addBrain(Behaviour newBrain) {
        brain.add(newBrain);
        newBrain.start();
        brain.sort((b1, b2) -> Integer.compare(b2.getWeight(), b1.getWeight()));
    }

    public AIState getThought(int index) {
        return brain.get(index).getState();
    }

    /**
     * Consult each Behaviour and act accordingly. Once an action is taken, the rest of the possible actions are
     * ignored, hence sorting them by priority earlier.
     *
     * @param time Seconds since last update
     */
    void processBehaviour(double time) {
        // using stream is overkill, since each creature wont have more than a handful of behaviours to cycle trough and
        // having the option to break the loop is better, considering each individual brain might have complex logic
        if (!hasState(EntityState.IDLE)) return;
        if (hasState(EntityState.DAMAGED)) return;
        for (Behaviour behaviour : brain) {
            if (behaviour.update(time)) break; // break the loop as soon as one brain makes a decision
        }
    }

    @Override
    public void update(double time) {
        super.update(time);

        // Process behaviour
        processBehaviour(time);
    }

    @Override
    protected void die() {
        super.die();

        // 10% chance to drop health potion
        Random rnd = new Random();
        if (rnd.nextDouble() > 0.90) Main.game.getLevel().getEntities().add(
                LootFactory.getConsumable(Items.POTIONHEALTH, getX(), getY(), 1)
        );

        // Placeholder end level condition
        if (this instanceof Enemy) {
            Main.game.getLevel().enemyCount--;
            if (Main.game.getLevel().enemyCount == 0) Main.game.getLevel().spawnBoss();
        }
    }

    // TODO: add methods to initialize loot table
}
