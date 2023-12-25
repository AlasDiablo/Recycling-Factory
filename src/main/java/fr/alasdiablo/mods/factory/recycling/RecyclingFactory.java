package fr.alasdiablo.mods.factory.recycling;

import com.mojang.logging.LogUtils;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(RecyclingFactory.MODID)
public class RecyclingFactory {
    public static final  String MODID  = "recycling_factory";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RECYCLING_FACTORY_TAB = CREATIVE_MODE_TABS.register(
            "recycling_factory_tab", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> RecyclingFactoryItems.SCRAP.get().getDefaultInstance())
                    .build()
    );

    public RecyclingFactory(@NotNull IEventBus modEventBus) {
        LOGGER.debug("Register creative tab");
        CREATIVE_MODE_TABS.register(modEventBus);

        LOGGER.debug("Register items");
        RecyclingFactoryItems.register(modEventBus);

        LOGGER.debug("Add creative tab contents listener");
        modEventBus.addListener(RecyclingFactoryItems::onBuildCreativeTabContents);
    }
}
