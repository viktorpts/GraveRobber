package Models;

import Enumerations.CurrentStatus;

public class Consumable extends Loot {
    private int ammo;
    private int duration;

    public Consumable(double x, double y, CurrentStatus currentStatus, int cost, int ammo, int duration) {
        super(x, y, currentStatus, cost);
        this.ammo = ammo;
        this.duration = duration;
    }
}
