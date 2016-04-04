package World;

import Models.Player;

/**
 * Contains the level geometry (grid), a tileset and a list of entity instances. One per game!
 */
public class Level {
    // Temp player, move this to entity list later
    Player player;

    public Level() {

    }
    public Level(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
