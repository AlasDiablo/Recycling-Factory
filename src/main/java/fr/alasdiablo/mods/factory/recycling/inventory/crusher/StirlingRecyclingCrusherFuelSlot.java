package fr.alasdiablo.mods.factory.recycling.inventory.crusher;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StirlingRecyclingCrusherFuelSlot extends Slot {
    private final StirlingRecyclingCrusherMenu menu;

    public StirlingRecyclingCrusherFuelSlot(StirlingRecyclingCrusherMenu stirlingRecyclingCrusherMenu, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.menu = stirlingRecyclingCrusherMenu;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.menu.isFuel(stack);
    }
}
