package fr.alasdiablo.mods.factory.recycling.block.crusher;

import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ToolsArmorCrusher extends Block implements WorldlyContainerHolder {
    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 5;
    public static final int READY     = 6;

    public ToolsArmorCrusher(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull WorldlyContainer getContainer(@NotNull BlockState blockState, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos) {
        return null;
    }
}
