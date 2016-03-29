import java.util.ArrayList;
import java.util.List;

public class Player extends Creature {

    private List<Loot> inventory;

    public Player(int startHealthPoints, int startAttackPower, int startArmorValue, double x, double y, boolean isAlive) {
        super(startHealthPoints, startAttackPower, startArmorValue, x, y, isAlive);
        setInventory(new ArrayList<>());
    }

    public void setInventory(List<Loot> inventory) {
        this.inventory = inventory;
    }

    public List<Loot> getInventory() {

        return inventory;
    }
}
