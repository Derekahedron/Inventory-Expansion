package derekahedron.invexp.item.sack;

import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.InsertableContents;
import derekahedron.invexp.containeritem.UsableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages contents of a sack. Extends ContainerItemContentsWriter for improved modifying of
 * contents.
 */
public class SackContentsWriter extends ContainerItemContentsWriter implements SackContentsReader, InsertableContents, UsableContents {
    private SackContents component;

    private SackContentsWriter(
            ItemStack containerStack,
            SackContents component) {
        super(containerStack);
        this.component = component;
    }

    /**
     * Create a new SackContentsWriter from the given stack. If the stack cannot have
     * contents, returns null
     *
     * @param stack stack to create contents from
     * @return created SackContentsWriter; null if not valid
     */
    @Nullable
    public static SackContentsWriter of(@Nullable ItemStack stack) {
        if (stack == null || !SackContents.hasSackContents(stack.getItem())) return null;
        SackContents component = SackContents.getComponent(stack);
        return new SackContentsWriter(stack, component);
    }

    @Override
    public List<ItemStack> getStacks() {
        return component.getStacks();
    }

    @Override
    public int getSelectedIndex() {
        return component.selectedIndex;
    }

    @Override
    public List<String> getSackTypes() {
        return component.getSackTypes();
    }

    @Override
    public Fraction getTotalWeight() {
        return component.getTotalWeight()
                .orElseGet(this::computeTotalWeight);
    }

    @Override
    public Builder getBuilder() {
        return new Builder();
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return isInTypes(getSackType(stack));
    }

    @Override
    public void playInsertSound(Entity entity) {
        if (getContainerStack().getItem() instanceof SackItem sackItem) {
            sackItem.playInsertSound(entity);
        }
    }

    public class Builder extends ContainerItemContentsWriter.Builder implements SackContentsReader {

        public final List<String> sackTypes;

        /**
         * Copies component data into modifiable versions.
         */
        public Builder() {
            super(
                    SackContentsWriter.this.getStacks(),
                    SackContentsWriter.this.getSelectedIndex(),
                    SackContentsWriter.this.getTotalWeight());
            this.sackTypes = new ArrayList<>(component.getSackTypes());
        }

        @Override
        public void apply() {
            component = new SackContents(
                    List.copyOf(sackTypes),
                    List.copyOf(stacks),
                    clampIndex(selectedIndex),
                    computeTotalWeight());
            component.setComponent(containerStack);
        }

        @Override
        public ItemStack getContainerStack() {
            return containerStack;
        }

        @Override
        public List<String> getSackTypes() {
            return sackTypes;
        }

        @Override
        public int add(ItemStack stack, int insertAt) {
            String sackType = getSackType(stack);
            int added = super.add(stack, insertAt);

            if (added > 0) {
                tryAddType(sackType);
            }

            return added;
        }

        @Override
        public int remove(ItemStack stack, int toRemove) {
            int removed = super.remove(stack, toRemove);

            // Try to remove sack type
            if (removed > 0) {
                tryRemoveType(getSackType(stack));
            }

            return removed;
        }

        @Override
        public ItemStack popSelectedStack() {
            ItemStack poppedStack = super.popSelectedStack();

            if (!poppedStack.isEmpty()) {
                tryRemoveType(getSackType(poppedStack));
            }

            return poppedStack;
        }

        @Override
        public List<ItemStack> popAllStacks() {
            sackTypes.clear();
            return super.popAllStacks();
        }

        /**
         * Tries to add the given sack type to the list of types.
         *
         * @param sackType a sack type to try to add
         */
        public void tryAddType(@Nullable String sackType) {
            if (sackType != null && !isInTypes(sackType)) {
                sackTypes.add(sackType);
            }
        }

        /**
         * Tries to remove a given sack type from the list of types, if applicable.
         *
         * @param sackType the sack type to try to remove
         */
        public void tryRemoveType(@Nullable String sackType) {
            if (sackType == null || !isInTypes(sackType)) return;

            for (ItemStack nestedStack : getStacks()) {
                if (sackType.equals(getSackType(nestedStack))) {
                    return;
                }
            }
            for (int i = 0; i < sackTypes.size(); i++) {
                if (sackType.equals(sackTypes.get(i))) {
                    sackTypes.remove(i);
                    i--;
                }
            }
        }
    }
}
