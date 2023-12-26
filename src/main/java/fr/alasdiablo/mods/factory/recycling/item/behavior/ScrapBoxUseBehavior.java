package fr.alasdiablo.mods.factory.recycling.item.behavior;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ScrapBoxUseBehavior extends DefaultDispenseItemBehavior {
    private static final int SPEED = 6;

    private final ScrapBoxResultTier tier;

    public ScrapBoxUseBehavior(ScrapBoxResultTier tier) {
        this.tier = tier;
    }

    public static @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, InteractionHand usedHand, ScrapBoxResultTier tier
    ) {
        ItemStack scrapBoxStack = player.getItemInHand(usedHand);
        Vec3      direction     = player.getLookAngle();
        Position  position      = player.position();
        ItemStack generatedDrop = ScrapBoxBehavior.getResultItem(tier);

        ItemEntity entity = new ItemEntity(level, position.x(), position.y() + 1f, position.z(), generatedDrop);

        double deltaV = level.random.nextDouble() * 0.1D + 0.2D;

        entity.setDeltaMovement(
                level.random.triangle(
                        direction.x() * deltaV,
                        0.0172275D * SPEED
                ),
                level.random.triangle(
                        0.2D,
                        0.0172275D * SPEED
                ),
                level.random.triangle(
                        direction.z() * deltaV,
                        0.0172275D * SPEED
                )
        );

        level.addFreshEntity(entity);
        scrapBoxStack.shrink(1);
        return InteractionResultHolder.sidedSuccess(scrapBoxStack, level.isClientSide());
    }

    @Override
    protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack scrapBoxStack) {
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        Position  position  = DispenserBlock.getDispensePosition(source);
        scrapBoxStack.shrink(1);
        ItemStack generatedDrop = ScrapBoxBehavior.getResultItem(this.tier);
        DefaultDispenseItemBehavior.spawnItem(source.level(), generatedDrop, SPEED, direction, position);
        return scrapBoxStack;
    }
}
