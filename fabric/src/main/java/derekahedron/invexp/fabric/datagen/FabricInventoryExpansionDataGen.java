package derekahedron.invexp.fabric.datagen;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.datagen.InvExpItemTagProvider;
import derekahedron.invexp.datagen.recipe.InvExpRecipeProvider;
import derekahedron.invexp.item.sack.SackTypeRules;
import derekahedron.invexp.item.sack.SackTypes;
import derekahedron.invexp.item.sack.SackWeightRules;
import derekahedron.invexp.registry.InvExpRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import org.jetbrains.annotations.Nullable;

public class FabricInventoryExpansionDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(FabricInvExpModelProvider::new);
        pack.addProvider((FabricDataGenerator.Pack.Factory<InvExpRecipeProvider>) InvExpRecipeProvider::new);
        pack.addProvider(SackTypeProvider::new);
        pack.addProvider(SackTypeDefaultProvider::new);
        pack.addProvider(SackWeightDefaultProvider::new);
        pack.addProvider(InvExpItemTagProvider::new);
    }

    public @Nullable String getEffectiveModId() {
        return InventoryExpansion.MOD_ID;
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(InvExpRegistryKeys.SACK_TYPE, SackTypes::bootstrap);
        registryBuilder.add(InvExpRegistryKeys.SACK_TYPE_RULE, SackTypeRules::bootstrap);
        registryBuilder.add(InvExpRegistryKeys.SACK_WEIGHT_RULE, SackWeightRules::bootstrap);
    }
}
