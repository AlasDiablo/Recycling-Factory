package fr.alasdiablo.mods.factory.recycling.block;

import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.block.crusher.ToolsArmorCrusher;
import fr.alasdiablo.mods.factory.recycling.block.rubbish.RubbishBinBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RecyclingFactoryBlockStateProperties {
    public static final IntegerProperty LEVEL_RUBBISH_BIN = IntegerProperty.create(
            Registries.LEVEL_RUBBISH_BIN, RubbishBinBlock.MIN_LEVEL, RubbishBinBlock.READY);

    public static final IntegerProperty LEVEL_CRUSHER = IntegerProperty.create(
            Registries.LEVEL_CRUSHER, ToolsArmorCrusher.MIN_LEVEL, ToolsArmorCrusher.READY);
    public static final IntegerProperty ITEM_TYPE_CRUSHER = IntegerProperty.create(Registries.ITEM_TYPE_CRUSHER, 0, 127);
    public static final IntegerProperty MIN_DROP_CRUSHER = IntegerProperty.create(Registries.MIN_DROP_CRUSHER, 0, 127);
    public static final IntegerProperty MAX_DROP_CRUSHER = IntegerProperty.create(Registries.MAX_DROP_CRUSHER, 0, 127);
}
