package Abilities;

import Enumerations.*;
import Game.Main;
import Models.Creature;
import Models.Player;
import World.Coord;

import java.util.EnumSet;

/**
 * Note that attacks don't actually have a cooldown, since another attack cannot be initiated before the first one has
 * resolved, but we use the cooldown to disable attacking if attack was cancelled (by staggering or other effects)
 */
public class Attack extends Ability {

    double damage;
    double radius;
    Coord target;
    double elapsedTime;

    // TODO: set cool down from constructor
    public Attack(Creature owner, double damage, double radius) {
        super(owner, 1.0);
        this.damage = damage;
        this.radius = radius;
        this.target = new Coord(0.0, 0.0);
        elapsedTime = 0;
    }

    /**
     * TODO: find a way to set target directly with usage; the problem is we want the target some times to be a location
     * and other times to be an Entity, or even no target
     */
    public void setTarget(Coord target) {
        this.target.setPos(target.getPos());
    }

    @Override
    public void use() {
        spend();
        elapsedTime = 0;
        state = AbilityState.CASTINGUP;
        owner.stop();
        owner.changeAnimation(Abilities.ATTACKPRIMARY.getName());
        owner.setState(EnumSet.of(EntityState.CASTINGINIT));
    }

    @Override
    public void update(double time) {
        if (isReady()) return;
        if (state == AbilityState.COOLING) {
            cool(time);
            return;
        }
        if (!vrfyState()) {
            // entity state has changed from external source, cancel attack
            state = AbilityState.COOLING;
            return;
        }
        elapsedTime += time;
        switch (state) {
            case CASTINGUP: // attack wind up, can't be cancelled, can be interrupted by staggering effects
                if (owner.getAnimationState() != AnimationState.ATTACKINGINIT) {
                    // animation state has advanced, resolve damage
                    state = AbilityState.CASTINGDOWN; // move to next state
                    owner.setState(EnumSet.of(EntityState.CASTING)); // from this point on, animation can be canceled
                    // TODO: damage resolution
                    // apply to everyone within range 1, for testing
                    Main.game.getLevel().getEntities().stream()
                            .filter(entity -> entity instanceof Creature)
                            .filter(entity -> Coord.subtract(entity.getPos(), owner.getPos()).getMagnitude() <= 1.0)
                            .forEach(entity -> {
                                if (entity == owner) return; // don't knock ourselves back, duh
                                Creature current = (Creature)entity;
                                current.takeDamage(10, DamageType.WEAPONMELEE, owner.getPos());
                            });
                }
                break;
            case CASTINGDOWN: // attack wind down, can be cancelled by other abilities (chaining attacks, dodging, etc.)
                if (owner.getAnimationState() != AnimationState.ATTACKING) {
                    // animation state has advanced, go back to ready
                    remaining = 0;
                    state = AbilityState.READY;
                    owner.setState(EnumSet.of(EntityState.IDLE));
                }
                break;
        }
    }
}
