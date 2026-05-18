package derekahedron.invexp.registry;

import derekahedron.invexp.item.sack.SackType;
import derekahedron.invexp.item.sack.SackTypeRule;
import derekahedron.invexp.item.sack.SackWeightRule;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import static derekahedron.invexp.platform.Services.REGISTRY_REGISTRAR;

/**
 * Holds all Registry Keys for Inventory Expansion.
 */
public class InvExpRegistryKeys {

    public static final ResourceKey<Registry<SackType>> SACK_TYPE = of("sack_type");
    public static final ResourceKey<Registry<SackTypeRule>> SACK_TYPE_RULE = of("sack_type_rule");
    public static final ResourceKey<Registry<SackWeightRule>> SACK_WEIGHT_RULE = of("sack_weight_rule");

    /**
     * Creates an Inventory Expansion registry key under a given path.
     *
     * @param path a {@link String} to create the registry under
     * @param <T> the type of registry created
     * @return the Registry Key that was created
     */
    public static <T> ResourceKey<Registry<T>> of(String path) {
        return ResourceKey.createRegistryKey(InvExpUtil.location(path));
    }

    /**
     * Initializes registry keys.
     */
    public static void init() {
        REGISTRY_REGISTRAR.register(SACK_TYPE, SackType.CODEC, SackType.CODEC);
        REGISTRY_REGISTRAR.register(SACK_TYPE_RULE, SackTypeRule.CODEC, SackTypeRule.CODEC);
        REGISTRY_REGISTRAR.register(SACK_WEIGHT_RULE, SackWeightRule.CODEC, SackWeightRule.CODEC);
    }
}
