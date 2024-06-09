package fr.alasdiablo.mods.factory.recycling.block.crusher;

import com.mojang.serialization.MapCodec;
import fr.alasdiablo.mods.factory.recycling.block.crusher.entity.StirlingRecyclingCrusherEntity;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StirlingRecyclingCrusher extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty   LIT    = BlockStateProperties.LIT;

    public static final MapCodec<StirlingRecyclingCrusher> CODEC = simpleCodec(StirlingRecyclingCrusher::new);

    public StirlingRecyclingCrusher(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE)
        );
    }

    @Override
    protected @NotNull MapCodec<StirlingRecyclingCrusher> codec() {
        return CODEC;
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand,
            @NotNull BlockHitResult result
    ) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof StirlingRecyclingCrusherEntity) {
            player.openMenu((MenuProvider) blockEntity);
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(
            @NotNull Level world, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable LivingEntity player, @NotNull ItemStack itemStack
    ) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof StirlingRecyclingCrusherEntity) {
                ((StirlingRecyclingCrusherEntity) blockEntity).setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(
            @NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos blockPos, @NotNull BlockState newBlockState, boolean movedByPiston
    ) {
        if (!blockState.is(newBlockState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof StirlingRecyclingCrusherEntity) {
                if (world instanceof ServerLevel) {
                    Containers.dropContents(world, blockPos, (StirlingRecyclingCrusherEntity) blockEntity);
                }

                super.onRemove(blockState, world, blockPos, newBlockState, movedByPiston);
                world.updateNeighbourForOutputSignal(blockPos, this);
                return;
            }

            super.onRemove(blockState, world, blockPos, newBlockState, movedByPiston);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(blockPos));
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(@NotNull BlockState blockState, @NotNull Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(@NotNull BlockState blockState, @NotNull Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, LIT);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StirlingRecyclingCrusherEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level world, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType
    ) {
        if (world.isClientSide) {
            return null;
        }
        return createTickerHelper(blockEntityType, RecyclingFactoryEntityTypes.STIRLING_RECYCLING_CRUSHER.get(), StirlingRecyclingCrusherEntity::serverTick);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        if (blockState.getValue(LIT)) {
            double x = blockPos.getX() + 0.5;
            double y = blockPos.getY();
            double z = blockPos.getZ() + 0.5;

            if (random.nextDouble() < 0.1) { // TODO Add crusher sounds on top of the fire crackle
                world.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1, 1, false);
            }

            Direction      direction = blockState.getValue(FACING);
            Direction.Axis axis      = direction.getAxis();

            double directionScale = 0.52;
            double randomValue    = random.nextDouble() * 0.6 - 0.3;

            double randomX = axis == Direction.Axis.X ? direction.getStepX() * directionScale : randomValue;
            double randomY = random.nextDouble() * 9.0 / 16.0;
            double randomZ = axis == Direction.Axis.Z ? direction.getStepZ() * directionScale : randomValue;

            world.addParticle(ParticleTypes.SMOKE, x + randomX, y + randomY, z + randomZ, 0, 0, 0);
        }
    }
}
