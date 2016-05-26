package World;

import Enumerations.*;
import Factories.AnimationFactory;
import Factories.CreatureFactory;
import Factories.LootFactory;
import Models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains the level geometry (grid), a tileset and a list of entity instances. One per game! Methods are self-
 * explanatory or documented if otherwise.
 */
public class Level {

    Player player; // For convenience access; do not render player separately, he will appear in entity list!
    List<Entity> entities;
    ArrayList<Tile> geometry;

    public static byte CURRENT_LEVEL = 1;
    public static int enemyCount;

    // TODO: add level geometry and populate with entities
    public Level() {
        entities = new ArrayList<>();
        generateGeometry();
        spawnEnemies();
        player = new Player(100, 10, 0, 0, 0);
        setStart();
        entities.add(player);
    }

    public Level(Player player) {
        entities = new ArrayList<>();
        generateGeometry();
        spawnEnemies();
        spawnItems();
        this.player = player;
        setStart();
        entities.add(this.player);
    }

    private void generateGeometry() {
        DungeonMaker generator = new DungeonMaker();
        geometry = generator.getLevelTiles();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Stream<Creature> getValidCreatures(Creature collider) {
        return entities.stream()
                .filter(entity -> entity instanceof Creature) // get just the creatures
                .filter(entity -> !entity.hasState(EntityState.DEAD)) // don't collide with corpses
                .filter(entity -> Math.abs(entity.getX() - collider.getX()) < Physics.activeRange / 2 &&
                        Math.abs(entity.getY() - collider.getY()) < Physics.activeRange / 2)
                .filter(entity -> !entity.equals(collider)) // can't collide with self
                .map(e -> (Creature) e);
    }

    public Stream<Loot> getItems(Creature collider) {
        return entities.stream()
                .filter(entity -> entity instanceof Loot)
                .filter(entity -> Math.abs(entity.getX() - collider.getX()) < Physics.activeRange / 2 &&
                        Math.abs(entity.getY() - collider.getY()) < Physics.activeRange / 2)
                .map(entity -> (Loot) entity);
    }

    public ArrayList<Tile> getGeometry() {
        return geometry;
    }

    public Stream<Tile> getValidTiles(Creature collider) {
        return geometry.stream()
                .filter(tile -> tile.getTileType() == TileType.WALL || tile.getTileType() == TileType.DOOR) //just the walls
                .filter(tile -> Math.abs(tile.getX() - collider.getX()) < Physics.activeRange &&
                        Math.abs(tile.getY() - collider.getY()) < Physics.activeRange);
    }

    // Pick starting position inside the maze and place player there
    private void setStart() {
        Random rnd = new Random();
        List<Tile> validTiles = geometry.stream()
                .filter(tile -> tile.getTileType() == TileType.FLOOR && tile.getY() > 1)
                .collect(Collectors.toList());
        while (true) {
            int tile = rnd.nextInt(validTiles.size());
            if (validTiles.get(tile).getTileType() == TileType.FLOOR) {
                player.setX(validTiles.get(tile).getX());
                player.setY(validTiles.get(tile).getY());
                break;
            }
        }
    }

    private void spawnEnemies() {
        // Add 5 Skeletons and 2 groups of 3 Rats
        Random rnd = new Random();
        // Get just the floor tiles
        List<Tile> validTiles = geometry.stream()
                .filter(tile -> tile.getTileType() == TileType.FLOOR && tile.getY() > 1)
                .collect(Collectors.toList());
        for (int i = 0; i < 5; i++) {
            Tile current = validTiles.get(rnd.nextInt(validTiles.size())); // Pick random tile
            entities.add(CreatureFactory.createEnemy(EnemyTypes.SKELETON, current.getX(), current.getY(), rnd.nextDouble() * Math.PI * 2));
            validTiles.remove(current); // Remove tile from list
            enemyCount++;
        }
        ///*
        validTiles = geometry.stream()
                .filter(tile -> tile.getTileType() == TileType.FLOOR && tile.getY() > 1)
                .collect(Collectors.toList());
        for (int i = 0; i < 2; i++) {
            Tile current = validTiles.get(rnd.nextInt(validTiles.size())); // Pick random tile
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, current.getX() - 0.3, current.getY() + 0.4, rnd.nextDouble() * Math.PI * 2));
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, current.getX() + 0.3, current.getY() + 0.4, rnd.nextDouble() * Math.PI * 2));
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, current.getX(), current.getY() - 0.2, rnd.nextDouble() * Math.PI * 2));
            validTiles.remove(current); // Remove tile from list
            enemyCount += 3;
        }
        //*/
    }

    private void spawnItems() {
        Random rnd = new Random();
        // Get just the floor tiles
        List<Tile> validTiles = geometry.stream()
                .filter(tile -> tile.getTileType() == TileType.FLOOR && tile.getY() > 1)
                .collect(Collectors.toList());
        for (int i = 0; i < 2; i++) {
            Tile current = validTiles.get(rnd.nextInt(validTiles.size())); // Pick random tile
            entities.add(LootFactory.getConsumable(Items.POTIONHEALTH, current.getX(), current.getY(), 1));
        }
    }

    public void spawnBoss() {
        geometry.stream().filter(tile -> tile.getTileType() == TileType.DOOR).forEach(Tile::toggle);
    }
}
