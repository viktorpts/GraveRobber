package Models;

import Abilities.Ability;
import Abilities.Attack;
import Enumerations.Abilities;
import Game.Main;
import Interfaces.IMovable;
import Renderer.Animation;
import World.Coord;
import World.Physics;

import java.util.HashMap;

public class Creature extends Entity implements IMovable{
    // Stats
    private int healthPoints;
    private int attackPower;
    private int armorValue;
    HashMap<Abilities, Ability> abilities;

    // Physical characteristics
    private double radius; // used for collision detection
    private double maxSpeed;
    private double maxAcceleration;
    private Coord velocity; // current velocity vector


    // Constructor
    public Creature(int startHealthPoints, int startAttackPower, int startArmorValue, Coord position) {
        // Init parent
        super(new Animation(0.0), position.getX(), position.getY(), 0.0);
        // Init stats
        this.setHealthPoints(startHealthPoints);
        this.setAttackPower(startAttackPower);
        this.setArmorValue(startArmorValue);
        abilities = new HashMap<>();
        abilities.put(Abilities.ATTACKPRIMARY, new Attack(this, 10.0, 0.5));

        // Init physical characteristics
        velocity = new Coord(0.0, 0.0);
        // TODO: add a way for these to be set at production time; not a good idea to change them directly though
        radius = 0.25;
        maxSpeed = Physics.maxMoveSpeed; // we use the properties of the player for now
        maxAcceleration = Physics.playerAcceleration;
    }

    // Properties
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

    // Implementation of IMovable, everything to do with motion
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
        Coord dist = new Coord(getX(), getY());
        dist.subtract(target.getPos());
        double penetration = dist.getMagnitude() - (radius + target.getRadius()); // TODO: Replace this with entity size
        if (penetration < 0.0) {
            // collision; resolve via projection (entities placed apart, no vector modification)
            Main.debugInfo += String.format("%ncollision");
            dist.setMagnitude(penetration / 2); // separation vector
            getPos().subtract(dist);
            dist.scale(-1); // push target entity in opposite direction
            target.getPos().subtract(dist);
            // TODO: modify each entity's velocity vector, so they aren't moving towards each other
            return true;
        }
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

    /**
     * Update the creature, depending on time elapsed since last update. Process behaviour, if entity has an AI
     * attached, look for collisions with other objects, move physically, cool down all used abilites, etc.
     * @param time Seconds since last update
     */
    public void update(double time) {
        // Process behaviour
        if (this instanceof Enemy) {
            ((Enemy)this).processBehaviour(time);
        }

        // Detect collisions
        // TODO: this will check each pair twice, make a separate list and deplete it
        Main.game.getLevel().getEntities().stream()
                .filter(entity -> entity instanceof Creature) // get just the creatures
                .filter(entity -> !entity.equals(this)) // can't collide with self
                .forEach(entity -> ((Creature)entity).hitscan(this)); // resolution currently included in detection, can be filtered further

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

    // Abilities

    /**
     * Add an ability to the list
     * @param name A name from the dedicated enumeration
     * @param ability An instance of the ability. Do not attach the same reference to two different creatures, since
     *                cooldown and effects are linked back to the creature
     */
    public void addAbility(Abilities name, Ability ability) {
        abilities.put(name, ability);
    }

    public void useAbility(Abilities ability) {
        if (!isReady()) return;
        if (abilities.containsKey(ability)) {
            if (!abilities.get(ability).isReady()) return;
            abilities.get(ability).use();
        }
    }
    // TODO: Methods for taking damage and damage calculation
}
