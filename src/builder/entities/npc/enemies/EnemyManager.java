package builder.entities.npc.enemies;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.entities.npc.spawners.Spawner;
import builder.player.Player;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.game.HasPosition;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all enemy entities and their spawners in the game.
 */
public class EnemyManager implements Tickable, Interactable, RenderableGroup {

    private final ArrayList<Spawner> spawners = new ArrayList<>();
    private final ArrayList<Enemy> birds = new ArrayList<>();
    private int spawnX;
    private int spawnY;

    /**
     * Constructs a new EnemyManager.
     *
     * @param dimensions The game dimensions.
     */
    public EnemyManager(Dimensions dimensions) {}

    /**
     * Gets the list of all spawners.
     *
     * @return The list of spawners.
     */
    public ArrayList<Spawner> getSpawners() {
        return spawners;
    }

    /**
     * Gets the list of all bird enemies.
     *
     * @return The list of bird enemies.
     */
    public ArrayList<Enemy> getBirds() {
        return birds;
    }

    /**
     * Gets the spawn X coordinate.
     *
     * @return The spawn X coordinate.
     */
    public int getSpawnX() {
        return spawnX;
    }

    /**
     * Sets the spawn X coordinate.
     *
     * @param spawnX The new spawn X coordinate.
     */
    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    /**
     * Gets the spawn Y coordinate.
     *
     * @return The spawn Y coordinate.
     */
    public int getSpawnY() {
        return spawnY;
    }

    /**
     * Sets the spawn Y coordinate.
     *
     * @param spawnY The new spawn Y coordinate.
     */
    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    /**
     * Removes all enemies that are marked for removal.
     */
    public void cleanup() {
        for (int i = this.birds.size() - 1; i >= 0; i -= 1) {
            if (this.birds.get(i).isMarkedForRemoval()) {
                this.birds.remove(i);
            }
        }
    }

    /**
     * Adds a spawner to the enemy manager.
     *
     * @param spawner The spawner to add.
     */
    public void add(Spawner spawner) {
        this.spawners.add(spawner);
    }

    /**
     * Creates and adds a magpie to the enemy list.
     *
     * @param player The player to target.
     * @return The created magpie.
     */
    public Magpie mkM(Player player) {
        final Magpie magpie = new Magpie(this.spawnX, this.spawnY, player);
        this.birds.add(magpie);
        return magpie;
    }

    /**
     * Creates and adds a pigeon to the enemy list.
     *
     * @param hasPosition The target position.
     * @return The created pigeon.
     */
    public Pigeon mkP(HasPosition hasPosition) {
        final Pigeon pigeon = new Pigeon(this.spawnX, this.spawnY, hasPosition);
        this.birds.add(pigeon);
        return pigeon;
    }

    /**
     * Creates an eagle targeting the player.
     *
     * @param player The player to target.
     * @return The created eagle.
     */
    public Eagle mkE(Player player) {
        final Eagle eagle = new Eagle(this.spawnX, this.spawnY, player);
        return eagle;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Spawner spawner : this.spawners) {
            spawner.tick(state, game);
        }
        for (Enemy bird : birds) {
            if (bird instanceof Magpie temp) {
                temp.tick(state, game);
            }
            if (bird instanceof Eagle temp) {
                temp.tick(state, game);
            }
            if (bird instanceof Pigeon temp) {
                temp.tick(state, game);
            }
        }
    }

    /**
     * Gets all Magpie enemies from the enemy manager.
     *
     * @return ArrayList of all Magpies.
     */
    public ArrayList<Magpie> getMagpies() {
        final ArrayList<Magpie> magpies = new ArrayList<Magpie>();
        for (Enemy bird : birds) {
            if (bird instanceof Magpie temp) {
                magpies.add(temp);
            }
        }
        return magpies;
    }

    /**
     * Gets all enemies managed by this enemy manager.
     *
     * @return ArrayList of all enemies.
     */
    public ArrayList<Enemy> getAll() {
        return this.birds;
    }

    /**
     * Handles interaction logic for all enemies.
     *
     * @param state The state of the engine, including the mouse, keyboard information and
     *     dimension. Useful for processing keyboard presses or mouse movement.
     * @param game The state of the game, including the player and world. Can be used to query or
     *     update the game state.
     */
    @Override
    public void interact(EngineState state, GameState game) {
        /* @todo cleanup */
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.birds);
    }
}
