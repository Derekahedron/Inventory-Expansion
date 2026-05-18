package derekahedron.invexp.mixin;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemInvoker {

    @Invoker(value = "getWeight")
    static int invexp$callGetWeight(ItemStack stack) {
        throw new AssertionError();
    }

    @Invoker(value = "getContentWeight")
    static int invexp$callGetContentWeight(ItemStack stack) {
        throw new AssertionError();
    }
}
