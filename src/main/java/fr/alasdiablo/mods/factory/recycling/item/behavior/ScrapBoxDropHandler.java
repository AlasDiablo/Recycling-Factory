package fr.alasdiablo.mods.factory.recycling.item.behavior;

import com.mojang.datafixers.util.Pair;
import fr.alasdiablo.mods.factory.recycling.api.ChanceDrop;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ScrapBoxDropHandler {
    private final NavigableMap<Float, Pair<ScrapBoxResultType, Item>> PROCESS_DROP_TABLE = new TreeMap<>();
    private final Random                                              RANDOM             = new Random();
    private       float                                               totalChance        = 0f;

    ScrapBoxDropHandler(@NotNull List<Pair<ScrapBoxResultType, ChanceDrop>> rawDrops) {
        float totalRawChance = 0f;
        for (Pair<ScrapBoxResultType, ChanceDrop> drop: rawDrops) {
            totalRawChance += drop.getSecond().getRawChance();
        }
        PROCESS_DROP_TABLE.clear();
        totalChance = 0f;
        for (Pair<ScrapBoxResultType, ChanceDrop> drop: rawDrops) {
            drop.getSecond().processChance(totalRawChance);
            totalChance += drop.getSecond().getCalculatedChance();
            PROCESS_DROP_TABLE.put(totalChance, Pair.of(drop.getFirst(), drop.getSecond().getDrop()));
        }
    }

    public @NotNull ItemStack getResultItem() {
        float                                            chance = RANDOM.nextFloat() * totalChance;
        Map.Entry<Float, Pair<ScrapBoxResultType, Item>> entry  = PROCESS_DROP_TABLE.higherEntry(chance);
        if (entry == null) {
            return ItemStack.EMPTY;
        }
        Pair<ScrapBoxResultType, Item> drop = entry.getValue();
        ItemStack                      output;
        switch (drop.getFirst()) {
            case TOOL -> {
                output = new ItemStack(drop.getSecond());
                output.setDamageValue(RANDOM.nextInt(output.getMaxDamage()));
            }
            case NORMAL -> output = new ItemStack(drop.getSecond());
            default -> throw new IllegalStateException("Invalid ScrapResultType for : " + drop.getSecond().toString());
        }
        return output;
    }
}
