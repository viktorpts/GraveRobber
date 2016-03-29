public class Loot extends Entity {
        private CurrentStatus currentStatus;
        private int Cost;

    public Loot(double x, double y, CurrentStatus currentStatus, int cost) {
        super(x, y, false);//items not alive
        this.currentStatus = currentStatus;
        Cost = cost;
        super.setX(x);
        super.setY(y);
        super.setAlive(false);
    }
}
