package fr.alasdiablo.mods.factory.recycling.data.loot.table;

import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryBlocks;
import fr.alasdiablo.mods.lib.api.data.loot.DioBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

public class RecyclingFactoryBlockLootTables extends DioBlockLootSubProvider {
    public RecyclingFactoryBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(RecyclingFactoryBlocks.RUBBISH_BIN.get());
        this.dropSelf(RecyclingFactoryBlocks.STIRLING_RECYCLING_CRUSHER.get());
    }
}
