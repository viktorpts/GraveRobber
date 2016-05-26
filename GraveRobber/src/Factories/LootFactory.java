package Factories;

import Enumerations.InventoryStatus;
import Enumerations.Items;
import Models.Consumable;

public class LootFactory {

    public static Consumable getConsumable(Items type, double x, double y, int ammo) {
        return new Consumable(AnimationFactory.getAnimation(type.getSpriteName()),
                type,
                x, y, InventoryStatus.GROUND,
                type.getCost(), ammo, type.getDuration());
    }

}
