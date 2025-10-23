package builder.entities.tiles;

import builder.GameState;
import builder.entities.npc.Scarecrow;
import builder.entities.resources.Cabbage;
import builder.inventory.Inventory;
import builder.inventory.items.Bucket;
import builder.inventory.items.Hoe;
import builder.inventory.items.Pole;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;

/**
 * A dirt tile may be used for farming. A dirt tile has two states: tilled and untilled. The tile
 * should begin untilled and may become tilled by using a hoe on it (in stage 3). When untilled,
 * dirt is rendered as {@link SpriteGallery#field}, when tilled, dirt is rendered as {@link
 * SpriteGallery#tilled}. (Stage 3) A bucket can be used on dirt to plant a cabbage on it.
 */
public class Dirt extends Tile {

    private static final SpriteGroup dirtArt = SpriteGallery.field;
    private static final SpriteGroup tillArt = SpriteGallery.tilled;
    private boolean tilled = false;

    /**
     * Construct a new untilled dirt tile at the given x, y position.
     *
     * @param x The x-axis (horizontal) coordinate.
     * @param y The y-axis (vertical) coordinate.
     * @requires x >= 0, x is less than the window width
     * @requires y >= 0, y is less than the window height
     */
    public Dirt(int x, int y) {
        super(x, y, dirtArt);
    }

    /**
     * Whether the dirt is tilled or not.
     *
     * @return true if the dirt is tilled, false otherwise.
     */
    public boolean isTilled() {
        return this.tilled;
    }

    /** Till the dirt, changing its rendering to its tilled state. */
    public void till() {
        this.tilled = true;
        this.setArt(tillArt);
    }

    /**
     * Attempt to plant a {@link Cabbage} and adjust the resources accordingly. If the user can not
     * currently place the {@link Cabbage} one should not be placed.
     */
    public void plant(Inventory inventory) {
        if (inventory.getCoins() >= Cabbage.COST) {
            inventory.addCoins(-Cabbage.COST);
            Cabbage cabbage = new Cabbage(this.getX(), this.getY());
            this.placeOn(cabbage);
        }
    }

    /**
     * When a hoe is used on a dirt tile, it should become tilled.
     *
     * <p>When a bucket is used on a dirt tile and the following conditions are met a cabbage should
     * be planted (placed) upon it. Conditions:
     *
     * <ol>
     *   <li>There are no other entities stacked on the tile,
     *   <li>the dirt is tilled, and
     *   <li>the inventory has coins greater than or equal to the cost of a cabbage (i.e. {@link
     *       Cabbage#COST}).
     * </ol>
     *
     * <p>The cost of the cabbage should be subtracted from the inventory if it is successfully
     * planted.
     *
     * @stage3
     */
    @Override
    public void use(EngineState state, GameState game) {
        Inventory inventory = game.getInventory();
        if (inventory.getHolding() instanceof Hoe) {
            this.till();
        }
        if (inventory.getHolding() instanceof Bucket
                && this.getStackedEntities().isEmpty()
                && this.isTilled()) {
            this.plant(inventory);
        }
        if (inventory.getHolding() instanceof Pole
                && this.getStackedEntities().isEmpty()
                && this.isTilled()
                && inventory.getCoins() >= Scarecrow.COIN_COST) {
            inventory.addCoins(-Scarecrow.COIN_COST);
            Scarecrow scarecrow = new Scarecrow(this.getX(), this.getY());
            this.placeOn(scarecrow);
            game.getNpcs().getNpcs().add(scarecrow);
        }
    }
}
