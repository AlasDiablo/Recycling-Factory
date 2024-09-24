package fr.alasdiablo.mods.factory.recycling.data.loot;

import fr.alasdiablo.mods.factory.recycling.data.loot.table.RecyclingFactoryBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RecyclingFactoryLootTableProvider extends LootTableProvider {
    public RecyclingFactoryLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(RecyclingFactoryBlockLootTables::new, LootContextParamSets.BLOCK)),
                lookup
        );
    }
}
