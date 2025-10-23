package builder.world;

import engine.game.ImmutablePosition;

/**
 * Interface for player spawn details containing position and starting resources.
 */
public interface PlayerDetails extends ImmutablePosition {
    /**
     * Gets the starting food amount for the player.
     *
     * @return The starting food amount.
     */
    int getStartingFood();

    /**
     * Gets the starting coins amount for the player.
     *
     * @return The starting coins amount.
     */
    int getStartingCoins();
}
