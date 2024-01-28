package fr.alasdiablo.mods.factory.recycling.compatibility;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.compatibility.scrapbox.ScrapBoxCategory;
import fr.alasdiablo.mods.factory.recycling.compatibility.scrapbox.ScrapBoxRecipes;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(RecyclingFactory.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ScrapBoxCategory(
                RecipeTypes.BASIC_SCRAP_BOX,
                RecyclingFactoryItems.BASIC_SCRAP_BOX,
                ScrapBoxResultTier.BASIC,
                registration.getJeiHelpers().getGuiHelper()
        ));

        registration.addRecipeCategories(new ScrapBoxCategory(
                RecipeTypes.ADVANCED_SCRAP_BOX,
                RecyclingFactoryItems.ADVANCED_SCRAP_BOX,
                ScrapBoxResultTier.ADVANCED,
                registration.getJeiHelpers().getGuiHelper()
        ));

        registration.addRecipeCategories(new ScrapBoxCategory(
                RecipeTypes.ELITE_SCRAP_BOX,
                RecyclingFactoryItems.ELITE_SCRAP_BOX,
                ScrapBoxResultTier.ELITE,
                registration.getJeiHelpers().getGuiHelper()
        ));

        registration.addRecipeCategories(new ScrapBoxCategory(
                RecipeTypes.ULTIMATE_SCRAP_BOX,
                RecyclingFactoryItems.ULTIMATE_SCRAP_BOX,
                ScrapBoxResultTier.ULTIMATE,
                registration.getJeiHelpers().getGuiHelper()
        ));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addRecipes(
                RecipeTypes.BASIC_SCRAP_BOX,
                ScrapBoxRecipes.getRecipes(
                        ScrapBoxResultTier.BASIC,
                        registration.getIngredientManager()
                )
        );

        registration.addRecipes(
                RecipeTypes.ADVANCED_SCRAP_BOX,
                ScrapBoxRecipes.getRecipes(
                        ScrapBoxResultTier.ADVANCED,
                        registration.getIngredientManager()
                )
        );

        registration.addRecipes(
                RecipeTypes.ELITE_SCRAP_BOX,
                ScrapBoxRecipes.getRecipes(
                        ScrapBoxResultTier.ELITE,
                        registration.getIngredientManager()
                )
        );

        registration.addRecipes(
                RecipeTypes.ULTIMATE_SCRAP_BOX,
                ScrapBoxRecipes.getRecipes(
                        ScrapBoxResultTier.ULTIMATE,
                        registration.getIngredientManager()
                )
        );
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.DISPENSER), RecipeTypes.BASIC_SCRAP_BOX);
        registration.addRecipeCatalyst(
                new ItemStack(RecyclingFactoryItems.BASIC_SCRAP_BOX.asItem()),
                RecipeTypes.BASIC_SCRAP_BOX
        );

        registration.addRecipeCatalyst(new ItemStack(Blocks.DISPENSER), RecipeTypes.ADVANCED_SCRAP_BOX);
        registration.addRecipeCatalyst(
                new ItemStack(RecyclingFactoryItems.ADVANCED_SCRAP_BOX.asItem()),
                RecipeTypes.ADVANCED_SCRAP_BOX
        );

        registration.addRecipeCatalyst(new ItemStack(Blocks.DISPENSER), RecipeTypes.ELITE_SCRAP_BOX);
        registration.addRecipeCatalyst(
                new ItemStack(RecyclingFactoryItems.ELITE_SCRAP_BOX.asItem()),
                RecipeTypes.ELITE_SCRAP_BOX
        );

        registration.addRecipeCatalyst(new ItemStack(Blocks.DISPENSER), RecipeTypes.ULTIMATE_SCRAP_BOX);
        registration.addRecipeCatalyst(
                new ItemStack(RecyclingFactoryItems.ULTIMATE_SCRAP_BOX.asItem()),
                RecipeTypes.ULTIMATE_SCRAP_BOX
        );
    }
}
