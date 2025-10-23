package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.timing.RepeatingTimer;

import java.util.ArrayList;

/**
 * Represents a bee hive that can spawn guard bees to defend against enemies.
 */
public class BeeHive extends Npc {

    public static final int DETECTION_DISTANCE = 350;
    public static final int TIMER_DURATION = 100;
    public static final int FOOD_COST = 2;
    public static final int COIN_COST = 2;

    private static final SpriteGroup ART = SpriteGallery.hive;

    private boolean loaded = true;
    private final RepeatingTimer reloadTimer = new RepeatingTimer(TIMER_DURATION);

    /**
     * Constructs a new BeeHive at the specified position.
     *
     * @param x The x-coordinate of the hive.
     * @param y The y-coordinate of the hive.
     */
    public BeeHive(int x, int y) {
        super(x, y);
        setSprite(ART.getSprite("default"));
        setSpeed(0);
    }

    /**
     * Updates the bee hive state each game tick.
     *
     * @param state The current engine state.
     * @param game The current game state.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state);
        updateReloadStatus();
    }

    /**
     * Updates the reload status of the hive based on the timer.
     */
    private void updateReloadStatus() {
        if (!loaded) {
            reloadTimer.tick();
            if (reloadTimer.isFinished()) {
                loaded = true;
            }
        }
    }

    /**
     * Handles interaction with the bee hive, attempting to spawn a guard bee.
     *
     * @param state The current engine state.
     * @param game The current game state.
     */
    @Override
    public void interact(EngineState state, GameState game) {
        super.interact(state, game);
        attemptBeeSpawn(game);
    }

    /**
     * Attempts to spawn a guard bee if enemies are in range and hive is loaded.
     *
     * @param game The current game state.
     */
    private void attemptBeeSpawn(GameState game) {
        GuardBee bee = createBeeIfEnemyInRange(game.getEnemies().getBirds());
        if (bee != null) {
            game.getNpcs().getNpcs().add(bee);
            loaded = false;
        }
    }

    /**
     * Creates a guard bee if any enemy is within detection range.
     *
     * @param enemies The list of enemies to check.
     * @return A new GuardBee targeting the nearest enemy, or null if none in range.
     */
    private GuardBee createBeeIfEnemyInRange(ArrayList<Enemy> enemies) {
        if (!loaded) {
            return null;
        }

        for (Enemy enemy : enemies) {
            if (isEnemyInRange(enemy)) {
                return new GuardBee(getX(), getY(), enemy);
            }
        }
        return null;
    }

    /**
     * Checks if an enemy is within detection range of this bee hive.
     *
     * @param enemy The enemy to check distance for.
     * @return true if the enemy is within detection range, false otherwise.
     */
    private boolean isEnemyInRange(Enemy enemy) {
        return distanceFrom(enemy) < DETECTION_DISTANCE;
    }
}