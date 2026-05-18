package derekahedron.invexp.platform;

import derekahedron.invexp.platform.services.*;

import java.util.ServiceLoader;

/**
 * Hold services used to provide compatibility between the forge and fabric.
 */
public class Services {

    public static final IItemRegistrar ITEM_REGISTRAR = load(IItemRegistrar.class);
    public static final ISoundEventRegistrar SOUND_EVENT_REGISTRAR = load(ISoundEventRegistrar.class);
    public static final IRecipeSerializerRegistrar RECIPE_SERIALIZER_REGISTRAR = load(IRecipeSerializerRegistrar.class);
    public static final IRegistryRegistrar REGISTRY_REGISTRAR = load(IRegistryRegistrar.class);
    public static final IPacketRegistrar PACKET_REGISTRAR = load(IPacketRegistrar.class);

    public static final INetworkHandler NETWORK_HANDLER = load(INetworkHandler.class);
    public static final IRegistryEventRegistrar REGISTRY_EVENT_REGISTRAR = load(IRegistryEventRegistrar.class);
    public static final ICreativeItemsRegistrar CREATIVE_ITEMS_REGISTRAR = load(ICreativeItemsRegistrar.class);
    public static final IIngredientProvider INGREDIENT_PROVIDER = load(IIngredientProvider.class);
    public static final IGameplayHooks GAMEPLAY_HOOKS = load(IGameplayHooks.class);

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}