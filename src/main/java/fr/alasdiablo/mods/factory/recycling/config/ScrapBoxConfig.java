package fr.alasdiablo.mods.factory.recycling.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.alasdiablo.diolib.api.config.JsonConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScrapBoxConfig extends JsonConfig {
    private final String name;
    private final List<AddEntry> addEntries;
    private final List<RemoveEntry> removeEntries;

    public ScrapBoxConfig(String name) {
        this.name = name;
        this.addEntries = new ArrayList<>();
        this.removeEntries = new ArrayList<>();
    }

    @Override
    protected void read(@NotNull JsonObject jsonObject) {
        JsonArray add = jsonObject.getAsJsonArray("add");
        add.forEach(jsonElement -> {
            JsonObject entry = jsonElement.getAsJsonObject();
            this.addEntries.add(new AddEntry(
                    entry.get("type").getAsString(),
                    entry.get("id").getAsString(),
                    entry.get("chance").getAsFloat()
            ));
        });

        JsonArray remove = jsonObject.getAsJsonArray("remove");
        remove.forEach(jsonElement -> {
            JsonObject entry = jsonElement.getAsJsonObject();
            this.removeEntries.add(new RemoveEntry(
                    entry.get("type").getAsString(),
                    entry.get("id").getAsString()
            ));
        });
    }

    @Override
    protected JsonObject write() {
        final JsonObject json = new JsonObject();
        json.addProperty("__comment", "Config documentation available at https://github.com/AlasDiablo/Recycling-Factory/wiki/Scrap-Box#scrap-box-config");
        json.add("add", new JsonArray());
        json.add("remove", new JsonArray());
        return json;
    }

    public List<AddEntry> getAddEntries() {
        return addEntries;
    }

    public List<RemoveEntry> getRemoveEntries() {
        return removeEntries;
    }

    @Override
    protected String getName() {
        return this.name;
    }

    public record AddEntry(String type, String id, float chance) {}
    public record RemoveEntry(String type, String id) {}
}
