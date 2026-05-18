package derekahedron.invexp.item;

import derekahedron.invexp.item.sack.SackRuleManager;

import javax.annotation.Nullable;

/**
 * Allows tracking Sack Rules on an item so the fetch time for things like sack types and weights is O(1).
 */
public interface ItemDuck {

    /**
     * Updates the Sack Rules attached to this item.
     *
     * @param sackRules the new rules for this item.
     */
    void invexp$setSackRules(@Nullable SackRuleManager.SackRules sackRules);

    /**
     * Gets the sack rules attached to this item.
     *
     * @return the sack rules that are on this item.
     */
    @Nullable
    SackRuleManager.SackRules invexp$getSackRules();
}
