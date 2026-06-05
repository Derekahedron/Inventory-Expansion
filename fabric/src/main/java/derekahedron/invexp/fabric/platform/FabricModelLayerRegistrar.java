package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IModelLayerRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class FabricModelLayerRegistrar implements IModelLayerRegistrar {

    @Override
    public void register(ModelLayerLocation location, Supplier<LayerDefinition> layerDefinition) {
        EntityModelLayerRegistry.registerModelLayer(location, layerDefinition::get);
    }
}
