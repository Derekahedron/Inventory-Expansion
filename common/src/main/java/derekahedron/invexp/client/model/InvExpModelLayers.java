package derekahedron.invexp.client.model;

import derekahedron.invexp.client.util.ClientServices;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

/**
 * Holds layer definitions for Inventory Expansion.
 */
public class InvExpModelLayers {

    public static final ModelLayerLocation QUIVER = register("quiver", QuiverModel::createQuiverLayer);

    /**
     * Registers a layer definition under a given path.
     *
     * @param path the path to register under
     * @param layerDefinition the supplier for creating a layer definition
     * @return the location of the registered definition
     */
    public static ModelLayerLocation register(String path, Supplier<LayerDefinition> layerDefinition) {
        return register(path, "main", layerDefinition);
    }

    /**
     * Registers a layer definition under a given path and layer.
     *
     * @param path the path to register under
     * @param layer the name of the layer in the layer definition
     * @param layerDefinition the supplier for creating a layer definition
     * @return the location of the registered definition
     */
    public static ModelLayerLocation register(String path, String layer, Supplier<LayerDefinition> layerDefinition) {
        ModelLayerLocation location = new ModelLayerLocation(InvExpUtil.location(path), layer);
        ClientServices.MODEL_LAYER_REGISTRAR.register(location, layerDefinition);
        return location;
    }

    /**
     * Initializes model layers.
     */
    public static void init() {
        // Do nothing: load class
    }
}
