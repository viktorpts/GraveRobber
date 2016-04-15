package AI;

import Enumerations.AIState;
import Enumerations.Abilities;
import Enumerations.EntityState;
import Game.Main;
import Models.Creature;
import Models.Enemy;
import World.Coord;

import java.util.Random;

public class Aggression extends Behaviour {

    private double range; // attack reach

    public Aggression(Creature owner, double range) {
        super(owner, 10);
        this.range = range;
    }

    @Override
    public boolean think(double time) {
        if (Main.game.getPlayer().getState().contains(EntityState.DEAD)) return false;
        if (Coord.subtract(Game.Main.game.getPlayer().getPos(), owner.getPos()).getMagnitude() <= range) {
            // face player
            owner.turnTo(Game.Main.game.getPlayer().getX(), Game.Main.game.getPlayer().getY());
            owner.useAbility(Abilities.ATTACKPRIMARY);
            return true;
        }
        return false;
    }

}
