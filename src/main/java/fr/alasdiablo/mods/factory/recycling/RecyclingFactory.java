package fr.alasdiablo.mods.factory.recycling;

import com.mojang.logging.LogUtils;
import fr.alasdiablo.mods.factory.recycling.config.RecyclingFactoryConfig;
import fr.alasdiablo.mods.factory.recycling.data.loot.RecyclingFactoryLootTableProvider;
import fr.alasdiablo.mods.factory.recycling.data.model.RecyclingFactoryItemModelProvider;
import fr.alasdiablo.mods.factory.recycling.data.recipe.RecyclingFactoryRecipeProvider;
import fr.alasdiablo.mods.factory.recycling.data.tag.RecyclingFactoryBlockTagsProvider;
import fr.alasdiablo.mods.factory.recycling.data.tag.RecyclingFactoryItemTagsProvider;
import fr.alasdiablo.mods.factory.recycling.gui.StirlingRecyclingCrusherScreen;
import fr.alasdiablo.mods.factory.recycling.init.*;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxBehavior;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxUseBehavior;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
@Mod(RecyclingFactory.MODID)
public class RecyclingFactory {
    public static final String MODID  = "recycling_factory";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RECYCLING_FACTORY_TAB = CREATIVE_MODE_TABS.register(
            "recycling_factory_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group." + MODID))
                    .icon(RecyclingFactoryItems.SCRAP::toStack)
                    .build()
    );

    public RecyclingFactory(@NotNull IEventBus modEventBus) {
        LOGGER.debug("Initializing configuration files");
        RecyclingFactoryConfig.init();

        LOGGER.debug("Add world loading listener");
        NeoForge.EVENT_BUS.addListener(this::onWorldLoad);

        LOGGER.debug("Register creative tab");
        CREATIVE_MODE_TABS.register(modEventBus);

        LOGGER.debug("Register entity types");
        RecyclingFactoryEntityTypes.register(modEventBus);

        LOGGER.debug("Register menu types");
        RecyclingFactoryMenuTypes.register(modEventBus);

        LOGGER.debug("Register block types");
        RecyclingFactoryBlockTypes.register(modEventBus);

        LOGGER.debug("Register items");
        RecyclingFactoryItems.register(modEventBus);

        LOGGER.debug("Register blocks");
        RecyclingFactoryBlocks.register(modEventBus);

        LOGGER.debug("Add creative tab contents listener");
        modEventBus.addListener(RecyclingFactoryItems::onBuildCreativeTabContents);
        modEventBus.addListener(RecyclingFactoryBlocks::onBuildCreativeTabContents);

        LOGGER.debug("Add screen listener");
        modEventBus.addListener(this::onMenuScreens);

        LOGGER.debug("Add capabilities listener");
        modEventBus.addListener(this::onCapabilities);

        LOGGER.debug("Add common setup listener");
        modEventBus.addListener(this::onCommonSetup);

        LOGGER.debug("Add gather data listener");
        modEventBus.addListener(this::onGatherData);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.debug("Register scrap box loot table");
        ScrapBoxBehavior.registerDrop();

        LOGGER.debug("Register basic scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.BASIC_SCRAP_BOX,
                new ScrapBoxUseBehavior(ScrapBoxResultTier.BASIC)
        );

        LOGGER.debug("Register advanced scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.ADVANCED_SCRAP_BOX,
                new ScrapBoxUseBehavior(ScrapBoxResultTier.ADVANCED)
        );

        LOGGER.debug("Register elite scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.ELITE_SCRAP_BOX,
                new ScrapBoxUseBehavior(ScrapBoxResultTier.ELITE)
        );

        LOGGER.debug("Register ultimate scrap box dispenser behavior");
        DispenserBlock.registerBehavior(
                RecyclingFactoryItems.ULTIMATE_SCRAP_BOX,
                new ScrapBoxUseBehavior(ScrapBoxResultTier.ULTIMATE)
        );
    }

    private void onMenuScreens(@NotNull RegisterMenuScreensEvent event) {
        event.register(RecyclingFactoryMenuTypes.STIRLING_RECYCLING_CRUSHER.get(), StirlingRecyclingCrusherScreen::new);
    }

    /**
     * @see net.neoforged.neoforge.capabilities.CapabilityHooks
     */
    private void onCapabilities(@NotNull RegisterCapabilitiesEvent event) {
        WorldlyContainerHolder rubbishBinBlock = (WorldlyContainerHolder) RecyclingFactoryBlocks.RUBBISH_BIN.get();
        event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, blockEntity, side) -> {
            if (side == null) {
                return new ForwardingItemHandler(
                        () -> new InvWrapper(
                                rubbishBinBlock.getContainer(
                                        level.getBlockState(pos),
                                        level,
                                        pos
                                )
                        )
                );
            } else {
                return new ForwardingItemHandler(
                        () -> new SidedInvWrapper(
                                rubbishBinBlock.getContainer(
                                        level.getBlockState(pos),
                                        level,
                                        pos
                                ),
                                side
                        )
                );
            }
        }, RecyclingFactoryBlocks.RUBBISH_BIN.get());

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                RecyclingFactoryEntityTypes.STIRLING_RECYCLING_CRUSHER.get(),
                (sidedContainer, side) -> side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side)
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
        final PackOutput                               output             = generator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookup             = event.getLookupProvider();
        final ExistingFileHelper                       existingFileHelper = event.getExistingFileHelper();

        LOGGER.debug("Add Item Model Provider");
        generator.addProvider(event.includeClient(), new RecyclingFactoryItemModelProvider(output, existingFileHelper));

        LOGGER.debug("Add Recipe Provider");
        generator.addProvider(event.includeServer(), new RecyclingFactoryRecipeProvider(output));

        LOGGER.debug("Add LootTable Provider");
        generator.addProvider(event.includeServer(), new RecyclingFactoryLootTableProvider(output));

        LOGGER.debug("Add Tags Provider");
        final RecyclingFactoryBlockTagsProvider blockTagsProvider = new RecyclingFactoryBlockTagsProvider(output, lookup, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeClient(), new RecyclingFactoryItemTagsProvider(output, lookup, blockTagsProvider, existingFileHelper));
    }
}
