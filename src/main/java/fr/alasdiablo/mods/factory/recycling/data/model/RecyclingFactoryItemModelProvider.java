package fr.alasdiablo.mods.factory.recycling.data.model;

import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import static fr.alasdiablo.mods.factory.recycling.Registries.*;

public class RecyclingFactoryItemModelProvider extends ItemModelProvider {

    public RecyclingFactoryItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RecyclingFactory.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.item(SCRAP);
        this.item(BASIC_SCRAP_BOX);
        this.item(ADVANCED_SCRAP_BOX);
        this.item(ELITE_SCRAP_BOX);
        this.item(ULTIMATE_SCRAP_BOX);
    }

    private void item(@NotNull String other) {
        withExistingParent(other, new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(RecyclingFactory.MODID, "item/" + other));
    }
}
