package fr.alasdiablo.mods.factory.recycling.item.behavior;

import com.mojang.datafixers.util.Pair;
import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import fr.alasdiablo.mods.factory.recycling.api.ChanceDrop;
import fr.alasdiablo.mods.factory.recycling.config.RecyclingFactoryConfig;
import fr.alasdiablo.mods.factory.recycling.config.ScrapBoxConfig;
import fr.alasdiablo.mods.factory.recycling.init.RecyclingFactoryItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
     * Create four loot tables with mathematically spread entry, this is used to get a loot with a percentage of drop.
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

//        printMarkdown(ScrapBoxResultTier.BASIC);
//
//        System.out.println();
//        System.out.println();
//        printMarkdown(ScrapBoxResultTier.ADVANCED);
//
//        System.out.println();
//        System.out.println();
//        printMarkdown(ScrapBoxResultTier.ELITE);
//
//        System.out.println();
//        System.out.println();
//        printMarkdown(ScrapBoxResultTier.ULTIMATE);
    }

    private static void printMarkdown(ScrapBoxResultTier tier) {
        DecimalFormat          df          = new DecimalFormat("0.000");
        AtomicReference<Float> rawTotal    = new AtomicReference<>(0f);
        AtomicReference<Float> chanceTotal = new AtomicReference<>(0f);
        System.out.println("| Item | Raw Chance | Chance |");
        System.out.println("|-|-|-|");
        getDrops(tier).forEach(chanceDrop -> {
            System.out.print("|");
            System.out.print(chanceDrop.getDrop().toString());
            System.out.print("|");
            System.out.print(chanceDrop.getRawChance());
            rawTotal.updateAndGet(v -> v + chanceDrop.getRawChance());
            System.out.print("|");
            System.out.print(df.format(chanceDrop.getCalculatedChance() * 100)); System.out.print("%");
            chanceTotal.updateAndGet(v -> v + chanceDrop.getCalculatedChance() * 100);
            System.out.println("|");
        });
        System.out.println("| Total | " + rawTotal + "|" + chanceTotal + "|");
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
        registerBasic();
        registerAdvanced();
        registerElite();
        registerUltimate();

        registerCustom(RecyclingFactoryConfig.BASIC_SCRAP_BOX, ScrapBoxResultTier.BASIC);
        registerCustom(RecyclingFactoryConfig.ADVANCED_SCRAP_BOX, ScrapBoxResultTier.ADVANCED);
        registerCustom(RecyclingFactoryConfig.ELITE_SCRAP_BOX, ScrapBoxResultTier.ELITE);
        registerCustom(RecyclingFactoryConfig.ULTIMATE_SCRAP_BOX, ScrapBoxResultTier.ULTIMATE);
    }

    private static void registerCustom(@NotNull ScrapBoxConfig config, ScrapBoxResultTier tier) {
        Registry<Item> itemRegistry = RecyclingFactoryItems.getRegistry();

        config.getRemoveEntries().forEach(removeEntry -> {
            Item item = itemRegistry.get(new ResourceLocation(removeEntry.id()));
            ScrapBoxResultType type = removeEntry.type().equals("tool") ? ScrapBoxResultType.TOOL : ScrapBoxResultType.NORMAL;

            RAW_DROPS.stream().filter(pairChanceDropPair -> {
                ScrapBoxResultType dropType = pairChanceDropPair.getFirst().getFirst();
                Item dropItem = pairChanceDropPair.getSecond().getDrop();
                return dropType == type && dropItem == item;
            }).findAny().ifPresent(RAW_DROPS::remove);
        });

        config.getAddEntries().forEach(addEntry -> {
            Item item = itemRegistry.get(new ResourceLocation(addEntry.id()));
            ScrapBoxResultType type = addEntry.type().equals("tool") ? ScrapBoxResultType.TOOL : ScrapBoxResultType.NORMAL;
            float chance = addEntry.chance();
            addDrop(type, tier, item, chance);
        });
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
        // Planks
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.OAK_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.ACACIA_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.BIRCH_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.JUNGLE_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.SPRUCE_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.DARK_OAK_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.MANGROVE_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.CHERRY_PLANKS, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.BAMBOO_PLANKS, 1f);

        // Ressources
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.STICK, 5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.FLINT, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.INK_SAC, 0.5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.IRON_NUGGET, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.GOLD_NUGGET, 0.7f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.FEATHER, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.STRING, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COPPER_INGOT, 1.5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.LEATHER, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.SLIME_BALL, 0.8f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.QUARTZ, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COAL, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.AMETHYST_SHARD, 0.3f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.LAPIS_LAZULI, 0.5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.REDSTONE, 0.5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.GLOWSTONE_DUST, 0.5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.GUNPOWDER, 0.5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.CLAY_BALL, 1f);

        // Foods
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.APPLE, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COOKED_CHICKEN, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COOKED_BEEF, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COOKED_MUTTON, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COOKED_COD, 1f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COOKED_PORKCHOP, 1f);

        // Block
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.GRASS_BLOCK, 1.5f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.DIRT, 6f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.GRAVEL, 3f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.COBBLESTONE, 2f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.GRANITE, 2f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.DIORITE, 2f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.ANDESITE, 2f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.SAND, 4f);
        addItemDrop(ScrapBoxResultTier.ADVANCED, Items.RED_SAND, 2.5f);

        // Tools
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.STONE_PICKAXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.STONE_AXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.STONE_HOE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.STONE_SHOVEL, 0.45f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.STONE_SWORD, 0.45f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.IRON_PICKAXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.IRON_AXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.IRON_HOE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.IRON_SHOVEL, 0.2f);
        addToolDrop(ScrapBoxResultTier.ADVANCED, Items.IRON_SWORD, 0.2f);
    }

    private static void registerElite() {
        // Ressources
        addItemDrop(ScrapBoxResultTier.ELITE, Items.STICK, 4f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.FLINT, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.INK_SAC, 0.5f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.GLOW_INK_SAC, 0.5f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.HONEYCOMB, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.GOLD_NUGGET, 0.9f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.IRON_NUGGET, 1.2f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.GOLD_INGOT, 0.7f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.IRON_INGOT, 0.9f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.LEATHER, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.SLIME_BALL, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.NETHERITE_SCRAP, 0.1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.QUARTZ, 1.2f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.COAL, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.DIAMOND, 0.3f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.EMERALD, 0.3f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.AMETHYST_SHARD, 0.5f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.LAPIS_LAZULI, 0.7f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.REDSTONE, 0.7f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.GLOWSTONE_DUST, 0.7f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.GUNPOWDER, 0.7f);

        // Foods
        addItemDrop(ScrapBoxResultTier.ELITE, Items.GOLDEN_APPLE, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.COOKED_CHICKEN, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.COOKED_BEEF, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.COOKED_MUTTON, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.COOKED_COD, 1f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.COOKED_PORKCHOP, 1f);

        // Block
        addItemDrop(ScrapBoxResultTier.ELITE, Items.GRASS_BLOCK, 3f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.CLAY, 3f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.SAND, 3f);
        addItemDrop(ScrapBoxResultTier.ELITE, Items.RED_SAND, 2.5f);

        // Tools
        addToolDrop(ScrapBoxResultTier.ELITE, Items.IRON_PICKAXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.IRON_AXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.IRON_HOE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.IRON_SHOVEL, 0.45f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.IRON_SWORD, 0.45f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.DIAMOND_PICKAXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.DIAMOND_AXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.DIAMOND_HOE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.DIAMOND_SHOVEL, 0.2f);
        addToolDrop(ScrapBoxResultTier.ELITE, Items.DIAMOND_SWORD, 0.2f);
    }

    private static void registerUltimate() {
        // Ressources
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.FLINT, 2f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.GLOW_INK_SAC, 0.7f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHER_STAR, 0.025f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.BLAZE_ROD, 0.3f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.HONEYCOMB, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.GOLD_INGOT, 0.9f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.IRON_INGOT, 1.2f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.LEATHER, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.SLIME_BALL, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHERITE_SCRAP, 0.3f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHERITE_INGOT, 0.1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.QUARTZ, 1.2f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.COAL, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.DIAMOND, 0.7f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.EMERALD, 0.7f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.AMETHYST_SHARD, 0.7f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.LAPIS_LAZULI, 0.9f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.REDSTONE, 0.9f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.GLOWSTONE_DUST, 0.9f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.GUNPOWDER, 0.9f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.DRAGON_EGG, 0.0025f);

        // Foods
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.ENCHANTED_GOLDEN_APPLE, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.COOKED_CHICKEN, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.COOKED_BEEF, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.COOKED_MUTTON, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.COOKED_COD, 1f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.COOKED_PORKCHOP, 1f);

        // Block
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.GRASS_BLOCK, 2.5f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.CLAY, 2.5f);
        addItemDrop(ScrapBoxResultTier.ULTIMATE, Items.RED_SAND, 2.5f);

        // Tools
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.DIAMOND_PICKAXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.DIAMOND_AXE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.DIAMOND_HOE, 0.45f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.DIAMOND_SHOVEL, 0.45f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.DIAMOND_SWORD, 0.45f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHERITE_PICKAXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHERITE_AXE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHERITE_HOE, 0.2f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHERITE_SHOVEL, 0.2f);
        addToolDrop(ScrapBoxResultTier.ULTIMATE, Items.NETHERITE_SWORD, 0.2f);
    }
}