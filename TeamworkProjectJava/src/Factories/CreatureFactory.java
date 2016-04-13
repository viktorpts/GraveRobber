package Factories;

import AI.Gank;
import AI.Roam;
import Abilities.Ability;
import Enumerations.Abilities;
import Enumerations.EnemyTypes;
import Interfaces.IEnemyProducible;
import Interfaces.IPlayerProducible;
import Models.Enemy;
import Models.Entity;
import Abilities.Attack;

import java.util.HashMap;

public class CreatureFactory implements IEnemyProducible, IPlayerProducible {
    public static Enemy createEnemy(EnemyTypes type, double x, double y, double direction)
    {
        Enemy thisEnemy = new Enemy(
                type.getAnimation(),x,y ,direction,
                type.getHealthPoints(),
                type.getAttackPoints(),
                type.getArmorValue(),
                type.getAbilities(),
                type.getRadius(),
                type.getMaxSpeed(),
                type.getMaxAcceleration()
        );
        thisEnemy.addAbility(Abilities.ATTACKPRIMARY, new Attack(thisEnemy, type.getAttackPoints(),type.getRadius()));
        thisEnemy.addBrain(new Roam(thisEnemy, 0.3));
        thisEnemy.addBrain(new Gank(thisEnemy, 3));
        return thisEnemy;
    }
}
