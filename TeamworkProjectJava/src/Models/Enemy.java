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

    public AIState getThought(int index) {
        return brain.get(index).getState();
    }

    // TODO: add methods to initialize loot table
    void processBehaviour(double time) {
        // TODO: sort by weight
        brain.stream().filter(behaviour -> behaviour.getState() == AIState.GOING || behaviour.getState() == AIState.PROCESSING)
                .forEach(behaviour -> behaviour.update(time));
    }
}
