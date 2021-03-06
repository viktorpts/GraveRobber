package Factories;

import AI.Aggression;
import AI.Gank;
import AI.Roam;
import Abilities.Ability;
import Enumerations.AbilityTypes;
import Enumerations.EnemyTypes;
import Interfaces.IEnemyProducible;
import Interfaces.IPlayerProducible;
import Models.Enemy;
import Abilities.MeleeAttack;
import Abilities.ChargeAttack;

import java.util.HashMap;

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
                new HashMap<AbilityTypes, Ability>(),
                type.getRadius(),
                type.getMaxSpeed(),
                type.getMaxAcceleration()
        );
        thisEnemy.addBrain(new Roam(thisEnemy, 0.3)); // all enemies roam randomly
        switch (type) {
            case SKELETON: // skeletons are fearsome
                thisEnemy.addAbility(AbilityTypes.ATTACKPRIMARY, new MeleeAttack(thisEnemy, 0.75, type.getAttackPoints(), 0.6, 1.0));
                thisEnemy.addBrain(new Gank(thisEnemy, 5));
                thisEnemy.addBrain(new Aggression(thisEnemy, 0.4));
                break;
            case GIANT_RAT: // rats are fast but feeble
                thisEnemy.addAbility(AbilityTypes.ATTACKPRIMARY, new ChargeAttack(thisEnemy, 0.3, type.getAttackPoints(), 0.3, 2.0));
                thisEnemy.addBrain(new Gank(thisEnemy, 3));
                thisEnemy.addBrain(new Aggression(thisEnemy, 1.5));
                break;
            default:
                thisEnemy.addAbility(AbilityTypes.ATTACKPRIMARY, new MeleeAttack(thisEnemy, 0.75, type.getAttackPoints(), 0.6, 1.0));
                thisEnemy.addBrain(new Gank(thisEnemy, 3));
                break;
        }
        return thisEnemy;
    }

}
