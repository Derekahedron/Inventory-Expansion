package derekahedron.invexp.fabric.datagen;

import derekahedron.invexp.registry.InvExpRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class SackWeightDefaultProvider extends FabricDynamicRegistryProvider {

    public SackWeightDefaultProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Sack Weight Rules";
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        entries.addAll(provider.lookupOrThrow(InvExpRegistryKeys.SACK_WEIGHT_RULE));
    }
}
