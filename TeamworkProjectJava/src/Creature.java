public class Creature extends Entity{
    private int healthPoints;
    private int attackPower;
    private int armorValue;

    public Creature(int startHealthPoints, int startAttackPower, int startArmorValue, double x, double y, boolean isAlive) {
        super(x, y, isAlive);
        this.setHealthPoints(startHealthPoints);
        this.setAttackPower(startAttackPower);
        this.setArmorValue(startArmorValue);
        super.setX(x);
        super.setY(y);
        super.setAlive(isAlive);
    }
    public int getHealthPoints() {
        return healthPoints;
    }
    public void setHealthPoints(int value) {
        this.healthPoints = value;
    }
    public int getAttackPower() {
        return attackPower;
    }
    public void setAttackPower(int value) {
        this.attackPower = value;
    }
    public int getArmorValue() {
        return armorValue;
    }
    public void setArmorValue(int value) {
        this.armorValue = value;
    }

}
