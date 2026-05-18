package derekahedron.invexp.item.bundle;

import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BundleContentsWriter extends ContainerItemContentsWriter implements BundleContentsReader {

    private BundleContents component;

    private BundleContentsWriter(ItemStack stack, BundleContents component) {
        super(stack);
        this.component = component;
    }

    /**
     * Create a new BundleContentsWriter from the given stack. If the stack cannot have
     * contents, returns null
     *
     * @param stack stack to create contents from
     * @return created BundleContentsWriter; null if not valid
     */
    @Nullable
    public static BundleContentsWriter of(@Nullable ItemStack stack) {
        if (stack == null || !BundleContents.hasBundleContents(stack.getItem())) return null;
        BundleContents component = BundleContents.getComponent(stack);
        return new BundleContentsWriter(stack, component);
    }

    /**
     * Checks if the given contents are valid. Does so by checking that each item can be added,
     * plus making sure that the weights and stacks are not over the max amount.
     *
     * @return <code>true</code> if the contents are valid; <code>false</code> otherwise
     */
    public boolean isValid() {
        if (!getStacks().stream().allMatch(this::canTryInsert)) return false;

        if (getTotalWeight().compareTo(getMaxWeight()) > 0) return false;

        return getStacks().size() <= getMaxStacks();
    }

    /**
     * Checks if the contents are valid. If they are not, create a new BundleContentsWriter
     * and add each item one by one. Leftover stacks are given to the player after the validation.
     *
     * @param player player holding the bundle
     */
    public void validate(Player player) {
        if (isValid()) return;

        ArrayList<ItemStack> removedStacks = new ArrayList<>(getStacks().size());
        BundleContentsWriter newContents = new BundleContentsWriter(containerStack, new BundleContents());
        Builder builder = newContents.getBuilder();
        for (int i = getStacks().size() - 1; i >= 0; i--) {
            ItemStack stack = getStacks().get(i).copy();
            builder.add(stack, 0);
            if (!stack.isEmpty()) {
                removedStacks.add(stack);
            }
        }

        builder.selectedIndex = builder.nextSelectedIndex(getSelectedStack(), getSelectedIndex());
        builder.apply();
        component = newContents.component;
        for (ItemStack stack : removedStacks) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
    }

    @Override
    public Fraction getTotalWeight() {
        return component.getTotalWeight()
                .orElse(computeTotalWeight());
    }

    @Override
    public List<ItemStack> getStacks() {
        return component.stacks;
    }

    @Override
    public int getSelectedIndex() {
        return component.selectedIndex;
    }

    @Override
    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex != -1) {
            selectedIndex = clampIndex(selectedIndex);
        }
        if (selectedIndex != getSelectedIndex()) {
            ContainerItemContentsWriter.Builder builder = getBuilder();
            builder.setSelectedIndex(selectedIndex);
            builder.apply();
        }
    }

    @Override
    public Builder getBuilder() {
        return new Builder();
    }

    /**
     * Builder for BundleContents. Contains a copy of the bundle contents to be modified.
     */
    public class Builder extends ContainerItemContentsWriter.Builder implements BundleContentsReader {

        public Builder() {
            super(
                    BundleContentsWriter.this.getStacks(),
                    BundleContentsWriter.this.getSelectedIndex(),
                    BundleContentsWriter.this.getTotalWeight());
        }

        @Override
        public void apply() {
            component = new BundleContents(
                    List.copyOf(stacks),
                    Mth.clamp(selectedIndex, -1, stacks.size() - 1),
                    computeTotalWeight());
            component.setComponent(containerStack);
        }

        @Override
        public ItemStack getContainerStack() {
            return containerStack;
        }
    }
}
