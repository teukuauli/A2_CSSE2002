package builder.world;

import engine.game.HasPosition;

/**
 * Interface for spawner details containing position and duration information.
 */
public interface SpawnerDetails extends HasPosition {
    /**
     * Gets the x-coordinate of the spawner.
     *
     * @return The x-coordinate.
     */
    int getX();

    /**
     * Gets the y-coordinate of the spawner.
     *
     * @return The y-coordinate.
     */
    int getY();

    /**
     * Sets the x-coordinate of the spawner.
     *
     * @param x The new x-coordinate.
     */
    void setX(int x);

    /**
     * Sets the y-coordinate of the spawner.
     *
     * @param y The new y-coordinate.
     */
    void setY(int y);

    /**
     * Gets the spawn duration of the spawner.
     *
     * @return The spawn duration.
     */
    int getDuration();
}
