package fr.alasdiablo.mods.factory.recycling.config;

import fr.alasdiablo.diolib.api.config.JsonConfigBuilder;
import fr.alasdiablo.mods.factory.recycling.RecyclingFactory;

import java.io.IOException;

public class RecyclingFactoryConfig {
    private static final JsonConfigBuilder CONFIG_BUILDER = new JsonConfigBuilder(RecyclingFactory.MODID);

    public static final ScrapBoxConfig BASIC_SCRAP_BOX = new ScrapBoxConfig("basic_scrap_box");
    public static final ScrapBoxConfig ADVANCED_SCRAP_BOX = new ScrapBoxConfig("advanced_scrap_box");
    public static final ScrapBoxConfig ELITE_SCRAP_BOX = new ScrapBoxConfig("elite_scrap_box");
    public static final ScrapBoxConfig ULTIMATE_SCRAP_BOX = new ScrapBoxConfig("ultimate_scrap_box");

    static {
        try {
            CONFIG_BUILDER.add(BASIC_SCRAP_BOX).add(ADVANCED_SCRAP_BOX).add(ELITE_SCRAP_BOX).add(ULTIMATE_SCRAP_BOX).build();
        } catch (IOException e) {
            RecyclingFactory.LOGGER.error("Unable to load config files");
        }
    }

    public static void init() {}
}
