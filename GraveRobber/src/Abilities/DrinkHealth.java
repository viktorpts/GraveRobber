package Abilities;

import Enumerations.Items;
import Models.Consumable;
import Models.Creature;
import Models.Player;

public class DrinkHealth extends Ability {

    public DrinkHealth(Creature owner, double cooldown) {
        super(owner, cooldown, 0.0);
    }

    @Override
    public void use() {
        spend();
        resolve();
    }

    @Override
    public void resolve() {
        ((Consumable) ((Player) owner).getInventory().getItem(Items.POTIONHEALTH)).use();
        owner.modifyHealth(25);
    }

    @Override
    public boolean isReady() {
        return super.isReady() &&
                ((Consumable) ((Player) owner).getInventory().getItem(Items.POTIONHEALTH)).getAmmo() > 0;
    }

}
