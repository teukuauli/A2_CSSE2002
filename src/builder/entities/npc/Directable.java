package builder.entities.npc;

/**
 * Interface for entities that can be directed in a specific direction and moved.
 */
public interface Directable {

    /**
     * Gets the current direction of the entity in degrees.
     *
     * @return The current direction.
     */
    int getDirection();

    /**
     * Sets the direction of the entity in degrees.
     *
     * @param direction The new direction to set.
     */
    void setDirection(int direction);

    /**
     * Moves the entity according to its current direction and speed.
     */
    void move();
}
