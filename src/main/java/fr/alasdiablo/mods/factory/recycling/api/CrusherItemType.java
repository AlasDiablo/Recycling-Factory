package fr.alasdiablo.mods.factory.recycling.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CrusherItemType {
    private static final AtomicInteger INDEX = new AtomicInteger(0);
    private static final Map<String, Integer> ITEM_TYPE      = new HashMap<>();
    private static final Map<String, Item>    ITEM_TYPE_DROP = new HashMap<>();
    private static final Map<Item, String> ITEM_TO_ITEM_TYPE = new HashMap<>();

    /**
     * Add a new item type
     * @param itemType name of the item type
     * @return False if the item type has been refuse because it already existe or the item type has reach 127 entries.
     */
    public static boolean addItemType(String itemType, Item itemDrop) {
        if (ITEM_TYPE.size() >= 127) {
            return false;
        }
        if (ITEM_TYPE.get(itemType) != null) {
            return false;
        }
        ITEM_TYPE.put(itemType, INDEX.getAndIncrement());
        return true;
    }

    @Contract(pure = true)
    public static @NotNull Set<String> getItemTypes() {
        return ITEM_TYPE.keySet();
    }

    public static @Nullable Integer getItemTypeIndex(String itemType) {
        return ITEM_TYPE.get(itemType);
    }

    public static @Nullable Item getItemTypeDrop(String itemType) {
        return ITEM_TYPE_DROP.get(itemType);
    }

    public static @Nullable String getItemTypeFromItem(Item item) {
        return ITEM_TO_ITEM_TYPE.get(item);
    }

    @Deprecated
    public static void register() {
        ITEM_TYPE.put("wooden", INDEX.getAndIncrement());
        ITEM_TYPE_DROP.put("wooden", Items.STICK);
        ITEM_TO_ITEM_TYPE.put(Items.WOODEN_AXE, "wooden");
        ITEM_TO_ITEM_TYPE.put(Items.WOODEN_HOE, "wooden");
        ITEM_TO_ITEM_TYPE.put(Items.WOODEN_PICKAXE, "wooden");
        ITEM_TO_ITEM_TYPE.put(Items.WOODEN_SHOVEL, "wooden");
        ITEM_TO_ITEM_TYPE.put(Items.WOODEN_SWORD, "wooden");


        ITEM_TYPE.put("iron", INDEX.getAndIncrement());
        ITEM_TYPE_DROP.put("iron", Items.IRON_NUGGET);
        ITEM_TO_ITEM_TYPE.put(Items.IRON_AXE, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_BARS, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_BOOTS, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_CHESTPLATE, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_DOOR, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_HELMET, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_HOE, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_HORSE_ARMOR, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_LEGGINGS, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_PICKAXE, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_SHOVEL, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_SWORD, "iron");
        ITEM_TO_ITEM_TYPE.put(Items.IRON_TRAPDOOR, "iron");

        ITEM_TYPE.put("golden", INDEX.getAndIncrement());
        ITEM_TYPE_DROP.put("golden", Items.GOLD_NUGGET);
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_AXE, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_BOOTS, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_CHESTPLATE, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_HELMET, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_HOE, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_HORSE_ARMOR, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_LEGGINGS, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_PICKAXE, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_SHOVEL, "golden");
        ITEM_TO_ITEM_TYPE.put(Items.GOLDEN_SWORD, "golden");

        ITEM_TYPE.put("diamond", INDEX.getAndIncrement());
        // Add a drop
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_AXE, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_BOOTS, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_CHESTPLATE, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_HELMET, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_HOE, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_HORSE_ARMOR, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_LEGGINGS, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_PICKAXE, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_SHOVEL, "diamond");
        ITEM_TO_ITEM_TYPE.put(Items.DIAMOND_SWORD, "diamond");

        ITEM_TYPE.put("netherite", INDEX.getAndIncrement());
        // Add a drop
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_AXE, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_BOOTS, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_CHESTPLATE, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_HELMET, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_HOE, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_LEGGINGS, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_PICKAXE, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_SHOVEL, "netherite");
        ITEM_TO_ITEM_TYPE.put(Items.NETHERITE_SWORD, "netherite");
    }
}
