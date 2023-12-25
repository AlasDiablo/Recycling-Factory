package fr.alasdiablo.mods.factory.recycling.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChanceDrop {
    private final float rawChance;
    private final Item  drop;
    private       float calculatedChance;

    public ChanceDrop(Item drop, float rawChance) {
        this.drop      = drop;
        this.rawChance = rawChance;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull ChanceDrop of(Item drop, float rawChance) {
        return new ChanceDrop(drop, rawChance);
    }

    public void processChance(float totalRawChance) {
        this.calculatedChance = this.rawChance / totalRawChance;
    }

    public float getRawChance() {
        return this.rawChance;
    }

    public Item getDrop() {
        return this.drop;
    }

    public List<ItemStack> getDrops() {
        return List.of(new ItemStack(this.drop));
    }

    public float getCalculatedChance() {
        return this.calculatedChance;
    }
}
