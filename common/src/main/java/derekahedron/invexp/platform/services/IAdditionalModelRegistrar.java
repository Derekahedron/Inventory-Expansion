package derekahedron.invexp.platform.services;

import net.minecraft.client.resources.model.ModelResourceLocation;

import java.util.List;
import java.util.function.Supplier;

/**
 * Service for registering additional models to be loaded for items.
 */
public interface IAdditionalModelRegistrar {

    /**
     * Registers the given location.
     *
     * @param model location to load
     */
    void register(ModelResourceLocation model);

    /**
     * Registers the given locations at once.
     *
     * @param models a supplier to a list of models to register
     */
    void register(Supplier<List<ModelResourceLocation>> models);
}
