package Factories;

import AI.Aggression;
import AI.Gank;
import AI.Roam;
import Abilities.Ability;
import Enumerations.Abilities;
import Enumerations.EnemyTypes;
import Interfaces.IEnemyProducible;
import Interfaces.IPlayerProducible;
import Models.Enemy;
import Abilities.MeleeAttack;
import Abilities.ChargeAttack;
import Renderer.Animation;

import java.util.HashMap;
import java.util.Objects;

public class CreatureFactory implements IEnemyProducible, IPlayerProducible {

    /**
     * Create an enemy of requested type, using a template, and place them at the desired location
     *
     * @param type      Predetermined from list of templates
     * @param x         Horizontal position, world coordinates (right positive)
     * @param y         Vertical position, world coordinates (bottom positive)
     * @param direction Facing angle, radians
     * @return A unique enemy, place this in the entity list of the level
     */
    public static Enemy createEnemy(EnemyTypes type, double x, double y, double direction) {
        Enemy thisEnemy = new Enemy(
                AnimationFactory.getAnimation(type.name()), x, y, direction,
                type.getHealthPoints(),
                type.getAttackPoints(),
                type.getArmorValue(),
                new HashMap<Abilities, Ability>(),
                type.getRadius(),
                type.getMaxSpeed(),
                type.getMaxAcceleration()
        );
        thisEnemy.addBrain(new Roam(thisEnemy, 0.3)); // all enemies roam randomly
        switch (type) {
            case SKELETON: // skeletons are fearsome
                thisEnemy.addAbility(Abilities.ATTACKPRIMARY, new MeleeAttack(thisEnemy, 0.75, type.getAttackPoints(), 0.4));
                thisEnemy.addBrain(new Gank(thisEnemy, 5));
                thisEnemy.addBrain(new Aggression(thisEnemy, 0.75));
                break;
            case GIANT_RAT: // rats are fast but feeble
                thisEnemy.addAbility(Abilities.ATTACKPRIMARY, new ChargeAttack(thisEnemy, 0.3, type.getAttackPoints(), 0.2, 2.0));
                thisEnemy.addBrain(new Gank(thisEnemy, 3));
                thisEnemy.addBrain(new Aggression(thisEnemy, 1.0));
                break;
            default:
                thisEnemy.addAbility(Abilities.ATTACKPRIMARY, new MeleeAttack(thisEnemy, 0.75, type.getAttackPoints(), 0.4));
                thisEnemy.addBrain(new Gank(thisEnemy, 3));
                break;
        }
        return thisEnemy;
    }

}
