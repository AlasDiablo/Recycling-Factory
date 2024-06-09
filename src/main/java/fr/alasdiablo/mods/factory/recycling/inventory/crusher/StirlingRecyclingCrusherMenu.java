package fr.alasdiablo.mods.factory.recycling.inventory.crusher;

import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryMenuTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class StirlingRecyclingCrusherMenu extends AbstractContainerMenu {
    public static final  int           INGREDIENT_SLOT  = 0;
    public static final  int           FUEL_SLOT        = 1;
    public static final  int           RESULT_SLOT      = 2;
    public static final  int           SLOT_COUNT       = 3;
    public static final  int           DATA_COUNT       = 4;
    private static final int           INV_SLOT_START   = 3;
    private static final int           USE_ROW_SLOT_END = 39;
    private final        Container     container;
    private final        ContainerData data;
    protected final      Level         world;


    public StirlingRecyclingCrusherMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public StirlingRecyclingCrusherMenu(
            int containerId, @NotNull Inventory playerInventory, Container recyclingCrusherContainer, ContainerData recyclingCrusherData
    ) {
        super(RecyclingFactoryMenuTypes.STIRLING_RECYCLING_CRUSHER.get(), containerId);
        checkContainerSize(recyclingCrusherContainer, SLOT_COUNT);
        checkContainerDataCount(recyclingCrusherData, DATA_COUNT);
        this.container = recyclingCrusherContainer;
        this.data      = recyclingCrusherData;
        this.world     = playerInventory.player.level();

        this.addSlot(new Slot(recyclingCrusherContainer, INGREDIENT_SLOT, 56, 17));
        this.addSlot(new StirlingRecyclingCrusherFuelSlot(this, recyclingCrusherContainer, FUEL_SLOT, 56, 53));
        this.addSlot(new StirlingRecyclingCrusherResultSlot(recyclingCrusherContainer, RESULT_SLOT, 116, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(recyclingCrusherData);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack returnStack = ItemStack.EMPTY;
        Slot      slot        = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            returnStack = slotStack.copy();
            if (index == RESULT_SLOT) {
                if (!this.moveItemStackTo(slotStack, DATA_COUNT, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, returnStack);
            } else if (index != INGREDIENT_SLOT && index != FUEL_SLOT) {

                if (this.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, FUEL_SLOT, RESULT_SLOT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, INGREDIENT_SLOT, FUEL_SLOT, false)) {
                    return ItemStack.EMPTY;
                }


            } else if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == slotStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return returnStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }

    protected boolean isFuel(@NotNull ItemStack pStack) {
        return pStack.getBurnTime(RecipeType.SMELTING) > 0;
    }

    public float getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? Mth.clamp((float) i / (float) j, 0.0F, 1.0F) : 0.0F;
    }

    public float getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return Mth.clamp((float) this.data.get(0) / (float) i, 0.0F, 1.0F);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }
}
