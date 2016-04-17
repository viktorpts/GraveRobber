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

    ArrayList<Tile> geometry;

    public static byte CURRENT_LEVEL = 1;

    // TODO: add level geometry and populate with entities
    public Level() {
        entities = new ArrayList<>();
        generateGeometry();
        spawnEnemies();
        player = new Player(100, 10, 0, 0, 0);
        entities.add(player);
    }

    public Level(Player player) {
        entities = new ArrayList<>();
        generateGeometry();
        spawnEnemies();
        this.player = player;
        entities.add(this.player);
    }

    private void generateGeometry() {
        DungeonMaker generator = new DungeonMaker();
        geometry = generator.processTiles();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Tile> getGeometry() {
        return geometry;
    }

    private void spawnEnemies() {
        // Add 10 Skeletons and 20 Rats
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            entities.add(CreatureFactory.createEnemy(EnemyTypes.SKELETON, rnd.nextInt(20), rnd.nextInt(20), rnd.nextDouble() * Math.PI * 2));
        }
        for (int i = 0; i < 20; i++) {
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, rnd.nextInt(20), rnd.nextInt(20), rnd.nextDouble() * Math.PI * 2));
        }
    }
}
