package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * Represents an eagle enemy that steals food from the player.
 */
public class Eagle extends Enemy implements Expirable {

    private static final SpriteGroup ART = SpriteGallery.eagle;
    private static final int DEFAULT_LIFESPAN = 999999;
    private static final int INITIAL_SPEED = 2;
    private static final int ESCAPE_SPEED = 4;
    private static final int FOOD_STOLEN = 3;

    private FixedTimer lifespan = new FixedTimer(DEFAULT_LIFESPAN);
    private final HasPosition trackedTarget;
    private boolean attacking = true;
    private final int spawnX;
    private final int spawnY;
    private int food = 0;
    private boolean escapedWithFood = false;

    /**
     * Constructs a new Eagle at the specified position targeting the given player.
     *
     * @param x The x-coordinate of the spawn position.
     * @param y The y-coordinate of the spawn position.
     * @param trackedTarget The player target to track and steal food from.
     */
    public Eagle(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.setSpeed(INITIAL_SPEED);
        this.setSprite(ART.getSprite("default"));
        updateDirectionToTarget();
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
        super.tick(engine);

        tickLifespan();
        handlePlayerInteraction(engine, game);
        handleSpawnReturn(engine);
        move();
        updateDirectionAndSprite();
        recoverFoodIfKilled(game);
    }

    private void tickLifespan() {
        lifespan.tick();
        if (lifespan.isFinished()) {
            markForRemoval();
        }
    }

    private void handlePlayerInteraction(EngineState engine, GameState game) {
        if (!attacking) {
            return;
        }

        boolean reachedPlayer = distanceFrom(
                game.getPlayer().getX(),
                game.getPlayer().getY()
        ) < engine.getDimensions().tileSize();

        if (reachedPlayer && food == 0) {
            stealFood(game);
        }
    }

    private void stealFood(GameState game) {
        game.getInventory().addFood(-FOOD_STOLEN);
        this.food = FOOD_STOLEN;
        this.attacking = false;
        this.setSpeed(ESCAPE_SPEED);
    }

    private void handleSpawnReturn(EngineState engine) {
        if (attacking) {
            return;
        }

        boolean reachedSpawn = distanceFrom(spawnX, spawnY)
                < engine.getDimensions().tileSize();

        if (reachedSpawn) {
            escapedWithFood = true;
            markForRemoval();
        }
    }

    private void updateDirectionAndSprite() {
        if (attacking) {
            updateDirectionToTarget();
            updateSpriteBasedOnTarget();
        } else {
            updateDirectionToSpawn();
            updateSpriteBasedOnSpawn();
        }
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
        if (trackedTarget.getY() > getY()) {
            setSprite(ART.getSprite("down"));
        } else {
            setSprite(ART.getSprite("up"));
        }
    }

    private void updateSpriteBasedOnSpawn() {
        if (spawnY < getY()) {
            setSprite(ART.getSprite("up"));
        } else {
            setSprite(ART.getSprite("down"));
        }
    }

    private void recoverFoodIfKilled(GameState game) {
        if (isMarkedForRemoval() && food > 0 && !escapedWithFood) {
            game.getInventory().addFood(food);
        }
    }
}