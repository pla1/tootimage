package com.tootimage;

import java.util.ArrayList;

public class Toot {
    private int fave_num;
    private int repeat_num;
    private String text;
    private String statusnet_html;
    private ArrayList<Attachment> attachments = new ArrayList<>();
    private User user;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Toot() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFave_num() {
        return fave_num;
    }

    public void setFave_num(int fave_num) {
        this.fave_num = fave_num;
    }

    public int getRepeat_num() {
        return repeat_num;
    }

    public void setRepeat_num(int repeat_num) {
        this.repeat_num = repeat_num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatusnet_html() {
        return statusnet_html;
    }

    public void setStatusnet_html(String statusnet_html) {
        this.statusnet_html = statusnet_html;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

}
