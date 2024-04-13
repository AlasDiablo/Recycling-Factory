package fr.alasdiablo.mods.factory.recycling.data.recipe;

import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryBlocks;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class RecyclingFactoryRecipeProvider extends RecipeProvider {
    public RecyclingFactoryRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RecyclingFactoryBlocks.TRASH_CAN.get())
                .define('I', Items.IRON_INGOT)
                .define('C', Blocks.COMPOSTER)
                .pattern("I I")
                .pattern("ICI")
                .pattern("III")
                .unlockedBy("has_composter", has(Blocks.COMPOSTER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.BASIC_SCRAP_BOX.get())
                .define('S', RecyclingFactoryItems.SCRAP.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .unlockedBy("has_scrap", has(RecyclingFactoryItems.SCRAP.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ADVANCED_SCRAP_BOX.get())
                .define('B', RecyclingFactoryItems.BASIC_SCRAP_BOX.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .unlockedBy("has_basic_scrap_box", has(RecyclingFactoryItems.BASIC_SCRAP_BOX.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ELITE_SCRAP_BOX.get())
                .define('A', RecyclingFactoryItems.ADVANCED_SCRAP_BOX.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy("has_advanced_scrap_box", has(RecyclingFactoryItems.ADVANCED_SCRAP_BOX.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ULTIMATE_SCRAP_BOX.get())
                .define('E', RecyclingFactoryItems.ELITE_SCRAP_BOX.get())
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .unlockedBy("has_elite_scrap_box", has(RecyclingFactoryItems.ELITE_SCRAP_BOX.get()))
                .save(recipeOutput);
    }
}
