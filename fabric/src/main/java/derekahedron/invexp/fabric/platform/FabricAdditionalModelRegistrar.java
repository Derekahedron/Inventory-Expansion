package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IAdditionalModelRegistrar;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabricAdditionalModelRegistrar implements IAdditionalModelRegistrar {

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
     */
    public static void init() {
        ModelLoadingPlugin.register(pluginContext -> {
            for (Supplier<List<ModelResourceLocation>> registration : REGISTRATIONS) {
                pluginContext.addModels(registration.get());
            }
        });
    }
}
