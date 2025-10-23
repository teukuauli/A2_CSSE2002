package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;

import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;

/**
 * Represents a non-player character (NPC) in the game.
 */
public class Npc extends Entity implements Interactable, Tickable, Directable {

    private int direction = 0;
    private double speed = 1;

    /**
     * Constructs a new NPC at the specified position.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Npc(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the movement speed of the NPC.
     *
     * @return The speed value.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the movement speed of the NPC.
     *
     * @param speed The new speed value.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the current direction in degrees.
     *
     * @return The direction value.
     */
    public int getDirection() {
        return this.direction;
    }

    /**
     * Sets the direction in degrees.
     *
     * @param direction The new direction value.
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Adjust the X and Y of this NPC based on its direction and speed.
     */
    public void move() {
        final int deltaX = (int) Math.round(Math.cos(Math.toRadians(this.direction)) * this.speed);
        final int deltaY = (int) Math.round(Math.sin(Math.toRadians(this.direction)) * this.speed);
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    @Override
    public void tick(EngineState state) {
        this.move();
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.move();
    }

    @Override
    public void interact(EngineState state, GameState game) {}

    /**
     * Return how far away this npc is from the given position.
     *
     * @param position the position we are measuring to from this npcs position!
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - this.getX();
        int deltaY = position.getY() - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Return how far away this npc is from the given position.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(int x, int y) {
        int deltaX = x - this.getX();
        int deltaY = y - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
