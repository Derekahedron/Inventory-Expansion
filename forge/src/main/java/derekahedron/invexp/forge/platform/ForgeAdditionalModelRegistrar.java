package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IAdditionalModelRegistrar;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ForgeAdditionalModelRegistrar implements IAdditionalModelRegistrar {

    private static final List<Supplier<List<ModelResourceLocation>>> REGISTRATIONS = new ArrayList<>();

    @Override
    public void register(ModelResourceLocation model) {
        REGISTRATIONS.add(() -> List.of(model));
    }

    @Override
    public void register(Supplier<List<ModelResourceLocation>> models) {
        REGISTRATIONS.add(models);
    }

    /**
     * Initializes the model loading.
     *
     * @param event the event to register models under
     */
    public static void init(ModelEvent.RegisterAdditional event) {
        for (Supplier<List<ModelResourceLocation>> registration : REGISTRATIONS) {
            for (ModelResourceLocation model : registration.get()) {
                event.register(model);
            }
        }
    }
}
