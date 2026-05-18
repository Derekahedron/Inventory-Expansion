package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IItemColorRegistrar;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ForgeItemColorRegistrar implements IItemColorRegistrar {
    private static final List<ItemColorRegistration> REGISTRATIONS = new ArrayList<>();

    @Override
    public void register(ItemColor itemColor, Supplier<Item> item) {
        REGISTRATIONS.add(new ItemColorRegistration(itemColor, item));
    }

    /**
     * Initializes the item colors.
     *
     * @param event the event to register under
     */
    public static void init(RegisterColorHandlersEvent.Item event) {
        for (ItemColorRegistration registration : REGISTRATIONS) {
            event.register(registration.itemColor, registration.item.get());
        }
    }

    public record ItemColorRegistration(ItemColor itemColor, Supplier<Item> item) {}
}
