package fr.alasdiablo.mods.factory.recycling.data.loot.table;

import fr.alasdiablo.diolib.api.data.loot.DioBlockLootSubProvider;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryBlocks;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

public class RecyclingFactoryBlockLootTables extends DioBlockLootSubProvider {
    public RecyclingFactoryBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(RecyclingFactoryBlocks.RUBBISH_BIN.get());
        this.dropSelf(RecyclingFactoryBlocks.STIRLING_RECYCLING_CRUSHER.get());
    }
}
