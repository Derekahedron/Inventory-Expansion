package derekahedron.invexp.block.dispenser;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

/**
 * Reimplements {@link net.minecraft.core.dispenser.OptionalDispenseItemBehavior} to allow extending the dispense method.
 */
public class ContainerItemDispenseItemBehavior implements DispenseItemBehavior {

    private boolean success = true;

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    protected void playSound(BlockSource source) {
        source.getLevel().levelEvent(this.isSuccess() ? 1000 : 1001, source.getPos(), 0);
    }

    @Override
    public ItemStack dispense(BlockSource source, ItemStack stack) {
        ItemStack itemstack = this.execute(source, stack);
        this.playSound(source);
        this.playAnimation(source, source.getBlockState().getValue(DispenserBlock.FACING));
        return itemstack;
    }

    protected ItemStack execute(BlockSource source, ItemStack stack) {
        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
        Position position = DispenserBlock.getDispensePosition(source);
        ItemStack itemstack = stack.split(1);
        spawnItem(source.getLevel(), itemstack, 6, direction, position);
        return stack;
    }

    public static void spawnItem(
            Level level,
            ItemStack stack,
            int speed,
            Direction facing,
            Position position) {
        double x = position.x();
        double y = position.y();
        double z = position.z();
        if (facing.getAxis() == Direction.Axis.Y) {
            y -= 0.125F;
        } else {
            y -= 0.15625F;
        }

        ItemEntity itementity = new ItemEntity(level, x, y, z, stack);
        double randomness = level.random.nextDouble() * 0.1 + 0.2;
        itementity.setDeltaMovement(
                level.random.triangle(facing.getStepX() * randomness, 0.0172275 * speed),
                level.random.triangle(0.2, 0.0172275 * speed),
                level.random.triangle(facing.getStepZ() * randomness, 0.0172275 * speed));
        level.addFreshEntity(itementity);
    }

    protected void playAnimation(BlockSource source, Direction facing) {
        source.getLevel().levelEvent(2000, source.getPos(), facing.get3DDataValue());
    }
}
