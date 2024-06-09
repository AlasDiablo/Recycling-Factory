package fr.alasdiablo.mods.factory.recycling.data.recipe;

import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryBlocks;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public class RecyclingFactoryRecipeProvider extends RecipeProvider {
    public RecyclingFactoryRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RecyclingFactoryBlocks.RUBBISH_BIN)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Blocks.COMPOSTER)
                .pattern("I I")
                .pattern("ICI")
                .pattern("III")
                .unlockedBy("has_composter", has(Blocks.COMPOSTER))
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RecyclingFactoryBlocks.STIRLING_RECYCLING_CRUSHER)
                .define('S', Blocks.SMOOTH_STONE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('P', Blocks.PISTON)
                .define('B', Blocks.BLAST_FURNACE)
                .define('R', RecyclingFactoryBlocks.RUBBISH_BIN)
                .pattern("SDS")
                .pattern("PRP")
                .pattern("SBS")
                .unlockedBy("has_smooth_stone", has(Blocks.SMOOTH_STONE))
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .unlockedBy("has_piston", has(Blocks.PISTON))
                .unlockedBy("has_blast_furnace", has(Blocks.BLAST_FURNACE))
                .unlockedBy("has_rubbish_bin", has(RecyclingFactoryBlocks.RUBBISH_BIN))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.BASIC_SCRAP_BOX)
                .define('S', RecyclingFactoryItems.SCRAP)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .unlockedBy("has_scrap", has(RecyclingFactoryItems.SCRAP))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ADVANCED_SCRAP_BOX)
                .define('B', RecyclingFactoryItems.BASIC_SCRAP_BOX)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .unlockedBy("has_basic_scrap_box", has(RecyclingFactoryItems.BASIC_SCRAP_BOX))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ELITE_SCRAP_BOX)
                .define('A', RecyclingFactoryItems.ADVANCED_SCRAP_BOX)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy("has_advanced_scrap_box", has(RecyclingFactoryItems.ADVANCED_SCRAP_BOX))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RecyclingFactoryItems.ULTIMATE_SCRAP_BOX)
                .define('E', RecyclingFactoryItems.ELITE_SCRAP_BOX)
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .unlockedBy("has_elite_scrap_box", has(RecyclingFactoryItems.ELITE_SCRAP_BOX))
                .save(recipeOutput);
    }
}
