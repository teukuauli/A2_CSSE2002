package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.npc.BeeHive;

import engine.EngineState;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * Spawner for bee hives that allows players to create them.
 */
public class BeeHiveSpawner implements Spawner {

    /**
     * Timer for spawning cooldown.
     */
    private RepeatingTimer timer;
    private int xc = 0;
    private int yc = 0;

    /**
     * Constructs a new BeeHiveSpawner.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param duration The spawn duration.
     */
    public BeeHiveSpawner(int x, int y, int duration) {
        this.xc = x;
        this.yc = y;
        this.timer = new RepeatingTimer(300);
    }

    @Override
    public TickTimer getTimer() {
        return this.timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        timer.tick();
        final boolean canAfford = game.getInventory().getFood() >= 3
                && game.getInventory().getCoins() >= 3;

        if (canAfford && state.getKeys().isDown('h')) {
            game.getInventory().addFood(-3);
            game.getInventory().addCoins(-3);
            game.getNpcs().getNpcs().add(new BeeHive(game.getPlayer().getX(),
                    game.getPlayer().getY()));
        }
        // look at use code example to spawn based on user input and only on grass tiles
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
