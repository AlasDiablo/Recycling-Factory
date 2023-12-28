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

    public static @NotNull List<ChanceDrop> getDrops(ScrapBoxResultTier tier) {
        return RAW_DROPS.stream()
                .filter(drop -> drop.getFirst().getSecond() == tier)
                .map(Pair::getSecond)
                .toList();
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

    private static void registerBasic() {
        // Planks
        addItemDrop(ScrapBoxResultTier.BASIC, Items.OAK_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.ACACIA_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.BIRCH_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.JUNGLE_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.SPRUCE_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.DARK_OAK_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.MANGROVE_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.CHERRY_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.BAMBOO_PLANKS, 1f);

        // Ressources
        addItemDrop(ScrapBoxResultTier.BASIC, Items.STICK, 5f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.FLINT, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.INK_SAC, 0.5f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.IRON_NUGGET, 0.8f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GOLD_NUGGET, 0.5f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.FEATHER, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.STRING, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COPPER_INGOT, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.LEATHER, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.QUARTZ, 0.8f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COAL, 0.8f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.LAPIS_LAZULI, 0.5f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.REDSTONE, 0.3f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GLOWSTONE_DUST, 0.3f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GUNPOWDER, 0.3f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.CLAY_BALL, 1f);

        // Foods
        addItemDrop(ScrapBoxResultTier.BASIC, Items.APPLE, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_CHICKEN, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_BEEF, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_MUTTON, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_COD, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COOKED_PORKCHOP, 1f);

        // Block
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GRASS_BLOCK, 1f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.DIRT, 6f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GRAVEL, 3f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.COBBLESTONE, 2f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.GRANITE, 2f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.DIORITE, 2f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.ANDESITE, 2f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.SAND, 4f);
        addItemDrop(ScrapBoxResultTier.BASIC, Items.RED_SAND, 2f);

        // Tools
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_PICKAXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_AXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_HOE, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_SHOVEL, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.WOODEN_SWORD, 0.45f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.STONE_PICKAXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.STONE_AXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.STONE_HOE, 0.2f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.STONE_SHOVEL, 0.2f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.STONE_SWORD, 0.2f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.GOLDEN_PICKAXE, 0.01f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.GOLDEN_AXE, 0.01f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.GOLDEN_HOE, 0.01f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.GOLDEN_SHOVEL, 0.01f);
        addToolDrop(ScrapBoxResultTier.BASIC, Items.GOLDEN_SWORD, 0.01f);
    }

    private static void registerAdvanced() {
    }

    private static void registerElite() {
    }

    private static void registerUltimate() {
    }

    /**
     * Function use by internal components of Recycling Factory, please do not use it!
     * Register the initial loot table of each scrap box
     */
    @Deprecated
    public static void registerDrop() {
        registerBasic();
        registerAdvanced();
        registerElite();
        registerUltimate();
    }
}