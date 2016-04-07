package Models;

import Abilities.Ability;
import Abilities.Attack;
import Enumerations.Abilities;
import Interfaces.IAbility;
import Interfaces.IMovable;
import World.Coord;
import World.Physics;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Creature extends Entity implements IMovable{
    private int healthPoints;
    private int attackPower;
    private int armorValue;
    Coord velocity;
    HashMap<Abilities, Ability> abilities;

    // TODO: Make this it's own object with more actions
    private int behaviour;
    private double behaviourState;

    public Creature(int startHealthPoints, int startAttackPower, int startArmorValue, Coord position) {
        super(position);
        this.setHealthPoints(startHealthPoints);
        this.setAttackPower(startAttackPower);
        this.setArmorValue(startArmorValue);
        velocity = new Coord(0.0, 0.0);

        abilities = new HashMap<>();
        abilities.put(Abilities.ATTACKPRIMARY, new Attack(this, 10.0, 0.5));

        behaviour = 0;
        behaviourState = 0;
    }
    public int getHealthPoints() {
        return healthPoints;
    }
    public void setHealthPoints(int value) {
        this.healthPoints = value;
    }
    public int getAttackPower() {
        return attackPower;
    }
    public void setAttackPower(int value) {
        this.attackPower = value;
    }
    public int getArmorValue() {
        return armorValue;
    }
    public void setArmorValue(int value) {
        this.armorValue = value;
    }

    @Override
    public void accelerate(Coord vector, double time) {
        vector.scale(time);
        velocity.add(vector);
        if (velocity.getMagnitude() > Physics.maxVelocity) {
            velocity.setMagnitude(Physics.maxVelocity);
        }
    }

    @Override
    public void stop() {
        velocity = new Coord(0, 0);
    }

    @Override
    public void place(Coord newPosition) {
        super.setPos(newPosition);
    }

    @Override
    public void place(double newX, double newY) {
        super.setPos(newX, newY);
    }

    @Override
    public boolean hitscan(Entity target) {
        // TODO: Implement collision detection
        return false;
    }

    @Override
    public Coord getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Coord newVelocity) {
        velocity = newVelocity;
    }

    public void setBehaviour(int behaviour) {
        this.behaviour = behaviour;
    }

    public void update(double time) {
        // Process behaviour, temp
        if (behaviour > 0) {
            behaviourState += time;
            if (behaviourState > 3) {
                behaviourState = 0;
                behaviour++;
                setDirection(Math.PI * 2 * (new Random().nextFloat()));
                Coord moveSome = new Coord(10.0, 0.0);
                moveSome.setDirection(getDirection());
                accelerate(moveSome, 0.5);
            }
        }

        // If the object is moving, apply friction
        if (velocity.getMagnitude() != 0) Physics.decelerate(velocity, time);
        double newX = super.getX() + velocity.getX() * time;
        double newY = super.getY() + velocity.getY() * time;
        super.setX(newX);
        super.setY(newY);

        // Cool down used abilities
        abilities.entrySet().stream()
                .filter(entry -> !entry.getValue().isReady()) // Filter used abilities
                .forEach(entry -> entry.getValue().cool(time));
    }

    public void addAbility(Ability ability) {

    }

    public void useAbility(Abilities ability) {
        if (!isReady()) return;
        if (abilities.containsKey(ability)) {
            abilities.get(ability).use();
        }
    }
    // TODO: Methods for taking damage and damage calculation
}
