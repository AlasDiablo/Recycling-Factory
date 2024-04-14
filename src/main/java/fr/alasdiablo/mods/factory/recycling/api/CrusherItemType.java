package fr.alasdiablo.mods.factory.recycling.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CrusherItemType {
    public record ItemType(Integer itemTypeId, Item drop) {}
    public record ItemMap(String itemType, Integer maxAmount) {}

    private static final AtomicInteger INDEX = new AtomicInteger(0);
    private static final Map<String, ItemType> ITEM_TYPE = new HashMap<>();
    private static final Map<Item, ItemMap> ITEM_MAP     = new HashMap<>();

    /**
     * Add a new item type
     * @param itemType name of the item type
     */
    public static void addItemType(String itemType, Item itemDrop) throws IndexOutOfBoundsException, IllegalStateException {
        if (ITEM_TYPE.size() >= 127) {
            throw new IndexOutOfBoundsException("The ItemType map is full!");
        }
        if (ITEM_TYPE.get(itemType) != null) {
            throw new IllegalStateException("The ItemType already exists!");
        }
        ITEM_TYPE.put(itemType, new ItemType(INDEX.getAndIncrement(), itemDrop));
    }

    public static void addCrushableItem(String itemType, Item crushableItem, int maxAmount) {
        if (ITEM_TYPE.get(itemType) == null) {
            throw new IllegalStateException("The ItemType does not exist!");
        }
        if (ITEM_MAP.get(crushableItem) != null) {
            throw new IllegalStateException("The Crushable Item already exists!");
        }
        ITEM_MAP.put(crushableItem, new ItemMap(itemType, maxAmount));
    }

    @Contract(pure = true)
    public static @NotNull Set<String> getItemTypes() {
        return ITEM_TYPE.keySet();
    }

    public static @Nullable Integer getItemTypeId(Item crushableItem) {
        ItemMap entry = ITEM_MAP.get(crushableItem);
        if (entry == null) {
            return null;
        }

        ItemType itemType = ITEM_TYPE.get(entry.itemType);
        if (itemType == null) {
            return null;
        }

        return itemType.itemTypeId;
    }

    @Deprecated
    public static void register() {
        addItemType("wooden", Items.STICK);
        addCrushableItem("wooden", Items.WOODEN_AXE, 12);
        addCrushableItem("wooden", Items.WOODEN_HOE, 8);
        addCrushableItem("wooden", Items.WOODEN_PICKAXE, 12);
        addCrushableItem("wooden", Items.WOODEN_SHOVEL, 4);
        addCrushableItem("wooden", Items.WOODEN_SWORD, 8);

        ITEM_TYPE.put("iron", new ItemType(INDEX.getAndIncrement(), Items.IRON_NUGGET));
        addCrushableItem("iron", Items.IRON_AXE, 27);
        addCrushableItem("iron", Items.IRON_BARS, 3);
        addCrushableItem("iron", Items.IRON_BOOTS, 36);
        addCrushableItem("iron", Items.IRON_CHESTPLATE, 72);
        addCrushableItem("iron", Items.IRON_DOOR, 18);
        addCrushableItem("iron", Items.IRON_HELMET, 54);
        addCrushableItem("iron", Items.IRON_HOE, 18);
        addCrushableItem("iron", Items.IRON_HORSE_ARMOR, 54);
        addCrushableItem("iron", Items.IRON_LEGGINGS, 63);
        addCrushableItem("iron", Items.IRON_PICKAXE, 27);
        addCrushableItem("iron", Items.IRON_SHOVEL, 9);
        addCrushableItem("iron", Items.IRON_SWORD, 18);
        addCrushableItem("iron", Items.IRON_TRAPDOOR, 36);

        ITEM_TYPE.put("golden", new ItemType(INDEX.getAndIncrement(), Items.GOLD_NUGGET));
        addCrushableItem("golden", Items.GOLDEN_AXE, 27);
        addCrushableItem("golden", Items.GOLDEN_BOOTS, 36);
        addCrushableItem("golden", Items.GOLDEN_CHESTPLATE, 72);
        addCrushableItem("golden", Items.GOLDEN_HELMET, 54);
        addCrushableItem("golden", Items.GOLDEN_HOE, 18);
        addCrushableItem("golden", Items.GOLDEN_HORSE_ARMOR, 54);
        addCrushableItem("golden", Items.GOLDEN_LEGGINGS, 63);
        addCrushableItem("golden", Items.GOLDEN_PICKAXE, 27);
        addCrushableItem("golden", Items.GOLDEN_SHOVEL, 9);
        addCrushableItem("golden", Items.GOLDEN_SWORD, 18);

        // TODO Add a drop
        ITEM_TYPE.put("diamond", new ItemType(INDEX.getAndIncrement(), Items.STONE));
        addCrushableItem("diamond", Items.DIAMOND_AXE, 27);
        addCrushableItem("diamond", Items.DIAMOND_BOOTS, 36);
        addCrushableItem("diamond", Items.DIAMOND_CHESTPLATE, 72);
        addCrushableItem("diamond", Items.DIAMOND_HELMET, 54);
        addCrushableItem("diamond", Items.DIAMOND_HOE, 18);
        addCrushableItem("diamond", Items.DIAMOND_HORSE_ARMOR, 54);
        addCrushableItem("diamond", Items.DIAMOND_LEGGINGS, 63);
        addCrushableItem("diamond", Items.DIAMOND_PICKAXE, 27);
        addCrushableItem("diamond", Items.DIAMOND_SHOVEL, 9);
        addCrushableItem("diamond", Items.DIAMOND_SWORD, 18);

        ITEM_TYPE.put("netherite", new ItemType(INDEX.getAndIncrement(), Items.NETHERITE_SCRAP));
        addCrushableItem("netherite", Items.NETHERITE_AXE, 4);
        addCrushableItem("netherite", Items.NETHERITE_BOOTS, 4);
        addCrushableItem("netherite", Items.NETHERITE_CHESTPLATE, 4);
        addCrushableItem("netherite", Items.NETHERITE_HELMET, 4);
        addCrushableItem("netherite", Items.NETHERITE_HOE, 4);
        addCrushableItem("netherite", Items.NETHERITE_LEGGINGS, 4);
        addCrushableItem("netherite", Items.NETHERITE_PICKAXE, 4);
        addCrushableItem("netherite", Items.NETHERITE_SHOVEL, 4);
        addCrushableItem("netherite", Items.NETHERITE_SWORD, 4);
    }
}
