package com.tootimage;

import java.util.ArrayList;

public class Toot {
    private Account account;
    private ArrayList<Media_attachment> media_attachments = new ArrayList<>();
    private int fave_num;
    private int repeat_num;
    private int replies_count;
    private int reblogs_count;
    private int favourites_count;
    private String content;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Toot() {
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ArrayList<Media_attachment> getMedia_attachments() {
        return media_attachments;
    }

    public void setMedia_attachments(ArrayList<Media_attachment> media_attachments) {
        this.media_attachments = media_attachments;
    }

    public int getReplies_count() {
        return replies_count;
    }

    public void setReplies_count(int replies_count) {
        this.replies_count = replies_count;
    }

    public int getReblogs_count() {
        return reblogs_count;
    }

    public void setReblogs_count(int reblogs_count) {
        this.reblogs_count = reblogs_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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


}
