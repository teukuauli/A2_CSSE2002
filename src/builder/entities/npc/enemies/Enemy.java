package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;

import engine.EngineState;

/**
 * Represents a hostile enemy NPC in the game.
 */
public class Enemy extends Npc {
    /**
     * Constructs a new Enemy at the specified position.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
    }

    @Override
    public void interact(EngineState state, GameState game) {}
}
