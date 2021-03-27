package com.wll.talker.bean.card;

import com.google.gson.annotations.Expose;
import com.wll.talker.bean.db.Message;

import java.time.LocalDateTime;

/**
 * ��Ϣ�Ŀ�ƬModel
 *
 * created by detachment on 2020/6/1
 */
public class MessageCard {
    @Expose
    private String id; // Id
    @Expose
    private String content;// ����
    @Expose
    private String attach;// ������������Ϣ
    @Expose
    private int type;// ��Ϣ����
    @Expose
    private LocalDateTime createAt;// ����ʱ��
    @Expose
    private String groupId;// �����Ⱥ��Ϣ����ӦȺId
    @Expose
    private String senderId;// ������Id����Ϊ��
    @Expose
    private String receiverId;// ������Id

    public MessageCard(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.type = message.getType();
        this.attach = message.getAttach();
        this.createAt = message.getCreatedAt();
        this.groupId = message.getGroupId();
        this.senderId = message.getSenderId();
        this.receiverId = message.getReceiverId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
