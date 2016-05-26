package Models;

import Enumerations.EntityState;
import Enumerations.Items;

import java.util.*;

public class Inventory {

    List<Loot> contents;
    Set<Items> itemTypes;

    public Inventory() {
        contents = new ArrayList<>();
        itemTypes = new HashSet<>();
    }

    public void pickUp(Loot loot) {
        if (itemTypes.contains(loot.getType())) {
            if (loot instanceof Consumable) {
                Consumable owned = (Consumable) getItem(loot.getType());
                if (owned.getAmmo() < owned.getType().getMaxStack()) {
                    // Stack consumable items
                    owned.replenish(((Consumable) loot).getAmmo());
                    ((Consumable) loot).setAmmo(0);
                    loot.setState(EnumSet.of(EntityState.DESTROYED));
                }
            }
        } else {
            contents.add(loot);
            itemTypes.add(loot.getType());
            loot.pickUp();
        }
    }

    public Loot getItem(Items item) {
        for (Loot loot : contents) {
            if (loot.getType() == item) {
                return loot;
            }
        }
        return null;
    }

    public List<Loot> getContents() {
        return contents;
    }
}
