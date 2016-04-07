package World;

import Models.Entity;
import Models.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the level geometry (grid), a tileset and a list of entity instances. One per game!
 */
public class Level {
    // For convenience access; do not render player separately, he will appear in entity list!
    Player player;

    List<Entity> entities;

    // TODO: add level geometry and populate with entities
    public Level() {
        entities = new ArrayList<>();
        player = new Player(100, 10, 0, new Coord(0.0, 0.0), true);
        entities.add(player);
    }
    public Level(Player player) {
        entities = new ArrayList<>();
        this.player = player;
        entities.add(this.player);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
