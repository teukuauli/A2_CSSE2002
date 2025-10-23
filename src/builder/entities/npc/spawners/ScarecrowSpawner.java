package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.npc.Scarecrow;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * Spawner for scarecrows that allows players to create them.
 */
public class ScarecrowSpawner implements Spawner {

    private int xc = 0;
    private int yc = 0;
    private RepeatingTimer timer = new RepeatingTimer(300);

    /**
     * Constructs a new ScarecrowSpawner.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public ScarecrowSpawner(int x, int y) {
        this.xc = x;
        this.yc = y;
    }

    @Override
    public TickTimer getTimer() {
        return this.timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.timer.tick();
        // look at use code to spawn
        if (game.getInventory().getCoins() >= 2 && state.getKeys().isDown('c')) {
            game.getInventory().addCoins(-2);
            game.getNpcs().addNpc(new Scarecrow(game.getPlayer().getX(),
                    game.getPlayer().getY()));
        }
    }

    @Override
    public int getX() {
        return this.xc;
    }

    @Override
    public void setX(int x) {
        this.xc = x;
    }

    @Override
    public int getY() {
        return this.yc;
    }

    @Override
    public void setY(int y) {
        this.yc = y;
    }
}
