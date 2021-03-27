package com.wll.talker.bean.card;

import com.google.gson.annotations.Expose;
import com.wll.talker.bean.db.Group;
import com.wll.talker.bean.db.GroupMember;

import java.time.LocalDateTime;

/**
 * Ⱥ��ϢModel
 *
 * created by detachment on 2020/6/1
 */
public class GroupCard {
    @Expose
    private String id;// Id
    @Expose
    private String name;// ����
    @Expose
    private String desc;// ����
    @Expose
    private String picture;// ȺͼƬ
    @Expose
    private String ownerId;// ������Id
    @Expose
    private int notifyLevel;// ���ڵ�ǰ�û���֪ͨ����
    @Expose
    private LocalDateTime joinAt;// ����ʱ��
    @Expose
    private LocalDateTime modifyAt;// ����޸�ʱ��

    public GroupCard(GroupMember member) {
        final Group group = member.getGroup();
        this.id = group.getId();
        this.name = group.getName();
        this.desc = group.getDescription();
        this.picture = group.getPicture();
        this.ownerId = group.getOwner().getId();
        this.notifyLevel = member.getNotifyLevel();
        this.joinAt = member.getCreateAt();
        this.modifyAt = group.getUpdateAt();
    }

    public GroupCard(Group group, GroupMember member) {
        this.id = group.getId();
        this.name = group.getName();
        this.desc = group.getDescription();
        this.picture = group.getPicture();
        this.ownerId = group.getOwner().getId();
        this.notifyLevel = member != null ? member.getNotifyLevel() : 0;
        this.joinAt = member != null ? member.getCreateAt() : null;
        this.modifyAt = group.getUpdateAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public LocalDateTime getJoinAt() {
        return joinAt;
    }

    public void setJoinAt(LocalDateTime joinAt) {
        this.joinAt = joinAt;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
