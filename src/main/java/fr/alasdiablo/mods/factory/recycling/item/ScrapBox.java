package fr.alasdiablo.mods.factory.recycling.item;

import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxResultTier;
import fr.alasdiablo.mods.factory.recycling.item.behavior.ScrapBoxUseBehavior;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ScrapBox extends Item {
    private final ScrapBoxResultTier tier;

    public ScrapBox(ScrapBoxResultTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ScrapBoxUseBehavior.use(level, player, hand, this.tier);
    }
}
