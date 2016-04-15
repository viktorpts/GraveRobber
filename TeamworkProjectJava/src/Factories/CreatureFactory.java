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
import Abilities.MeleeAttack;

import java.util.HashMap;
import java.util.Objects;

public class CreatureFactory implements IEnemyProducible, IPlayerProducible {

    static Enemy GiantRat = createEnemy(EnemyTypes.GIANT_RAT, 0, 0, 0);
    static Enemy Slime = createEnemy(EnemyTypes.SLIME, 0, 0, 0);
    static Enemy Zombie = createEnemy(EnemyTypes.ZOMBIE, 0, 0, 0);
    static HashMap<EnemyTypes, Enemy> enemies = new HashMap<EnemyTypes, Enemy>(); //HashMap with all enemies

    public static void init() { //Initialize HashMap with all enemies
        enemies.put(EnemyTypes.GIANT_RAT, GiantRat);
        enemies.put(EnemyTypes.SLIME, Slime);
        enemies.put(EnemyTypes.ZOMBIE, Zombie);
    }

    public static Enemy createEnemy(EnemyTypes type, double x, double y, double direction) {
        Enemy thisEnemy = new Enemy(
                type.getAnimation(), x, y, direction,
                type.getHealthPoints(),
                type.getAttackPoints(),
                type.getArmorValue(),
                type.getAbilities(),
                type.getRadius(),
                type.getMaxSpeed(),
                type.getMaxAcceleration()
        );
        thisEnemy.addAbility(Abilities.ATTACKPRIMARY, new MeleeAttack(thisEnemy, type.getAttackPoints(), type.getRadius()));
        thisEnemy.addBrain(new Roam(thisEnemy, 0.3));
        thisEnemy.addBrain(new Gank(thisEnemy, 3));
        return thisEnemy;
    }

    public static Enemy spawnEnemy(EnemyTypes type, double x, double y, double direction) {
        Enemy enemyToSpawn = enemies.getOrDefault(type, null);
        enemyToSpawn.setPos(x, y);
        enemyToSpawn.setDirection(direction);
        return enemyToSpawn;
    }
}
