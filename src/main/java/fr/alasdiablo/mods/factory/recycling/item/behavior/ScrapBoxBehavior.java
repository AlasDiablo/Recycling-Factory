package fr.alasdiablo.mods.factory.recycling.item.behavior;

import com.mojang.datafixers.util.Pair;
import fr.alasdiablo.mods.factory.recycling.api.ChanceDrop;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ScrapBoxBehavior {
    private static final List<Pair<Pair<ScrapBoxResultType, ScrapBoxResultTier>, ChanceDrop>> RAW_DROPS                   = new ArrayList<>();
    private static final Map<ScrapBoxResultTier, ScrapBoxDropHandler>                         TIER_SCRAP_BOX_DROP_HANDLER = new HashMap<>();

    /**
     * Get a random item from the scrap box loot table and return it
     *
     * @param tier Scrap box tier
     *
     * @return A random item with custom durability if it is a tool or an armor
     */
    public static @NotNull ItemStack getResultItem(ScrapBoxResultTier tier) {
        if (TIER_SCRAP_BOX_DROP_HANDLER.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return TIER_SCRAP_BOX_DROP_HANDLER.get(tier).getResultItem();
    }

    public static @NotNull List<ChanceDrop> getDrops() {
        return RAW_DROPS.stream().map(Pair::getSecond).toList();
    }

    /**
     * Function use by internal components of Recycling Factory, please do not use it!
     * Create four loot table with mathematically spread entry, this is used to get a loot with a percentage of drop.
     */
    @Deprecated
    public static void updateChance() {
        for (ScrapBoxResultTier tier: ScrapBoxResultTier.values()) {
            List<Pair<ScrapBoxResultType, ChanceDrop>> drops = RAW_DROPS
                    .stream()
                    .filter(drop -> drop.getFirst().getSecond() == tier)
                    .map(drop -> Pair.of(
                            drop.getFirst().getFirst(),
                            drop.getSecond()
                    )).toList();
            TIER_SCRAP_BOX_DROP_HANDLER.put(tier, new ScrapBoxDropHandler(drops));
        }
    }

    /**
     * Add a tool item in the scrap box loot table
     *
     * @param tier      Targeted scrap box tier
     * @param item      Item to loot
     * @param rawChance The presence of the item in the loot table
     */
    public static void addToolDrop(ScrapBoxResultTier tier, Item item, float rawChance) {
        addDrop(ScrapBoxResultType.TOOL, tier, item, rawChance);
    }

    /**
     * Add an armor item in the scrap box loot table
     *
     * @param tier      Targeted scrap box tier
     * @param item      Item to loot
     * @param rawChance The presence of the item in the loot table
     */
    public static void addArmorDrop(ScrapBoxResultTier tier, Item item, float rawChance) {
        addDrop(ScrapBoxResultType.TOOL, tier, item, rawChance);
    }

    /**
     * Add a standard item in the scrap box loot table
     *
     * @param tier      Targeted scrap box tier
     * @param item      Item to loot
     * @param rawChance The presence of the item in the loot table
     */
    public static void addItemDrop(ScrapBoxResultTier tier, Item item, float rawChance) {
        addDrop(ScrapBoxResultType.NORMAL, tier, item, rawChance);
    }

    /**
     * Add a new entry in the scrap box loot table
     *
     * @param type      Type of loot, standard item or item with durability like tools
     * @param tier      Targeted scrap box tier
     * @param item      Item to loot
     * @param rawChance The presence of the item in the loot table
     */
    public static void addDrop(ScrapBoxResultType type, ScrapBoxResultTier tier, Item item, float rawChance) {
        RAW_DROPS.add(Pair.of(Pair.of(type, tier), ChanceDrop.of(item, rawChance)));
    }

    /**
     * Function use by internal components of Recycling Factory, please do not use it!
     * Register the initial loot table of each scrap box
     */
    @Deprecated
    public static void registerDrop() {
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_PICKAXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_AXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_HOE, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_SHOVEL, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_SWORD, 0.45f);

        addItemDrop(ScrapBoxResultTier.BASIC, Items.EMERALD, 0.05f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.DIAMOND, 0.05f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.REDSTONE, 0.30f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GLOWSTONE_DUST, 0.30f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.RAW_IRON, 0.80f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.RAW_GOLD, 0.5f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.LAPIS_LAZULI, 0.5f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COAL, 0.80f);

        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_CHICKEN, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_BEEF, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_MUTTON, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_COD, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_PORKCHOP, 1.0f);

        addItemDrop(ScrapBoxResultTier.BASIC, Items.STICK, 5.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.DIRT, 6.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.SAND, 4.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GRASS_BLOCK, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GRAVEL, 3.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COBBLESTONE, 2.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.OAK_PLANKS, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.ACACIA_PLANKS, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.BIRCH_PLANKS, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.JUNGLE_PLANKS, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.SPRUCE_PLANKS, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.DARK_OAK_PLANKS, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.RED_SAND, 2.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.CLAY_BALL, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GRANITE, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.ANDESITE, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.DIORITE, 1.0f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.WHEAT_SEEDS, 2.0f);
    }
}