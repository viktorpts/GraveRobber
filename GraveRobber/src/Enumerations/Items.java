package Enumerations;

public enum Items {
    POTIONHEALTH(3, "HealthPotion", 100, 0);

    private int maxStack;
    private String spriteName;
    private int cost;
    private double duration;

    Items(int maxStack, String spriteName, int cost, double duration) {
        this.maxStack = maxStack;
        this.spriteName = spriteName;
        this.cost = cost;
        this.duration = duration;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public int getCost() {
        return cost;
    }

    public double getDuration() {
        return duration;
    }
}
