package Enumerations;

import Abilities.Ability;

import java.util.HashMap;

public enum EnemyTypes {
    GIANT_RAT(50, 5, 0,
            new HashMap<AbilityTypes, Ability>(),
            0.25, 3, 10),
    SLIME(150, 15, 0,
            new HashMap<AbilityTypes, Ability>(),
            0.25, 3.5, 7),
    ZOMBIE(200, 10, 5,
            new HashMap<AbilityTypes, Ability>(),
            0.25, 4, 8),
    SKELETON(100, 15, 5,
            new HashMap<AbilityTypes, Ability>(),
            0.35, 3, 5);

    private int healthPoints;
    private int attackPoints;
    private int armorValue;
    private HashMap<AbilityTypes, Ability> abilities;
    private double radius;
    private double maxSpeed;
    private double maxAcceleration;

    EnemyTypes(int healthPoints, int attackPoints, int armorValue, HashMap<AbilityTypes, Ability> abilities, double radius, double maxSpeed, double maxAcceleration) {
        this.healthPoints = healthPoints;
        this.attackPoints = attackPoints;
        this.armorValue = armorValue;
        this.abilities = abilities;
        this.radius = radius;
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getArmorValue() {
        return armorValue;
    }

    public HashMap<AbilityTypes, Ability> getAbilities() {
        return abilities;
    }

    public double getRadius() {
        return radius;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMaxAcceleration() {
        return maxAcceleration;
    }
}
