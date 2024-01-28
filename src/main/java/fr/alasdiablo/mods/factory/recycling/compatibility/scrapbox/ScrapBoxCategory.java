package fr.alasdiablo.mods.factory.recycling.compatibility.scrapbox;

import fr.alasdiablo.mods.factory.recycling.api.ChanceDrop;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class ScrapBoxCategory implements IRecipeCategory<ChanceDrop> {
    public static final  int                    width          = 120;
    public static final  int                    height         = 18;
    private static final DecimalFormat          DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final        IDrawable              background;
    private final        IDrawable              slot;
    private final        IDrawable              icon;
    private final        Component              localizedName;
    private final        RecipeType<ChanceDrop> recipeType;

    public ScrapBoxCategory(RecipeType<ChanceDrop> recipeType, ItemLike icon, ScrapBoxResultTier tier, @NotNull IGuiHelper guiHelper) {
        this.recipeType    = recipeType;
        this.background    = guiHelper.createBlankDrawable(width, height);
        this.slot          = guiHelper.getSlotDrawable();
        this.icon          = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(icon));
        this.localizedName = Component.translatable("gui.recycling_factory.category.scrap_box." + tier.name().toLowerCase());
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ChanceDrop recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 1, 1).addItemStacks(recipe.getDrops());
    }

    @Override
    public void draw(@NotNull ChanceDrop recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.slot.draw(guiGraphics);
        float     chance        = recipe.getCalculatedChance();
        String    chancePercent = DECIMAL_FORMAT.format(chance * 100);
        String    text          = I18n.get("gui.recycling_factory.category.scrap_box.chance", chancePercent);
        Minecraft minecraft     = Minecraft.getInstance();
        Font      font          = minecraft.font;
        guiGraphics.drawString(font, text, 24, 5, 0xFF808080, false);
    }

    @Override
    public @NotNull Component getTitle() {
        return this.localizedName;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public @NotNull RecipeType<ChanceDrop> getRecipeType() {
        return this.recipeType;
    }
}
