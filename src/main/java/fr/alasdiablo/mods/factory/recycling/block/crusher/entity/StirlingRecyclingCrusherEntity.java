package fr.alasdiablo.mods.factory.recycling.block.crusher.entity;

import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryEntityTypes;
import fr.alasdiablo.mods.factory.recycling.inventory.StirlingRecyclingCrusherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class StirlingRecyclingCrusherEntity extends AbstractFurnaceBlockEntity {
    public StirlingRecyclingCrusherEntity(BlockPos blockPos, BlockState blockState) {
        super(RecyclingFactoryEntityTypes.STIRLING_RECYCLING_CRUSHER.get(), blockPos, blockState, RecipeType.SMOKING);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("gui.recycling_factory.stirling.crusher");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new StirlingRecyclingCrusherMenu(containerId, inventory, this, this.dataAccess);
    }
}
