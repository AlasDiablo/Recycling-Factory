package fr.alasdiablo.mods.factory.recycling.item.behavior;

import com.mojang.datafixers.util.Pair;
import fr.alasdiablo.mods.factory.recycling.api.ChanceDrop;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unused")
public class ScrapBoxBehavior {
    private static final List<Pair<Pair<ScrapBoxResultType, ScrapBoxResultTier>, ChanceDrop>> RAW_DROPS                   = new ArrayList<>();
    private static final Map<ScrapBoxResultTier, ScrapBoxDropHandler>                         TIER_SCRAP_BOX_DROP_HANDLER = new HashMap<>();

    public static @NotNull ItemStack getResultItem(ScrapBoxResultTier tier) {
        if (TIER_SCRAP_BOX_DROP_HANDLER.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return TIER_SCRAP_BOX_DROP_HANDLER.get(tier).getResultItem();
    }

    public static @NotNull List<ChanceDrop> getDrops() {
        return RAW_DROPS.stream().map(Pair::getSecond).toList();
    }

    @Deprecated
    public static void updateChance() {
        for (ScrapBoxResultTier tier: ScrapBoxResultTier.values()) {
            List<Pair<ScrapBoxResultType, ChanceDrop>> drops = RAW_DROPS.stream().map(drop -> Pair.of(
                    drop.getFirst().getFirst(),
                    drop.getSecond()
            )).toList();
            TIER_SCRAP_BOX_DROP_HANDLER.put(tier, new ScrapBoxDropHandler(drops));
        }
    }

    public static void addToolDrop(ScrapBoxResultTier tier, Item item, float rawChance) {
        addDrop(ScrapBoxResultType.TOOL, tier, item, rawChance);
    }

    public static void addArmorDrop(ScrapBoxResultTier tier, Item item, float rawChance) {
        addDrop(ScrapBoxResultType.TOOL, tier, item, rawChance);
    }

    public static void addItemDrop(ScrapBoxResultTier tier, Item item, float rawChance) {
        addDrop(ScrapBoxResultType.NORMAL, tier, item, rawChance);
    }

    public static void addDrop(ScrapBoxResultType type, ScrapBoxResultTier tier, Item item, float rawChance) {
        RAW_DROPS.add(Pair.of(Pair.of(type, tier), ChanceDrop.of(item, rawChance)));
    }

    private static class ScrapBoxDropHandler {
        private final NavigableMap<Float, Pair<ScrapBoxResultType, Item>> PROCESS_DROP_TABLE = new TreeMap<>();
        private final Random                                              RANDOM             = new Random();
        private       float                                               totalChance        = 0f;

        private ScrapBoxDropHandler(@NotNull List<Pair<ScrapBoxResultType, ChanceDrop>> rawDrops) {
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
            float                          chance = RANDOM.nextFloat() * totalChance;
            Pair<ScrapBoxResultType, Item> drop   = PROCESS_DROP_TABLE.higherEntry(chance).getValue();
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

    public enum ScrapBoxResultType {
        TOOL,
        NORMAL
    }

    public enum ScrapBoxResultTier {
        BASIC,
        ADVANCED,
        ELITE,
        ULTIMATE
    }
}