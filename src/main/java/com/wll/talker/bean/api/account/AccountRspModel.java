package com.wll.talker.bean.api.account;

import com.google.gson.annotations.Expose;
import com.wll.talker.bean.card.UserCard;
import com.wll.talker.bean.db.User;

/**
 * �˻����ַ��ص�Model
 *
 * created by detachment on 2020/5/8
 */
public class AccountRspModel {
    // �û�������Ϣ
    @Expose
    private UserCard user;
    // ��ǰ��¼���˺�
    @Expose
    private String account;
    // ��ǰ��¼�ɹ����ȡ��Token,
    // ����ͨ��Token��ȡ�û���������Ϣ
    @Expose
    private String token;
    // ��ʾ�Ƿ��Ѿ��󶨵����豸PushId
    @Expose
    private boolean isBind;

    public AccountRspModel(User user) {
        // Ĭ���ް�
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBind) {
        this.user = new UserCard(user);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBind = isBind;
    }


    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
