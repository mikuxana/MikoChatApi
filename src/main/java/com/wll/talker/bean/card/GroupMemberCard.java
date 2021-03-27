package com.wll.talker.bean.card;

import com.google.gson.annotations.Expose;
import com.wll.talker.bean.db.GroupMember;

import java.time.LocalDateTime;

/**
 * Ⱥ��ԱModel
 *
 * created by detachment on 2020/6/1
 */
public class GroupMemberCard {
    @Expose
    private String id;// Id
    @Expose
    private String alias;// ��������ע
    @Expose
    private boolean isAdmin;// �Ƿ��ǹ���Ա
    @Expose
    private boolean isOwner;// �Ƿ��Ǵ�����
    @Expose
    private String userId;// ���ڵ��û�Id
    @Expose
    private String groupId;// ���ڵ�ȺId
    @Expose
    private LocalDateTime modifyAt;// ����޸�ʱ��

    public GroupMemberCard(GroupMember member) {
        this.id = member.getId();
        this.alias = member.getAlias();
        this.isAdmin = member.getPermissionType() == GroupMember.PERMISSION_TYPE_ADMIN;
        this.isOwner = member.getPermissionType() == GroupMember.PERMISSION_TYPE_ADMIN_SU;
        this.userId = member.getUser().getId();
        this.groupId = member.getGroup().getId();
        this.modifyAt = member.getUpdateAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
