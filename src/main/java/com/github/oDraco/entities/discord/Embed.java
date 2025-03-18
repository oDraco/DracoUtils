package com.github.oDraco.entities.discord;

import com.github.oDraco.util.MiscUtils;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Embed {

    private String authorName, authorURL, authorIconURL;
    private String bodyTitle, bodyDescription, bodyURL;
    private Integer color;
    private String imageURL, thumbnailURL;
    private String footerText, footerURL;
    private Instant timestamp;

    public JsonObject getJsonObject() {
        JsonObject obj = new JsonObject();
        if(!MiscUtils.allNull(authorName, authorURL, authorIconURL)) {
            JsonObject authorObj = new JsonObject();
            if(authorName != null)
                authorObj.addProperty("name", authorName);
            if(authorURL != null)
                authorObj.addProperty("url", authorURL);
            if(authorIconURL != null)
                authorObj.addProperty("icon_url", authorIconURL);
            obj.add("author", authorObj);
        }
        if(bodyTitle != null)
            obj.addProperty("title", bodyTitle);
        if(bodyDescription != null)
            obj.addProperty("description", bodyDescription);
        if(bodyURL != null)
            obj.addProperty("url", bodyURL);
        if(color != null)
            obj.addProperty("color", color);
        if((!MiscUtils.allNull(footerText, footerURL))) {
            JsonObject footerObj = new JsonObject();
            if(footerText != null)
                footerObj.addProperty("text", footerText);
            if(footerURL != null)
                footerObj.addProperty("icon_url", footerURL);
            obj.add("footer", footerObj);
        }
        if(timestamp != null)
            obj.addProperty("timestamp", timestamp.toString());
        if(imageURL != null) {
            JsonObject imageObj = new JsonObject();
            imageObj.addProperty("url", imageURL);
            obj.add("image", imageObj);
        }
        if(thumbnailURL != null) {
            JsonObject thumbnailObj = new JsonObject();
            thumbnailObj.addProperty("url", thumbnailURL);
            obj.add("thumbnail", thumbnailObj);
        }
        return obj;
    }
}
