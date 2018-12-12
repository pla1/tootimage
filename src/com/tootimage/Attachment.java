package com.tootimage;

public class Attachment {
    private String description;
    private String id;
    private String mimetype;
    private boolean oembed;
    private String url;
    public Attachment() {

    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public boolean isOembed() {
        return oembed;
    }

    public void setOembed(boolean oembed) {
        this.oembed = oembed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
