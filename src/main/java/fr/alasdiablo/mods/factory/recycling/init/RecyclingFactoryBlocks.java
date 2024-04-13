package fr.alasdiablo.mods.factory.recycling.init;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.block.TrashCanBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RecyclingFactoryBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RecyclingFactory.MODID);

    public static final RegistryObject<Block> TRASH_CAN = register(
            Registries.TRASH_CAN, () -> new TrashCanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.6f))
    );

    private static @NotNull RegistryObject<Block> register(String name, Supplier<Block> block) {
        RegistryObject<Block> blockRegistry = BLOCKS.register(name, block);
        RecyclingFactoryItems.ITEMS.register(name, () -> new BlockItem(blockRegistry.get(), new Item.Properties()));
        return blockRegistry;
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    public static void onBuildCreativeTabContents(@NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == RecyclingFactory.RECYCLING_FACTORY_TAB.getKey()) {
            event.accept(TRASH_CAN);
        }
    }
}
