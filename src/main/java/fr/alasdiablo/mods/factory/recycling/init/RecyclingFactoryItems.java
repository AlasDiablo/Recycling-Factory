package fr.alasdiablo.mods.factory.recycling.init;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.item.ScrapBox;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class RecyclingFactoryItems {
    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RecyclingFactory.MODID);

    public static final RegistryObject<Item> SCRAP = ITEMS.register(Registries.SCRAP, () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BASIC_SCRAP_BOX    = ITEMS.register(
            Registries.BASIC_SCRAP_BOX, () -> new ScrapBox(ScrapBoxResultTier.BASIC, new Item.Properties()));
    public static final RegistryObject<Item> ADVANCED_SCRAP_BOX = ITEMS.register(
            Registries.ADVANCED_SCRAP_BOX, () -> new ScrapBox(ScrapBoxResultTier.ADVANCED, new Item.Properties()));
    public static final RegistryObject<Item> ELITE_SCRAP_BOX    = ITEMS.register(
            Registries.ELITE_SCRAP_BOX, () -> new ScrapBox(ScrapBoxResultTier.ELITE, new Item.Properties()));
    public static final RegistryObject<Item> ULTIMATE_SCRAP_BOX = ITEMS.register(
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
