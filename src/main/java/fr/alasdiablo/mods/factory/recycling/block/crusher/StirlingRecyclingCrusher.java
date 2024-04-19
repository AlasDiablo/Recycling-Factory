package fr.alasdiablo.mods.factory.recycling.block.crusher;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StirlingRecyclingCrusher extends AbstractFurnaceBlock {
    public static final MapCodec<StirlingRecyclingCrusher> CODEC = simpleCodec(StirlingRecyclingCrusher::new);

    public StirlingRecyclingCrusher(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<StirlingRecyclingCrusher> codec() {
        return CODEC;
    }

    @Override
    protected void openContainer(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player) {

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return null;
    }
}
