package fr.alasdiablo.mods.factory.recycling.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class StirlingRecyclingCrusherMenu extends AbstractFurnaceMenu {
    public StirlingRecyclingCrusherMenu(int containerId, Inventory inventory) {
        super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.FURNACE, containerId, inventory);
    }

    public StirlingRecyclingCrusherMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(MenuType.SMOKER, RecipeType.SMOKING, RecipeBookType.FURNACE, containerId, inventory, container, containerData);
    }
}
