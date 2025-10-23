package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all NPCs in the game.
 */
public class NpcManager implements Interactable, Tickable, RenderableGroup {
    private final ArrayList<Npc> npcs = new ArrayList<>();

    /**
     * Constructs a new NpcManager.
     */
    public NpcManager() {}

    /**
     * Gets the list of all NPCs.
     *
     * @return The list of NPCs.
     */
    public ArrayList<Npc> getNpcs() {
        return npcs;
    }

    /**
     * Removes all NPCs that are marked for removal.
     */
    public void cleanup() {
        for (int i = this.npcs.size() - 1; i >= 0; i -= 1) {
            if (this.npcs.get(i).isMarkedForRemoval()) {
                this.npcs.remove(i);
            }
        }
    }

    /**
     * Adds an NPC to the manager for tracking and management.
     *
     * @param npc The NPC to add to the manager.
     */
    public void addNpc(Npc npc) {
        this.npcs.add(npc);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Npc npc : npcs) {
            npc.tick(state, game);
        }
    }

    @Override
    public void interact(EngineState state, GameState game) {
        for (Interactable interactable : this.getInteractables()) {
            interactable.interact(state, game);
        }
    }

    /**
     * Returns an ArrayList of Interactable NPCs.
     *
     * @return An ArrayList of Interactable NPCs.
     */
    private ArrayList<Interactable> getInteractables() {
        final ArrayList<Interactable> interactables = new ArrayList<>();
        for (Npc npc : npcs) {
            if (npc instanceof Interactable) {
                interactables.add(npc);
            }
        }
        return interactables;
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.npcs);
    }
}
