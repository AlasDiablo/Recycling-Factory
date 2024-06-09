package fr.alasdiablo.mods.factory.recycling.block.crusher.entity;

import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.block.crusher.StirlingRecyclingCrusher;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryEntityTypes;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import fr.alasdiablo.mods.factory.recycling.inventory.crusher.StirlingRecyclingCrusherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StirlingRecyclingCrusherEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
    private static final int SLOT_INPUT  = 0;
    private static final int SLOT_FUEL   = 1;
    private static final int SLOT_RESULT = 2;

    private static final int[] SLOTS_FOR_UP    = new int[]{ 0 };
    private static final int[] SLOTS_FOR_DOWN  = new int[]{ 2, 1 };
    private static final int[] SLOTS_FOR_SIDES = new int[]{ 1 };

    public static final int DATA_LIT_TIME           = 0;
    public static final int DATA_LIT_DURATION       = 1;
    public static final int DATA_COOKING_PROGRESS   = 2;
    public static final int DATA_COOKING_TOTAL_TIME = 3;
    public static final int BURN_TIME_STANDARD      = 200;
    public static final int BURN_COOL_SPEED         = 2;

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int                    litTime;
    private int                    litDuration;
    private int                    cookingProgress;
    private int                    cookingTotalTime;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_LIT_TIME -> {
                    if (litDuration > Short.MAX_VALUE) {
                        yield Mth.floor(((double) litTime / litDuration) * Short.MAX_VALUE);
                    }
                    yield litTime;
                }
                case DATA_LIT_DURATION -> Math.min(litDuration, Short.MAX_VALUE);
                case DATA_COOKING_PROGRESS -> cookingProgress;
                case DATA_COOKING_TOTAL_TIME -> cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_LIT_TIME:
                    litDuration = value;
                    break;
                case DATA_LIT_DURATION:
                    litTime = value;
                    break;
                case DATA_COOKING_PROGRESS:
                    cookingProgress = value;
                    break;
                case DATA_COOKING_TOTAL_TIME:
                    cookingTotalTime = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };


    public StirlingRecyclingCrusherEntity(BlockPos blockPos, BlockState blockState) {
        super(RecyclingFactoryEntityTypes.STIRLING_RECYCLING_CRUSHER.get(), blockPos, blockState);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("gui.recycling_factory." + Registries.STIRLING_RECYCLING_CRUSHER);
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new StirlingRecyclingCrusherMenu(containerId, inventory, this, this.dataAccess);
    }

    private int getBurnDuration(@NotNull ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        return fuel.getBurnTime(RecipeType.SMELTING);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
        this.litTime          = compoundTag.getInt("BurnTime");
        this.cookingProgress  = compoundTag.getInt("CookTime");
        this.cookingTotalTime = compoundTag.getInt("CookTimeTotal");
        this.litDuration      = this.getBurnDuration(this.items.get(1));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("BurnTime", this.litTime);
        compoundTag.putInt("CookTime", this.cookingProgress);
        compoundTag.putInt("CookTimeTotal", this.cookingTotalTime);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    public static void serverTick(Level world, BlockPos blockPos, BlockState blockState, @NotNull StirlingRecyclingCrusherEntity blockEntity) {
        boolean isLit     = blockEntity.isLit();
        boolean hasChange = false;

        if (isLit) {
            --blockEntity.litTime;
        }

        ItemStack fuel     = blockEntity.items.get(SLOT_FUEL);
        boolean   hasInput = !blockEntity.items.get(SLOT_INPUT).isEmpty();
        boolean   hasFuel  = !fuel.isEmpty();

        if (blockEntity.isLit() || hasInput && hasFuel) {

            if (!blockEntity.isLit() && blockEntity.canBurn(blockEntity.items)) {
                blockEntity.litTime     = blockEntity.getBurnDuration(fuel);
                blockEntity.litDuration = blockEntity.litTime;

                if (blockEntity.isLit()) {
                    hasChange = true;

                    if (fuel.hasCraftingRemainingItem()) {
                        blockEntity.items.set(SLOT_FUEL, fuel.getCraftingRemainingItem());
                    } else {
                        if (hasFuel) {
                            fuel.shrink(1);
                            if (fuel.isEmpty()) {
                                blockEntity.items.set(SLOT_FUEL, fuel.getCraftingRemainingItem());
                            }
                        }
                    }
                }
            }

            if (blockEntity.isLit() && blockEntity.canBurn(blockEntity.items)) {
                ++blockEntity.cookingProgress;
                if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
                    blockEntity.cookingProgress  = 0;
                    blockEntity.cookingTotalTime = BURN_TIME_STANDARD;
                    blockEntity.burn(blockEntity.items, world.random);
                }
            } else {
                blockEntity.cookingProgress = 0;
            }
        } else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress = Mth.clamp(
                    blockEntity.cookingProgress - BURN_COOL_SPEED, 0, blockEntity.cookingTotalTime
            );
        }

        if (isLit != blockEntity.isLit()) {
            hasChange  = true;
            blockState = blockState.setValue(StirlingRecyclingCrusher.LIT, blockEntity.isLit());
            world.setBlock(blockPos, blockState, 3);
        }

        if (hasChange) {
            setChanged(world, blockPos, blockState);
        }
    }

    private boolean canBurn(@NotNull NonNullList<ItemStack> itemStacks) {
        ItemStack input  = itemStacks.get(SLOT_INPUT);
        ItemStack result = itemStacks.get(SLOT_RESULT);

        if (!input.isEmpty()) {
            if (result.isEmpty()) {
                return true;
            }
            if (!result.is(RecyclingFactoryItems.SCRAP.asItem())) {
                return false;
            }
            return result.getCount() < result.getMaxStackSize();
        }

        return false;
    }

    private void burn(NonNullList<ItemStack> itemStacks, RandomSource randomSource) {
        if (this.canBurn(itemStacks)) {
            ItemStack input  = itemStacks.get(SLOT_INPUT);
            ItemStack result = itemStacks.get(SLOT_RESULT);

            float chanceToGenerateScrap = switch (input.getRarity()) {
                case COMMON -> 0.15f;
                case UNCOMMON -> 0.2f;
                case RARE -> 0.25f;
                case EPIC -> 0.3f;
            };

            if (randomSource.nextDouble() < chanceToGenerateScrap) {
                if (result.isEmpty()) {
                    itemStacks.set(
                            SLOT_RESULT,
                            new ItemStack(RecyclingFactoryItems.SCRAP.asItem()).copy()
                    );
                } else if (result.is(RecyclingFactoryItems.SCRAP.asItem())) {
                    result.grow(1);
                }
            }

            input.shrink(1);
        }
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        }
        return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            return itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET);
        }
        return true;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack itemStack) {
        ItemStack slotItemStack = this.items.get(slot);
        boolean flag = !itemStack.isEmpty() && ItemStack.isSameItemSameTags(slotItemStack, itemStack);
        this.items.set(slot, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if (slot == 0 && !flag) {
            this.cookingTotalTime = BURN_TIME_STANDARD;
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void fillStackedContents(@NotNull StackedContents contents) {
        for(ItemStack itemStack : this.items) {
            contents.accountStack(itemStack);
        }
    }
}
