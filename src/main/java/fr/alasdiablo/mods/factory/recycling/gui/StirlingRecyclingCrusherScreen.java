package fr.alasdiablo.mods.factory.recycling.gui;

import fr.alasdiablo.mods.factory.recycling.inventory.crusher.StirlingRecyclingCrusherMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class StirlingRecyclingCrusherScreen extends AbstractContainerScreen<StirlingRecyclingCrusherMenu> {
    private final ResourceLocation texture;
    private final ResourceLocation litProgressSprite;
    private final ResourceLocation burnProgressSprite;

    public StirlingRecyclingCrusherScreen(
            StirlingRecyclingCrusherMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);
        this.texture            = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
        this.litProgressSprite  = new ResourceLocation("minecraft", "container/furnace/lit_progress");
        this.burnProgressSprite = new ResourceLocation("minecraft", "container/furnace/burn_progress");
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(this.texture, x, y, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isLit()) {
            int litSpriteSize   = 14;
            int litSpriteOffset = Mth.ceil(this.menu.getLitProgress() * 13.0f) + 1;
            guiGraphics.blitSprite(
                    this.litProgressSprite,
                    litSpriteSize,
                    litSpriteSize,
                    0,
                    litSpriteSize - litSpriteOffset,
                    x + 56,
                    y + 36 + litSpriteSize - litSpriteOffset,
                    litSpriteSize,
                    litSpriteOffset
            );
        }

        int burnSpriteSize   = 24;
        int burnSpriteOffset = Mth.ceil(this.menu.getBurnProgress() * 24.0F);
        guiGraphics.blitSprite(
                this.burnProgressSprite,
                burnSpriteSize,
                16,
                0,
                0,
                x + 79,
                y + 34,
                burnSpriteOffset,
                16
        );
    }
}
