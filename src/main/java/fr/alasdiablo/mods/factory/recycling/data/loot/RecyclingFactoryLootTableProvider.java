package fr.alasdiablo.mods.factory.recycling.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import fr.alasdiablo.mods.factory.recycling.data.loot.table.RecyclingFactoryBlockLootTables;

import java.util.List;
import java.util.Set;

public class RecyclingFactoryLootTableProvider extends LootTableProvider {
    public RecyclingFactoryLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(RecyclingFactoryBlockLootTables::new, LootContextParamSets.BLOCK)));
    }
}
