package fr.alasdiablo.mods.factory.recycling.block;

import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.block.rubbish.RubbishBinBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RecyclingFactoryBlockStateProperties {
    public static final IntegerProperty LEVEL_RUBBISH_BIN = IntegerProperty.create(
            Registries.LEVEL_RUBBISH_BIN, RubbishBinBlock.MIN_LEVEL, RubbishBinBlock.READY);
}
