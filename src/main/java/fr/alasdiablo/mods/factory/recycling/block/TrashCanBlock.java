package fr.alasdiablo.mods.factory.recycling.block;

import com.mojang.serialization.MapCodec;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class TrashCanBlock extends Block implements WorldlyContainerHolder {
    public static final  int                     MIN_LEVEL   = 0;
    public static final  int                     MAX_LEVEL   = 7;
    public static final  int                     READY       = 8;
    public static final  IntegerProperty         LEVEL       = RecyclingFactoryBlockStateProperties.LEVEL_TRASH_CAN;
    public static final  MapCodec<TrashCanBlock> CODEC       = simpleCodec(TrashCanBlock::new);
    private static final VoxelShape              OUTER_SHAPE = Shapes.block();
    private static final VoxelShape[]            SHAPES      = Util.make(new VoxelShape[9], voxelShapes -> {
        for (int i = 0; i < 8; ++i) {
            voxelShapes[i] = Shapes.join(
                    OUTER_SHAPE,
                    Block.box(
                            2.0,
                            Math.max(2, 1 + i * 2),
                            2.0,
                            14.0,
                            16.0,
                            14.0
                    ),
                    BooleanOp.ONLY_FIRST
            );
        }
        voxelShapes[8] = voxelShapes[7];
    });

    public TrashCanBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(LEVEL, MIN_LEVEL)
        );
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext
    ) {
        return SHAPES[blockState.getValue(LEVEL)];
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return OUTER_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(
            @NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext
    ) {
        return SHAPES[0];
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void onPlace(
            @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState previousBlockState, boolean p_51982_
    ) {
        if (blockState.getValue(LEVEL) == MAX_LEVEL) {
            level.scheduleTick(blockPos, blockState.getBlock(), 20);
        }
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand interactionHand,
            @NotNull BlockHitResult hitResult
    ) {
        int       i         = blockState.getValue(LEVEL);
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (i < READY && !itemstack.isEmpty()) {
            if (i < MAX_LEVEL && !level.isClientSide) {
                BlockState blockstate = TrashCanBlock.addItem(player, blockState, level, pos, itemstack);
                level.levelEvent(1500, pos, blockState != blockstate ? 1 : 0);
                player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (i == READY) {
            TrashCanBlock.extractProduce(player, blockState, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public static void extractProduce(Entity entity, BlockState blockState, @NotNull Level level, BlockPos blockPos) {
        if (!level.isClientSide) {
            Vec3       vec3       = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 1.01, 0.5).offsetRandom(level.random, 0.7F);
            ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), new ItemStack(RecyclingFactoryItems.SCRAP.get()));
            itementity.setDefaultPickUpDelay();
            level.addFreshEntity(itementity);
        }

        TrashCanBlock.empty(entity, blockState, level, blockPos);
        level.playSound(null, blockPos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    static void empty(@Nullable Entity entity, @NotNull BlockState blockState, @NotNull LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockState blockstate = blockState.setValue(LEVEL, MIN_LEVEL);
        levelAccessor.setBlock(blockPos, blockstate, 3);
        levelAccessor.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, blockstate));
    }

    static @NotNull BlockState addItem(
            @Nullable Entity entity, @NotNull BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, @NotNull ItemStack itemStack
    ) {
        int i = blockState.getValue(LEVEL);
        float chanceToGenerateTrash = switch (itemStack.getRarity()) {
            case COMMON -> 0.15f;
            case UNCOMMON -> 0.2f;
            case RARE -> 0.25f;
            case EPIC -> 0.3f;
        };
        if ((i != MIN_LEVEL) && !(levelAccessor.getRandom().nextDouble() < chanceToGenerateTrash)) {
            return blockState;
        }

        int        j          = i + 1;
        BlockState blockstate = blockState.setValue(LEVEL, j);
        levelAccessor.setBlock(blockPos, blockstate, 3);
        levelAccessor.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, blockstate));
        if (j == MAX_LEVEL) {
            levelAccessor.scheduleTick(blockPos, blockState.getBlock(), 20);
        }

        return blockstate;
    }

    @Override
    public void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (blockState.getValue(LEVEL) == MAX_LEVEL) {
            serverLevel.setBlock(blockPos, blockState.cycle(LEVEL), 3);
            serverLevel.playSound(null, blockPos, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1f, 1f);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos) {
        return blockState.getValue(LEVEL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LEVEL);
    }

    @Override
    public boolean isPathfindable(
            @NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull PathComputationType pathComputationType
    ) {
        return false;
    }

    @Override
    public @NotNull WorldlyContainer getContainer(@NotNull BlockState blockState, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos) {
        int i = blockState.getValue(LEVEL);
        if (i == READY) {
            return new TrashCanOutputContainer(blockState, levelAccessor, blockPos, new ItemStack(RecyclingFactoryItems.SCRAP.get()));
        }
        if (i < MAX_LEVEL) {
            return new TrashCanInputContainer(blockState, levelAccessor, blockPos);
        }
        return new TrashCanEmptyContainer();
    }
}
