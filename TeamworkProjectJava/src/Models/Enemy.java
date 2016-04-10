package Models;

import AI.Behaviour;
import AI.Roam;
import Enumerations.AIState;
import World.Coord;

import java.util.ArrayList;

public class Enemy extends Creature {
    LootTable lootTable;
    ArrayList<Behaviour> brain;

    public Enemy(int startHealthPoints, int startAttackPower, int startArmorValue, double x, double y) {
        super(startHealthPoints, startAttackPower, startArmorValue, new Coord(x, y));

        // Make creature roam around randomly
        brain = new ArrayList<>();
        brain.add(new Roam(this, 0.5));
        brain.get(0).start();
    }
    public void addBrain(Behaviour newBrain) {
        brain.add(newBrain);
        brain.sort((b1, b2) -> Integer.compare(b2.getWeight(), b1.getWeight()));
    }

    public AIState getThought(int index) {
        return brain.get(index).getState();
    }

    void processBehaviour(double time) {
        // using stream is overkill, since each creature wont have more than a handful of behaviours to cycle trough and
        // having the option to break the loop is better, considering each individual brain might have complex logic
        for (Behaviour behaviour : brain) {
            if (behaviour.update(time)) break; // break the loop as soon as one brain makes a decision
        }
    }

    // TODO: add methods to initialize loot table
}
