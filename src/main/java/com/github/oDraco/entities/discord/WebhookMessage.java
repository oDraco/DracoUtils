package com.github.oDraco.entities.discord;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class WebhookMessage {

    @Setter
    private String content = null;
    private final List<Embed> embeds = new ArrayList<>();

    public WebhookMessage() {}

    public WebhookMessage(String content, Collection<? extends Embed> embeds) {
        this.content = content;
        this.embeds.addAll(embeds);
    }

    public void addEmbed(Embed embed) {
        embeds.add(embed);
    }

    public void removeEmbed(Embed embed) {
        embeds.remove(embed);
    }

    public JsonObject getJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty("content", content);
        obj.add("attachments", new JsonArray());
        JsonArray embedsArray = new JsonArray();
        for (Embed embed : embeds) {
            embedsArray.add(embed.getJsonObject());
        }
        obj.add("embeds", embedsArray);
        return obj;
    }
}
