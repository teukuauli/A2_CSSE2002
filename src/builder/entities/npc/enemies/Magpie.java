package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * Represents a magpie enemy that steals coins from the player.
 */
public class Magpie extends Enemy implements Expirable {

    private static final SpriteGroup ART = SpriteGallery.magpie;
    private static final int DEFAULT_LIFESPAN = 10000;
    private static final int DEFAULT_SPEED = 1;
    private static final int ESCAPE_SPEED = 2;
    private static final int COINS_STOLEN = 1;

    private FixedTimer lifespan = new FixedTimer(DEFAULT_LIFESPAN);
    private final HasPosition trackedTarget;
    /**
     * Indicates whether the magpie is currently in attacking mode.
     */
    private boolean attacking;
    private int coins = 0;
    private boolean escapedWithCoins = false;
    private final int spawnX;
    private final int spawnY;

    /**
     * Constructs a new Magpie at the specified position targeting the given player.
     *
     * @param x The x-coordinate of the spawn position.
     * @param y The y-coordinate of the spawn position.
     * @param trackedTarget The player target to track and steal coins from.
     */
    public Magpie(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.attacking = true;
        this.setSpeed(DEFAULT_SPEED);
        this.setSprite(ART.getSprite("down"));
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
        super.tick(engine, game);

        tickLifespan();
        updateDirectionAndSprite();
        move();
        handlePlayerInteraction(engine, game);
        handleSpawnReturn(engine);
        recoverCoinsIfKilled(game);
    }

    private void tickLifespan() {
        lifespan.tick();
        if (lifespan.isFinished()) {
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

    private void handlePlayerInteraction(EngineState engine, GameState game) {
        if (!attacking) {
            return;
        }

        Player player = game.getPlayer();
        boolean reachedPlayer = distanceFrom(player.getX(), player.getY())
                < engine.getDimensions().tileSize();

        if (reachedPlayer && game.getInventory().getCoins() > 0) {
            stealCoin(game);
        }
    }

    private void stealCoin(GameState game) {
        game.getInventory().addCoins(-COINS_STOLEN);
        this.coins += COINS_STOLEN;
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
            escapedWithCoins = true;
            markForRemoval();
        }
    }

    private void recoverCoinsIfKilled(GameState game) {
        if (isMarkedForRemoval() && coins > 0 && !escapedWithCoins) {
            game.getInventory().addCoins(coins);
        }
    }

    @Override
    public void interact(EngineState engine, GameState game) {}

    /**
     * Sets the attacking state of this magpie.
     *
     * @param attacking The new attacking state.
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
}