package fr.alasdiablo.mods.factory.recycling.compatibility;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.Registries;
import fr.alasdiablo.mods.factory.recycling.api.ChanceDrop;
import mezz.jei.api.recipe.RecipeType;

public class RecipeTypes {
    public static final RecipeType<ChanceDrop> BASIC_SCRAP_BOX = RecipeType.create(
            RecyclingFactory.MODID,
            Registries.BASIC_SCRAP_BOX,
            ChanceDrop.class
    );

    public static final RecipeType<ChanceDrop> ADVANCED_SCRAP_BOX = RecipeType.create(
            RecyclingFactory.MODID,
            Registries.ADVANCED_SCRAP_BOX,
            ChanceDrop.class
    );

    public static final RecipeType<ChanceDrop> ELITE_SCRAP_BOX = RecipeType.create(
            RecyclingFactory.MODID,
            Registries.ELITE_SCRAP_BOX,
            ChanceDrop.class
    );

    public static final RecipeType<ChanceDrop> ULTIMATE_SCRAP_BOX = RecipeType.create(
            RecyclingFactory.MODID,
            Registries.ULTIMATE_SCRAP_BOX,
            ChanceDrop.class
    );
}
