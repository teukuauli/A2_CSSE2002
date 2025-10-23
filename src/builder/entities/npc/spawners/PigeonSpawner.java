package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;

import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

import java.util.List;

/**
 * Spawner for pigeon enemies.
 */
public class PigeonSpawner implements Spawner {

    private static final int DEFAULT_SPAWN_INTERVAL = 300;

    private int xc;
    private int yc;
    private final TickTimer timer;

    /**
     * Constructs a new PigeonSpawner with default spawn interval.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public PigeonSpawner(int x, int y) {
        this(x, y, DEFAULT_SPAWN_INTERVAL);
    }

    /**
     * Constructs a new PigeonSpawner with specified spawn interval.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param duration The spawn interval duration.
     */
    public PigeonSpawner(int x, int y, int duration) {
        this.xc = x;
        this.yc = y;
        this.timer = new RepeatingTimer(duration);
    }

    @Override
    public TickTimer getTimer() {
        return timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        timer.tick();

        if (timer.isFinished()) {
            spawnPigeon(game);
        }
    }

    private void spawnPigeon(GameState game) {
        game.getEnemies().setSpawnX(getX());
        game.getEnemies().setSpawnY(getY());
        
        List<Tile> cabbageTiles = findCabbageTiles(game);
        
        if (!cabbageTiles.isEmpty()) {
            Tile closestCabbage = findClosestCabbage(cabbageTiles);
            game.getEnemies().getBirds().add(game.getEnemies().mkP(closestCabbage));
        }
    }

    private List<Tile> findCabbageTiles(GameState game) {
        return game.getWorld().tileSelector(tile ->
                tile.getStackedEntities().stream()
                        .anyMatch(entity -> entity instanceof Cabbage)
        );
    }

    private Tile findClosestCabbage(List<Tile> cabbageTiles) {
        Tile closest = cabbageTiles.get(0);
        int minDistance = distanceFrom(closest);

        for (Tile tile : cabbageTiles) {
            int distance = distanceFrom(tile);
            if (distance < minDistance) {
                minDistance = distance;
                closest = tile;
            }
        }
        return closest;
    }

    private int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - getX();
        int deltaY = position.getY() - getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    @Override
    public int getX() {
        return xc;
    }

    @Override
    public void setX(int x) {
        this.xc = x;
    }

    @Override
    public int getY() {
        return yc;
    }

    @Override
    public void setY(int y) {
        this.yc = y;
    }
}