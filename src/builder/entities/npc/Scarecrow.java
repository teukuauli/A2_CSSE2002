package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.enemies.Pigeon;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a scarecrow that scares away magpies and pigeons within a certain radius.
 */
public class Scarecrow extends Npc {

    public static final int COIN_COST = 2;
    private static final int SCARE_RADIUS_MULTIPLIER = 4;
    private static final SpriteGroup ART = SpriteGallery.scarecrow;

    /**
     * Constructs a new Scarecrow at the specified position.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Scarecrow(int x, int y) {
        super(x, y);
        setSprite(ART.getSprite("default"));
        setSpeed(0);
    }

    @Override
    public void tick(EngineState state) {
        super.tick(state);
    }

    @Override
    public void interact(EngineState state, GameState game) {
        super.interact(state, game);
        int scareRadius = calculateScareRadius(state);
        scareAwayBirds(game, scareRadius);
    }

    private int calculateScareRadius(EngineState state) {
        return state.getDimensions().tileSize() * SCARE_RADIUS_MULTIPLIER;
    }

    private void scareAwayBirds(GameState game, int scareRadius) {
        List<Magpie> magpies = extractMagpies(game);
        List<Pigeon> pigeons = extractPigeons(game);

        scareAwayMagpies(magpies, scareRadius);
        scareAwayPigeons(pigeons, scareRadius);
    }

    private List<Magpie> extractMagpies(GameState game) {
        List<Magpie> magpies = new ArrayList<>();
        for (Enemy bird : game.getEnemies().getBirds()) {
            if (bird instanceof Magpie magpie) {
                magpies.add(magpie);
            }
        }
        return magpies;
    }

    private List<Pigeon> extractPigeons(GameState game) {
        List<Pigeon> pigeons = new ArrayList<>();
        for (Enemy bird : game.getEnemies().getBirds()) {
            if (bird instanceof Pigeon pigeon) {
                pigeons.add(pigeon);
            }
        }
        return pigeons;
    }

    private void scareAwayMagpies(List<Magpie> magpies, int scareRadius) {
        for (Magpie magpie : magpies) {
            if (isWithinScareRadius(magpie, scareRadius)) {
                magpie.setAttacking(false);
            }
        }
    }

    private void scareAwayPigeons(List<Pigeon> pigeons, int scareRadius) {
        for (Pigeon pigeon : pigeons) {
            if (isWithinScareRadius(pigeon, scareRadius)) {
                pigeon.setAttacking(false);
            }
        }
    }

    private boolean isWithinScareRadius(Enemy bird, int scareRadius) {
        return distanceFrom(bird) < scareRadius;
    }
}