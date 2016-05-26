package Enumerations;

public enum AbilityTypes {
    ATTACKPRIMARY ("Primary Attack"),
    DASH ("Dash"),
    DEFEND ("Defend"),
    HEAL ("Heal");

    private String name;

    private AbilityTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

