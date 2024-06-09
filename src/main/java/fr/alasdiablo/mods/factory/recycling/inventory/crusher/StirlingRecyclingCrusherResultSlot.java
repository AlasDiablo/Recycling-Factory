package fr.alasdiablo.mods.factory.recycling.inventory.crusher;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StirlingRecyclingCrusherResultSlot extends Slot {
    public StirlingRecyclingCrusherResultSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
