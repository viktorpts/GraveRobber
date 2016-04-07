package Enumerations;

public enum Abilities {
    ATTACKPRIMARY ("Primary Attack");
    private String name;

    private Abilities(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

