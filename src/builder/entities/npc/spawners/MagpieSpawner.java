package builder.entities.npc.spawners;

import builder.GameState;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * Spawner for magpie enemies.
 */
public class MagpieSpawner implements Spawner {

    private static final int DEFAULT_SPAWN_INTERVAL = 360;

    private int xPos;
    private int yPos;
    private final TickTimer timer;

    /**
     * Constructs a new MagpieSpawner with default spawn interval.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public MagpieSpawner(int x, int y) {
        this(x, y, DEFAULT_SPAWN_INTERVAL);
    }

    /**
     * Constructs a new MagpieSpawner with specified spawn interval.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param duration The spawn interval duration.
     */
    public MagpieSpawner(int x, int y, int duration) {
        this.xPos = x;
        this.yPos = y;
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
            spawnMagpie(game);
        }
    }

    private void spawnMagpie(GameState game) {
        game.getEnemies().setSpawnX(getX());
        game.getEnemies().setSpawnY(getY());
        game.getEnemies().getBirds().add(game.getEnemies().mkM(game.getPlayer()));
    }

    @Override
    public int getX() {
        return xPos;
    }

    @Override
    public void setX(int x) {
        this.xPos = x;
    }

    @Override
    public int getY() {
        return yPos;
    }

    @Override
    public void setY(int y) {
        this.yPos = y;
    }
}