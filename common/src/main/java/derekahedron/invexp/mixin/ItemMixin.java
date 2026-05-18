package derekahedron.invexp.mixin;

import derekahedron.invexp.item.ItemDuck;
import derekahedron.invexp.item.sack.SackRuleManager;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(Item.class)
public class ItemMixin implements ItemDuck {

    @Unique
    @Nullable
    private SackRuleManager.SackRules invexp$sackRules;

    @Override
    public void invexp$setSackRules(@Nullable SackRuleManager.SackRules sackRules) {
        this.invexp$sackRules = sackRules;
    }

    @Override
    @Nullable
    public SackRuleManager.SackRules invexp$getSackRules() {
        return invexp$sackRules;
    }
}
