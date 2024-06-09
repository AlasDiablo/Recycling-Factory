package fr.alasdiablo.mods.factory.recycling.init;

import com.mojang.serialization.MapCodec;
import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.block.crusher.StirlingRecyclingCrusherBlock;
import fr.alasdiablo.mods.factory.recycling.block.rubbish.RubbishBinBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecyclingFactoryBlockTypes {
    private static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(
            net.minecraft.core.registries.Registries.BLOCK_TYPE, RecyclingFactory.MODID
    );

    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<RubbishBinBlock>> RUBBISH_BIN = BLOCK_TYPES.register(
            Registries.RUBBISH_BIN, () -> RubbishBinBlock.CODEC);

    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<StirlingRecyclingCrusherBlock>> STIRLING_RECYCLING_CRUSHER = BLOCK_TYPES.register(
            Registries.STIRLING_RECYCLING_CRUSHER, () -> StirlingRecyclingCrusherBlock.CODEC);

    public static void register(IEventBus bus) {
        BLOCK_TYPES.register(bus);
    }
}
