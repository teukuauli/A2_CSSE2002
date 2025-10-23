package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.Entity;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

import java.util.List;

/**
 * Represents a pigeon enemy that steals cabbages from fields.
 */
public class Pigeon extends Enemy implements Expirable {

    private static final SpriteGroup ART = SpriteGallery.pigeon;
    private static final int DEFAULT_LIFESPAN = 3000;
    private static final int PIGEON_SPEED = 4;

    private FixedTimer lifespan = new FixedTimer(DEFAULT_LIFESPAN);
    private HasPosition trackedTarget;
    /**
     * Indicates whether the pigeon is currently in attacking mode.
     */
    private boolean attacking = true;
    private final int spawnX;
    private final int spawnY;

    /**
     * Constructs a new Pigeon at the specified position.
     *
     * @param x The x-coordinate of the spawn position.
     * @param y The y-coordinate of the spawn position.
     */
    public Pigeon(int x, int y) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.setSpeed(PIGEON_SPEED);
        this.setSprite(ART.getSprite("down"));
    }

    /**
     * Constructs a new Pigeon at the specified position with a target.
     *
     * @param x The x-coordinate of the spawn position.
     * @param y The y-coordinate of the spawn position.
     * @param trackedTarget The target position to track.
     */
    public Pigeon(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.setSpeed(PIGEON_SPEED);
        initializeDirectionAndSprite();
    }

    private void initializeDirectionAndSprite() {
        if (trackedTarget != null) {
            updateDirectionToTarget();
            updateSpriteBasedOnTarget();
        } else {
            setSprite(ART.getSprite("down"));
        }
    }

    @Override
    public FixedTimer getLifespan() {
        return lifespan;
    }

    @Override
    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);

        retargetClosestCabbage(game, engine);
        updateMovementAndSprite(engine);
        move();
        tickLifespan();
    }

    private void retargetClosestCabbage(GameState game, EngineState engine) {
        List<Tile> cabbageTiles = findCabbageTiles(game);

        if (cabbageTiles.isEmpty()) {
            attacking = false;
            return;
        }

        Tile closestCabbage = findClosestTile(cabbageTiles);
        trackedTarget = closestCabbage;
        attemptCabbageSteal(closestCabbage, engine);
    }

    private List<Tile> findCabbageTiles(GameState game) {
        return game.getWorld().tileSelector(tile ->
                tile.getStackedEntities().stream()
                        .anyMatch(entity -> entity instanceof Cabbage)
        );
    }

    private Tile findClosestTile(List<Tile> tiles) {
        Tile closest = tiles.get(0);
        int minDistance = distanceFrom(closest);

        for (Tile tile : tiles) {
            int distance = distanceFrom(tile);
            if (distance < minDistance) {
                minDistance = distance;
                closest = tile;
            }
        }
        return closest;
    }

    private void attemptCabbageSteal(Tile cabbageTile, EngineState engine) {
        if (!attacking || trackedTarget == null) {
            return;
        }

        boolean reachedCabbage = distanceFrom(trackedTarget)
                < engine.getDimensions().tileSize();

        if (reachedCabbage) {
            stealCabbageFrom(cabbageTile);
        }
    }

    private void stealCabbageFrom(Tile tile) {
        for (Entity entity : tile.getStackedEntities()) {
            if (entity instanceof Cabbage cabbage) {
                cabbage.markForRemoval();
                attacking = false;
                break;
            }
        }
    }

    private void updateMovementAndSprite(EngineState engine) {
        if (!attacking) {
            handleReturnToSpawn(engine);
        } else if (trackedTarget != null) {
            handleTargetTracking();
        } else {
            handleCenterScreenMovement(engine);
        }
    }

    private void handleReturnToSpawn(EngineState engine) {
        updateDirectionToSpawn();
        updateSpriteBasedOnSpawn();

        boolean reachedSpawn = distanceFrom(spawnX, spawnY)
                < engine.getDimensions().tileSize();

        if (reachedSpawn) {
            markForRemoval();
        }
    }

    private void handleTargetTracking() {
        updateDirectionToTarget();
        updateSpriteBasedOnTarget();
    }

    private void handleCenterScreenMovement(EngineState engine) {
        int centerX = engine.getDimensions().windowSize() / 2;
        int centerY = engine.getDimensions().windowSize() / 2;
        setDirectionTo(centerX, centerY);
        updateSpriteBasedOnY(centerY);
    }

    private void updateDirectionToTarget() {
        setDirectionTo(trackedTarget.getX(), trackedTarget.getY());
    }

    private void updateDirectionToSpawn() {
        setDirectionTo(spawnX, spawnY);
    }

    private void setDirectionTo(int targetX, int targetY) {
        double deltaX = targetX - getX();
        double deltaY = targetY - getY();
        setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    private void updateSpriteBasedOnTarget() {
        updateSpriteBasedOnY(trackedTarget.getY());
    }

    private void updateSpriteBasedOnSpawn() {
        updateSpriteBasedOnY(spawnY);
    }

    private void updateSpriteBasedOnY(int targetY) {
        if (targetY > getY()) {
            setSprite(ART.getSprite("down"));
        } else {
            setSprite(ART.getSprite("up"));
        }
    }

    private void tickLifespan() {
        lifespan.tick();
        if (lifespan.isFinished()) {
            markForRemoval();
        }
    }

    /**
     * Sets the attacking state of this pigeon.
     *
     * @param attacking The new attacking state.
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
}