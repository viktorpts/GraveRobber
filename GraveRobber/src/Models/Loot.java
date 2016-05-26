package Models;

import Enumerations.InventoryStatus;
import Enumerations.Items;
import Renderer.Animation;

abstract public class Loot extends Entity {
    private InventoryStatus inventoryStatus;
    protected int cost;
    protected Items type;

    public Loot(Animation animation, Items type, double x, double y, InventoryStatus inventoryStatus, int cost) {
        super(animation, x, y, 0.0, 0.25);
        this.type = type;
        this.inventoryStatus = inventoryStatus;
        this.cost = cost;
        super.setX(x);
        super.setY(y);
    }

    public void pickUp() {
        inventoryStatus = InventoryStatus.BACKPACK;
    }

    public void drop() {
        inventoryStatus = InventoryStatus.GROUND;
    }

    public Items getType() {
        return type;
    }

    @Override
    public void render() {
        if (inventoryStatus == InventoryStatus.GROUND)
            super.render();
    }
}
