package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IModelLayerRegistrar;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ForgeModelLayerRegistrar implements IModelLayerRegistrar {

    private static final List<ModelLayerRegistration> REGISTRATIONS = new ArrayList<>();

    @Override
    public void register(ModelLayerLocation location, Supplier<LayerDefinition> layerDefinition) {
        REGISTRATIONS.add(new ModelLayerRegistration(location, layerDefinition));
    }

    /**
     * Initializes model layers.
     *
     * @param event the event to register under
     */
    public static void init(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (ModelLayerRegistration registration : REGISTRATIONS) {
            event.registerLayerDefinition(registration.location, registration.layerDefinition);
        }
    }

    public record ModelLayerRegistration(ModelLayerLocation location, Supplier<LayerDefinition> layerDefinition) {}
}
