package fr.alasdiablo.mods.factory.recycling.block.rubbish;

import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RubbishBinEmptyContainer extends SimpleContainer implements WorldlyContainer {
    public RubbishBinEmptyContainer() {
        super(0);
    }

    @Contract(value = "_ -> new", pure = true)
    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_19235_, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return false;
    }
}
