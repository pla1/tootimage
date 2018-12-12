package com.tootimage;

public class Account {
    private String avatar;
    private String avatar_static;
    private String acct;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_static() {
        return avatar_static;
    }

    public void setAvatar_static(String avatar_static) {
        this.avatar_static = avatar_static;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    private String display_name;

}
