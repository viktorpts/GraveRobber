package Models;

import Enumerations.InventoryStatus;

public class Loot extends Entity {
        private InventoryStatus inventoryStatus;
        private int Cost;

    public Loot(double x, double y, InventoryStatus inventoryStatus, int cost) {
        super(x, y, false);//items not alive
        this.inventoryStatus = inventoryStatus;
        Cost = cost;
        super.setX(x);
        super.setY(y);
        super.setAlive(false);
    }
}
