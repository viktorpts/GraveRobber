package Models;

import Enumerations.InventoryStatus;

abstract public class Loot extends Entity {
        private InventoryStatus inventoryStatus;
        private int Cost;

    public Loot(double x, double y, InventoryStatus inventoryStatus, int cost) {
        super(new Sprite(0.0), x, y, 0.0);
        this.inventoryStatus = inventoryStatus;
        Cost = cost;
        super.setX(x);
        super.setY(y);
    }
}
