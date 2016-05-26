package Models;

import Enumerations.InventoryStatus;
import Enumerations.Items;
import Renderer.Animation;

public class Consumable extends Loot {
    private int ammo;
    private double duration;

    public Consumable(Animation animation, Items type, double x, double y, InventoryStatus inventoryStatus, int cost, int ammo, double duration) {
        super(animation, type, x, y, inventoryStatus, cost);
        this.ammo = ammo;
        this.duration = duration;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void replenish(int newAmmo) {
        ammo += newAmmo;
        if (ammo > type.getMaxStack())
            ammo = type.getMaxStack();
    }

    public void use() {
        if (ammo > 0) {
            ammo--;
        }
    }
}
