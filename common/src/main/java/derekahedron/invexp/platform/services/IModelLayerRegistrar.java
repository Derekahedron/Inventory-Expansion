package derekahedron.invexp.platform.services;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

/**
 * Service for registering model layers.
 */
public interface IModelLayerRegistrar {

    /**
     * Register a model layer definition under a model layer location.
     *
     * @param location the location to register the definition under
     * @param layerDefinition a supplier for the definition
     */
    void register(ModelLayerLocation location, Supplier<LayerDefinition> layerDefinition);
}
