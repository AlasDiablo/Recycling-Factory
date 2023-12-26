package fr.alasdiablo.mods.factory.recycling.init;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.item.ScrapBox;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class RecyclingFactoryItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RecyclingFactory.MODID);

    public static final DeferredItem<Item> SCRAP = ITEMS.registerSimpleItem(Registries.SCRAP);

    public static final DeferredItem<Item> BASIC_SCRAP_BOX    = ITEMS.register(
            Registries.BASIC_SCRAP_BOX, () -> new ScrapBox(ScrapBoxResultTier.BASIC, new Item.Properties()));
    public static final DeferredItem<Item> ADVANCED_SCRAP_BOX = ITEMS.register(
            Registries.ADVANCED_SCRAP_BOX, () -> new ScrapBox(ScrapBoxResultTier.ADVANCED, new Item.Properties()));
    public static final DeferredItem<Item> ELITE_SCRAP_BOX    = ITEMS.register(
            Registries.ELITE_SCRAP_BOX, () -> new ScrapBox(ScrapBoxResultTier.ELITE, new Item.Properties()));
    public static final DeferredItem<Item> ULTIMATE_SCRAP_BOX = ITEMS.register(
            Registries.ULTIMATE_SCRAP_BOX, () -> new ScrapBox(ScrapBoxResultTier.ULTIMATE, new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static void onBuildCreativeTabContents(@NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == RecyclingFactory.RECYCLING_FACTORY_TAB.getKey()) {
            event.accept(SCRAP);
            event.accept(BASIC_SCRAP_BOX);
            event.accept(ADVANCED_SCRAP_BOX);
            event.accept(ELITE_SCRAP_BOX);
            event.accept(ULTIMATE_SCRAP_BOX);
        }
    }
}
