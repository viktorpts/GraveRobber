package Models;

import Enumerations.InventoryStatus;
import Enumerations.Items;
import Renderer.Animation;

public class Item extends Loot {
    public Item(Animation animation, Items type, double x, double y, InventoryStatus inventoryStatus, int cost) {
        super(animation, type, x, y, inventoryStatus, cost);
    }
}
