package fr.alasdiablo.mods.factory.recycling.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TrashCanInputContainer extends SimpleContainer implements WorldlyContainer {
    private final BlockState    blockState;
    private final LevelAccessor levelAccessor;
    private final BlockPos      blockPos;
    private       boolean       changed;

    public TrashCanInputContainer(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        super(1);
        this.blockState    = blockState;
        this.levelAccessor = levelAccessor;
        this.blockPos      = blockPos;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        if (direction == Direction.UP) {
            return new int[]{ 0 };
        }
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_52028_, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        if (itemStack.isEmpty()) {
            return false;
        }
        return !this.changed && direction == Direction.UP;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return false;
    }

    @Override
    public void setChanged() {
        ItemStack itemstack = this.getItem(0);
        if (!itemstack.isEmpty()) {
            this.changed = true;
            BlockState blockstate = TrashCanBlock.addItem(null, this.blockState, this.levelAccessor, this.blockPos, itemstack);
            this.levelAccessor.levelEvent(1500, this.blockPos, blockstate != this.blockState ? 1 : 0);
            this.removeItemNoUpdate(0);
        }
    }
}
