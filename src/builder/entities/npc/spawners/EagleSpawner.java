package builder.entities.npc.spawners;

import builder.GameState;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * Spawner for eagle enemies.
 */
public class EagleSpawner implements Spawner {

    private static final int DEFAULT_SPAWN_INTERVAL = 1000;

    private int xc;
    private int yc;
    private final TickTimer timer;

    /**
     * Constructs a new EagleSpawner with default spawn interval.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public EagleSpawner(int x, int y) {
        this(x, y, DEFAULT_SPAWN_INTERVAL);
    }

    /**
     * Constructs a new EagleSpawner with specified spawn interval.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param duration The spawn interval duration.
     */
    public EagleSpawner(int x, int y, int duration) {
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
            spawnEagle(game);
        }
    }

    private void spawnEagle(GameState game) {
        game.getEnemies().setSpawnX(getX());
        game.getEnemies().setSpawnY(getY());
        game.getEnemies().getBirds().add(game.getEnemies().mkE(game.getPlayer()));
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