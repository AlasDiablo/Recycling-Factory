package fr.alasdiablo.mods.factory.recycling.compatibility.scrapbox;

import fr.alasdiablo.mods.factory.recycling.api.ChanceDrop;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxBehavior;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ScrapBoxRecipes {
    public static @NotNull List<ChanceDrop> getRecipes(ScrapBoxResultTier tier, @NotNull IIngredientManager ingredientManager) {
        List<ChanceDrop>      scrapBoxDrop   = ScrapBoxBehavior.getDrops(tier);
        Collection<ItemStack> allIngredients = ingredientManager.getAllIngredients(VanillaTypes.ITEM_STACK);
        List<ChanceDrop> output = allIngredients.stream().<ChanceDrop>mapMulti((itemStack, consumer) -> {
                    Item                 item               = itemStack.getItem();
                    Optional<ChanceDrop> chanceDropOptional = scrapBoxDrop.stream().filter(chanceDrop -> chanceDrop.getDrop() == item).findAny();
                    float                chance             = 0f;
                    if (chanceDropOptional.isPresent()) {
                        chance = chanceDropOptional.get().getCalculatedChance();
                    }
                    if (chance > 0f) {
                        consumer.accept(chanceDropOptional.get());
                    }
                })
                .limit(scrapBoxDrop.size())
                .sorted(Comparator.comparingDouble(ChanceDrop::getCalculatedChance)).toList();
        output = new ArrayList<>(output);
        Collections.reverse(output);
        return output;
    }
}
