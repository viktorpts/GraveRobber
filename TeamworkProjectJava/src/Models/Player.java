package Models;

import Enumerations.Abilities;
import Abilities.Ability;
import Abilities.Dash;
import Abilities.MeleeAttack;
import Renderer.Animation;
import World.Coord;
import World.Physics;

import java.util.HashMap;

/**
 * Dedicated class to differentiate the Player character from his inanimate parent class and his sibling Enemy
 * creatures. Doesn't actually do much, since we don't have an inventory yet, but at least is very useful for filtering
 * Entity streams. Note Player framerate is considerably higher than everything else, to make controls feel responsive.
 */
public class Player extends Creature {
    private Inventory inventory;

    // Create new player
    public Player(int startHealthPoints,
                  int startAttackPower,
                  int startArmorValue,
                  double x, double y) {
        super(new Animation(15, "Player"),
                x, y, 0.0,
                startHealthPoints, startAttackPower, startArmorValue,
                new HashMap<Abilities, Ability>(),
                0.25, Physics.maxMoveSpeed, Physics.maxAcceleration);
        addAbility(Abilities.ATTACKPRIMARY, new MeleeAttack(this, startAttackPower, 1.0));
        addAbility(Abilities.DASH, new Dash(this, 5, 12));
    }

    // TODO: Inventory management
}
