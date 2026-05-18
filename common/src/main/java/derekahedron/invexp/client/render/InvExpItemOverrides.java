package derekahedron.invexp.client.render;

import derekahedron.invexp.client.util.ClientServices;
import derekahedron.invexp.item.InvExpItems;
import derekahedron.invexp.item.quiver.QuiverContentsWriter;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

/**
 * Holds Item Model Overrides for Inventory Expansion.
 */
public class InvExpItemOverrides {

    /**
     * Initializes item model overrides.
     */
    public static void init() {
        ClientServices.ITEM_OVERRIDE_REGISTRAR.register(
                InvExpItems.QUIVER,
                InvExpUtil.location("quiver/has_contents"),
                (stack, level, entity, id) -> {
                    QuiverContentsWriter contents = QuiverContentsWriter.of(stack);
                    return contents != null && !contents.isEmpty() ? 1.0F : 0.0F;
                });
        // Override bundle so it is always "filled"
        ClientServices.ITEM_OVERRIDE_REGISTRAR.register(
                () -> Items.BUNDLE,
                new ResourceLocation("filled"),
                (stack, level, entity, id) -> 1.0F);
    }
}
