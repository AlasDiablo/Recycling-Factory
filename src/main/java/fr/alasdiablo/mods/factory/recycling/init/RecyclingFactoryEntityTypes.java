package fr.alasdiablo.mods.factory.recycling.init;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.block.crusher.entity.StirlingRecyclingCrusherEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class RecyclingFactoryEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
            net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, RecyclingFactory.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StirlingRecyclingCrusherEntity>> STIRLING_RECYCLING_CRUSHER
            = BLOCK_ENTITY_TYPES.register(
            Registries.STIRLING_RECYCLING_CRUSHER,
            () -> new BlockEntityType<>(
                    StirlingRecyclingCrusherEntity::new,
                    Set.of(RecyclingFactoryBlocks.STIRLING_RECYCLING_CRUSHER.get()),
                    null
            )
    );

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);


    }
}
