package Models;

import Enumerations.Abilities;
import Abilities.Ability;
import Abilities.MeleeAttack;
import Renderer.Animation;
import World.Coord;
import World.Physics;

import java.util.HashMap;

public class Player extends Creature {
    private Inventory inventory;

    // Create new player
    public Player(int startHealthPoints,
                  int startAttackPower,
                  int startArmorValue,
                  double x, double y) {
        super(new Animation(15),
                x, y, 0.0,
                startHealthPoints, startAttackPower, startArmorValue,
                new HashMap<Abilities, Ability>(),
                0.25, Physics.maxMoveSpeed, Physics.maxAcceleration);
        addAbility(Abilities.ATTACKPRIMARY, new MeleeAttack(this, startAttackPower, 1.0));
    }

    // TODO: Inventory management
}
