package com.wll.talker.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * created by detachment on 2020/5/8
 */
public class LoginModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String pushId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    // ะฃั้
    public static boolean check(LoginModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.account)
                && !Strings.isNullOrEmpty(model.password);

    }
}
