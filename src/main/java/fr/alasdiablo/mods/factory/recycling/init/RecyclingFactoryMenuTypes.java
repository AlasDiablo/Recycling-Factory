package fr.alasdiablo.mods.factory.recycling.init;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.inventory.crusher.StirlingRecyclingCrusherMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecyclingFactoryMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            net.minecraft.core.registries.Registries.MENU, RecyclingFactory.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<StirlingRecyclingCrusherMenu>> STIRLING_RECYCLING_CRUSHER = MENU_TYPES.register(
            Registries.STIRLING_RECYCLING_CRUSHER, () -> new MenuType<>(StirlingRecyclingCrusherMenu::new, FeatureFlags.VANILLA_SET));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
