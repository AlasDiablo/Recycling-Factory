package fr.alasdiablo.mods.factory.recycling;

import com.mojang.logging.LogUtils;
import fr.alasdiablo.mods.factory.recycling.data.model.RecyclingFactoryItemModelProvider;
import fr.alasdiablo.mods.factory.recycling.data.recipe.RecyclingFactoryRecipeProvider;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryBlocks;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxBehavior;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxUseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@SuppressWarnings("deprecation")
@Mod(RecyclingFactory.MODID)
public class RecyclingFactory {
    public static final  String MODID  = "recycling_factory";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> RECYCLING_FACTORY_TAB = CREATIVE_MODE_TABS.register(
            "recycling_factory_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group." + MODID))
                    .icon(() -> RecyclingFactoryItems.SCRAP.get().getDefaultInstance())
                    .build()
    );

    public RecyclingFactory() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;

        LOGGER.debug("Add world loading listener");
        modEventBus.addListener(this::onWorldLoad);

        LOGGER.debug("Add common setup listener");
        modEventBus.addListener(this::onCommonSetup);

        LOGGER.debug("Add gather data listener");
        modEventBus.addListener(this::onGatherData);

        LOGGER.debug("Register creative tab");
        CREATIVE_MODE_TABS.register(modEventBus);

        LOGGER.debug("Register items");
        RecyclingFactoryItems.register(modEventBus);

        LOGGER.debug("Register blocks");
        RecyclingFactoryBlocks.register(modEventBus);

        LOGGER.debug("Add creative tab contents listener");
        modEventBus.addListener(RecyclingFactoryItems::onBuildCreativeTabContents);
        modEventBus.addListener(RecyclingFactoryBlocks::onBuildCreativeTabContents);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.debug("Register scrap box loot table");
        ScrapBoxBehavior.registerDrop();

        LOGGER.debug("Register basic scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.BASIC_SCRAP_BOX.get(),
                new ScrapBoxUseBehavior(ScrapBoxResultTier.BASIC)
        );

        LOGGER.debug("Register advanced scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.ADVANCED_SCRAP_BOX.get(),
                new ScrapBoxUseBehavior(ScrapBoxResultTier.ADVANCED)
        );

        LOGGER.debug("Register elite scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.ELITE_SCRAP_BOX.get(),
                new ScrapBoxUseBehavior(ScrapBoxResultTier.ELITE)
        );

        LOGGER.debug("Register ultimate scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.ULTIMATE_SCRAP_BOX.get(),
                new ScrapBoxUseBehavior(ScrapBoxResultTier.ULTIMATE)
        );
    }

    private boolean worldLoadOneTimeAction = true;

    private void onWorldLoad(LevelEvent.Load event) {
        if (this.worldLoadOneTimeAction) {
            LOGGER.debug("Generated scrap box loot table (world load)");
            ScrapBoxBehavior.updateChance();
            this.worldLoadOneTimeAction = false;
        }
    }

    private void onGatherData(@NotNull GatherDataEvent event) {
        LOGGER.debug("Start data generator");
        final DataGenerator                            generator          = event.getGenerator();
        final PackOutput         output             = generator.getPackOutput();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        LOGGER.debug("Add Item Model Provider");
        generator.addProvider(event.includeClient(), new RecyclingFactoryItemModelProvider(output, existingFileHelper));

        LOGGER.debug("Add Recipe Provider");
        generator.addProvider(event.includeServer(), new RecyclingFactoryRecipeProvider(output));
    }
}
