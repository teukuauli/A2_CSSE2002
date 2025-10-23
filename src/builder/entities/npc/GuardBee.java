package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * Represents a guard bee that tracks and attacks enemies.
 */
public class GuardBee extends Npc implements Expirable {

    private static final int SPEED = 2;
    private static final int DEFAULT_LIFESPAN = 300;
    private static final SpriteGroup ART = SpriteGallery.bee;

    // Direction angle ranges for sprite selection
    private static final int UP_MIN_ANGLE = 230;
    private static final int UP_MAX_ANGLE = 310;
    private static final int DOWN_MIN_ANGLE = 40;
    private static final int DOWN_MAX_ANGLE = 140;
    private static final int RIGHT_MIN_ANGLE = 310;
    private static final int RIGHT_MAX_ANGLE = 40;

    private final int spawnX;
    private final int spawnY;
    private FixedTimer lifespan = new FixedTimer(DEFAULT_LIFESPAN);
    private final HasPosition trackedTarget;

    /**
     * Constructs a new GuardBee at the specified position targeting the given enemy.
     *
     * @param x The x-coordinate of the bee's spawn position.
     * @param y The y-coordinate of the bee's spawn position.
     * @param trackedTarget The enemy target to track and attack.
     */
    public GuardBee(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.setSpeed(SPEED);
        this.setSprite(ART.getSprite("default"));
        initializeDirection();
    }

    private void initializeDirection() {
        double deltaX = trackedTarget.getX() - getX();
        double deltaY = trackedTarget.getY() - getY();
        setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
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
    public void tick(EngineState state, GameState game) {
        super.tick(state);

        if (checkAndHandleCollision(state, game)) {
            return; // Exit early if collision occurred
        }

        updateDirection();
        move();
        updateArtBasedOnDirection();
        tickLifespan();
    }

    private boolean checkAndHandleCollision(EngineState state, GameState game) {
        for (Enemy enemy : game.getEnemies().getAll()) {
            if (isCollidingWith(enemy, state)) {
                handleCollision(enemy);
                return true;
            }
        }
        return false;
    }

    private boolean isCollidingWith(Enemy enemy, EngineState state) {
        return distanceFrom(enemy) < state.getDimensions().tileSize();
    }

    private void handleCollision(Enemy enemy) {
        enemy.markForRemoval();
        markForRemoval();
    }

    private void updateDirection() {
        if (trackedTarget != null) {
            updateDirectionToTarget();
        } else {
            updateDirectionToSpawn();
        }
    }

    private void updateDirectionToTarget() {
        double deltaX = trackedTarget.getX() - getX();
        double deltaY = trackedTarget.getY() - getY();
        setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    private void updateDirectionToSpawn() {
        double deltaX = spawnX - getX();
        double deltaY = spawnY - getY();
        setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    private void updateArtBasedOnDirection() {
        int direction = getDirection();

        if (isGoingDown(direction)) {
            setSprite(ART.getSprite("down"));
        } else if (isGoingUp(direction)) {
            setSprite(ART.getSprite("up"));
        } else if (isGoingRight(direction)) {
            setSprite(ART.getSprite("right"));
        } else {
            setSprite(ART.getSprite("left"));
        }
    }

    private boolean isGoingUp(int direction) {
        return direction >= UP_MIN_ANGLE && direction < UP_MAX_ANGLE;
    }

    private boolean isGoingDown(int direction) {
        return direction >= DOWN_MIN_ANGLE && direction < DOWN_MAX_ANGLE;
    }

    private boolean isGoingRight(int direction) {
        return direction >= RIGHT_MIN_ANGLE || direction < RIGHT_MAX_ANGLE;
    }

    private void tickLifespan() {
        lifespan.tick();
        if (lifespan.isFinished()) {
            markForRemoval();
        }
    }
}