package fr.alasdiablo.mods.factory.recycling.data.recipe;

import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryBlocks;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecyclingFactoryRecipeProvider extends RecipeProvider {
    public RecyclingFactoryRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RecyclingFactoryBlocks.TRASH_CAN.asItem())
                .define('I', Items.IRON_INGOT)
                .define('C', Blocks.COMPOSTER)
                .pattern("I I")
                .pattern("ICI")
                .pattern("III")
                .unlockedBy("has_composter", has(Blocks.COMPOSTER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.BASIC_SCRAP_BOX.asItem())
                .define('S', RecyclingFactoryItems.SCRAP.asItem())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .unlockedBy("has_scrap", has(RecyclingFactoryItems.SCRAP.asItem()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ADVANCED_SCRAP_BOX.asItem())
                .define('B', RecyclingFactoryItems.BASIC_SCRAP_BOX.asItem())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .unlockedBy("has_basic_scrap_box", has(RecyclingFactoryItems.BASIC_SCRAP_BOX.asItem()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ELITE_SCRAP_BOX.asItem())
                .define('A', RecyclingFactoryItems.ADVANCED_SCRAP_BOX.asItem())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy("has_advanced_scrap_box", has(RecyclingFactoryItems.ADVANCED_SCRAP_BOX.asItem()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ULTIMATE_SCRAP_BOX.asItem())
                .define('E', RecyclingFactoryItems.ELITE_SCRAP_BOX.asItem())
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .unlockedBy("has_elite_scrap_box", has(RecyclingFactoryItems.ELITE_SCRAP_BOX.asItem()))
                .save(recipeOutput);
    }
}
