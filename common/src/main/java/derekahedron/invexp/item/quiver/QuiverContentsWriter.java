package derekahedron.invexp.item.quiver;

import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.InsertableContents;
import derekahedron.invexp.containeritem.ShootableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Manages contents of a quiver. Extends ContainerItemContents for improved modifying of
 * contents.
 */
public class QuiverContentsWriter extends ContainerItemContentsWriter implements
        QuiverContentsReader,
        InsertableContents,
        ShootableContents {

    private QuiverContents component;

    private QuiverContentsWriter(ItemStack quiverStack, QuiverContents component) {
        super(quiverStack);
        this.component = component;
    }

    /**
     * Create a new QuiverContentsWriter from the given stack. If the stack cannot have
     * contents, returns null
     *
     * @param stack stack to create contents from
     * @return created QuiverContentsWriter; null if not valid
     */
    @Nullable
    public static QuiverContentsWriter of(@Nullable ItemStack stack) {
        if (stack == null || !QuiverContents.hasQuiverContents(stack.getItem())) return null;
        QuiverContents component = QuiverContents.getComponent(stack);
        return new QuiverContentsWriter(stack, component);
    }

    @Override
    public List<ItemStack> getStacks() {
        return component.getStacks();
    }

    @Override
    public int getSelectedIndex() {
        return component.getSelectedIndex();
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
    public void playInsertSound(Entity entity) {
        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            quiverItem.playInsertSound(entity);
        }
    }

    @Override
    public int getPickupPriority() {
        return 100;
    }

    /**
     * Builder for QuiverContents. Contains a copy of the quiver contents to be modified.
     */
    public class Builder extends ContainerItemContentsWriter.Builder implements QuiverContentsReader {

        /**
         * Copies component data into modifiable versions.
         */
        public Builder() {
            super(
                    QuiverContentsWriter.this.getStacks(),
                    QuiverContentsWriter.this.getSelectedIndex(),
                    QuiverContentsWriter.this.getTotalWeight());
        }

        @Override
        public void apply() {
            component = new QuiverContents(
                    List.copyOf(stacks),
                    clampIndex(selectedIndex),
                    computeTotalWeight());
            component.setComponent(containerStack);
        }

        @Override
        public ItemStack getContainerStack() {
            return containerStack;
        }
    }
}
