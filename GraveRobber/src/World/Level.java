package World;

import Enumerations.*;
import Factories.AnimationFactory;
import Factories.CreatureFactory;
import Factories.LootFactory;
import Models.*;

import java.util.*;
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
    Map<Long, Tile> geometryMap;
    LevelMaker generator;

    private static int CURRENT_LEVEL = 1;
    public static int enemyCount;

    // TODO: add level geometry and populate with entities
    public Level() {
        entities = new ArrayList<>();
        generateGeometry();
        player = new Player(100, 10, 0, 0, 0);
        setStart();
        entities.add(player);
    }

    public Level(Player player, int depth) {
        CURRENT_LEVEL = depth;
        entities = new ArrayList<>();
        generateGeometry();
        spawnItems();
        this.player = player;
        setStart();
        entities.add(this.player);
    }

    public void nextLevel() {
        entities = new ArrayList<>();
        generator.nextLevel();
        geometry = generator.getTiles().values().stream().collect(Collectors.toCollection(ArrayList::new));
        geometryMap = generator.getTiles();
        spawnItems();
        setStart();
        entities.add(player);
    }

    private void generateGeometry() {
        LevelMaker.init();
        generator = new LevelMaker(CURRENT_LEVEL);
        geometry = generator.getTiles().values().stream().collect(Collectors.toCollection(ArrayList::new));
        geometryMap = generator.getTiles();
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
        Partition start = generator.getRoot().getLeftLeaf();
        Partition end = generator.getRoot().getRightLeaf();
        ArrayList<Room> rooms = new ArrayList<>();

        LinkedList<Partition> startLeaf = new LinkedList<>();
        startLeaf.addLast(start);
        while (startLeaf.size() > 0) {
            start = startLeaf.removeFirst();
            if (start.hasLeaves()) {
                startLeaf.addLast(start.getLeftLeaf());
                startLeaf.addLast(start.getRightLeaf());
            } else if (startLeaf.size() > 0) {
                rooms.add(start.getRoom());
            }
        }
        player.setX(start.getRoom().getOriginX());
        player.setY(start.getRoom().getOriginY());

        LinkedList<Partition> endLeaf = new LinkedList<>();
        endLeaf.addLast(end);
        while (endLeaf.size() > 0) {
            end = endLeaf.removeFirst();
            if (end.hasLeaves()) {
                endLeaf.addLast(end.getLeftLeaf());
                endLeaf.addLast(end.getRightLeaf());
            } else if (endLeaf.size() > 0) {
                rooms.add(end.getRoom());
            }
        }
        for (Room room : rooms) {
            spawnEnemies(room);
        }

        Tile endTile1 = geometryMap.get(generator.getIndex(end.getRoom().getOriginX()-1, end.getRoom().getOriginY()));
        Tile endTile2 = geometryMap.get(generator.getIndex(end.getRoom().getOriginX(), end.getRoom().getOriginY()));
        Tile endTile3 = geometryMap.get(generator.getIndex(end.getRoom().getOriginX()+1, end.getRoom().getOriginY()));
        Tile endTile4 = geometryMap.get(generator.getIndex(end.getRoom().getOriginX(), end.getRoom().getOriginY() + 1));
        endTile1.setTileType(TileType.DOOR);
        endTile1.setImageTile(TileType.DOOR);
        endTile2.setTileType(TileType.DOOR);
        endTile2.setImageTile(TileType.DOOR);
        endTile3.setTileType(TileType.DOOR);
        endTile3.setImageTile(TileType.DOOR);
        entities.add(LootFactory.getConsumable(Items.ENDKEY, endTile2.getX(), endTile2.getY() - 0.01, 1));
        entities.add(CreatureFactory.createEnemy(EnemyTypes.SKELETON, endTile4.getX(), endTile4.getY(), LevelMaker.rand(18) / 9 * Math.PI));
        enemyCount++;
    }

    private void spawnEnemies(Room room) {
        ArrayList<Tile> validTiles = new ArrayList<>();
        for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                validTiles.add(geometryMap.get(generator.getIndex(x, y)));
            }
        }
        validTiles = validTiles.stream()
                .filter(tile -> tile.getTileType() == TileType.FLOOR)
                .collect(Collectors.toCollection(ArrayList::new));
        int skellies = 1 + LevelMaker.rand(2);
        int ratpacks = LevelMaker.rand(2);
        for (int i = 0; i < skellies; i++) {
            Tile current = validTiles.get(LevelMaker.rand(validTiles.size())); // Pick random tile
            entities.add(CreatureFactory.createEnemy(EnemyTypes.SKELETON, current.getX(), current.getY(), LevelMaker.rand(18) / 9 * Math.PI));
            validTiles.remove(current); // Remove tile from list
            enemyCount++;
        }
        for (int i = 0; i < ratpacks; i++) {
            Tile current = validTiles.get(LevelMaker.rand(validTiles.size())); // Pick random tile
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, current.getX() - 0.3, current.getY() + 0.4, LevelMaker.rand(18) / 9 * Math.PI));
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, current.getX() + 0.3, current.getY() + 0.4, LevelMaker.rand(18) / 9 * Math.PI));
            entities.add(CreatureFactory.createEnemy(EnemyTypes.GIANT_RAT, current.getX(), current.getY() - 0.2, LevelMaker.rand(18) / 9 * Math.PI));
            validTiles.remove(current); // Remove tile from list
            enemyCount += 3;
        }
    }

    private void spawnItems() {
        int items = 1 + (int)Math.sqrt(CURRENT_LEVEL);
        Random rnd = new Random();
        // Get just the floor tiles
        List<Tile> validTiles = geometry.stream()
                .filter(tile -> tile.getTileType() == TileType.FLOOR && tile.getY() > 1)
                .collect(Collectors.toList());
        for (int i = 0; i < items; i++) {
            Tile current = validTiles.get(rnd.nextInt(validTiles.size())); // Pick random tile
            entities.add(LootFactory.getConsumable(Items.POTIONHEALTH, current.getX(), current.getY(), 1));
        }
    }

    public void spawnBoss() {
        //nextLevel();
        geometry.stream().filter(tile -> tile.getTileType() == TileType.DOOR).forEach(Tile::toggle);
    }

}
