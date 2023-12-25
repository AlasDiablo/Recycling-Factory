package fr.alasdiablo.mods.factory.recycling.init;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class RecyclingFactoryItems {
    public static final  DeferredRegister.Items ITEMS = DeferredRegister.createItems(RecyclingFactory.MODID);

    public static final DeferredItem<Item> SCRAP = ITEMS.registerSimpleItem("scrap");

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static void onBuildCreativeTabContents(@NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == RecyclingFactory.RECYCLING_FACTORY_TAB.getKey()) {
            event.accept(SCRAP);
        }
    }
}
