package fr.alasdiablo.mods.factory.recycling.block;

import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TrashCanOutputContainer extends SimpleContainer implements WorldlyContainer {
    private final BlockState    blockState;
    private final LevelAccessor levelAccessor;
    private final BlockPos      blockPos;
    private       boolean       changed;

    public TrashCanOutputContainer(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, ItemStack itemStack) {
        super(itemStack);
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
        if (direction == Direction.DOWN) {
            return new int[]{ 0 };
        }
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_52028_, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return !this.changed && direction == Direction.DOWN && itemStack.is(RecyclingFactoryItems.SCRAP.get());
    }

    @Override
    public void setChanged() {
        TrashCanBlock.empty(null, this.blockState, this.levelAccessor, this.blockPos);
        this.changed = true;
    }
}
