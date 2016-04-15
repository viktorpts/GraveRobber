package World;

import Enumerations.EnemyTypes;
import Factories.CreatureFactory;
import Models.Entity;
import Models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        spawnEnemies();
        player = new Player(100, 10, 0, 0, 0);
        entities.add(player);
    }

    public Level(Player player) {
        entities = new ArrayList<>();
        spawnEnemies();
        this.player = player;
        entities.add(this.player);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    private void spawnEnemies() {
        // Add 10 Giant Rats
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, rnd.nextInt(20), rnd.nextInt(20), rnd.nextDouble() * Math.PI * 2));
        }
    }
}
