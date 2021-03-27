package com.wll.talker.bean.db;

import com.wll.talker.bean.api.message.MessageCreateModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * created by detachment on 2020/5/8
 */
@Entity
@Table(name = "tb_message")
public class Message {
    // ���͸��˵�
    public static final int RECEIVER_TYPE_NONE = 1;
    // ���͸�Ⱥ��
    public static final int RECEIVER_TYPE_GROUP = 2;

    public static final int TYPE_STR = 1; // �ַ�������
    public static final int TYPE_PIC = 2; // ͼƬ����
    public static final int TYPE_FILE = 3; // �ļ�����
    public static final int TYPE_AUDIO = 4; // ��������

    // ����һ������
    @Id
    @PrimaryKeyJoinColumn
    // �������ɴ洢������ΪUUID
    // ���ﲻ�Զ�����UUID��Id�ɴ���д�룬�ɿͻ��˸�������
    // ���⸴�ӵķ������Ϳͻ��˵�ӳ���ϵ
    //@GeneratedValue(generator = "uuid")
    // ��uuid������������Ϊuuid2��uuid2�ǳ����UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // ��������ģ�������Ϊnull
    @Column(updatable = false, nullable = false)
    private String id;

    // ���ݲ�����Ϊ�գ�����Ϊtext
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // ����
    @Column
    private String attach;

    // ��Ϣ����
    @Column(nullable = false)
    private int type;


    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    // ������ ��Ϊ��
    // �����Ϣ��Ӧһ��������
    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    private User sender;
    // ����ֶν���ֻ��Ϊ�˶�Ӧsender�����ݿ��ֶ�senderId
    // �������ֶ��ĸ��»��߲���
    @Column(nullable = false, updatable = false, insertable = false)
    private String senderId;


    // ������ ��Ϊ��
    // �����Ϣ��Ӧһ��������
    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(updatable = false, insertable = false)
    private String receiverId;


    // һ��Ⱥ���Խ��ն����Ϣ
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;
    @Column(updatable = false, insertable = false)
    private String groupId;


    public Message() {

    }

    // ��ͨ���ѵķ��͵Ĺ��캯��
    public Message(User sender, User receiver, MessageCreateModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();

        this.sender = sender;
        this.receiver = receiver;
    }

    // ���͸�Ⱥ�Ĺ��캯��
    public Message(User sender, Group group, MessageCreateModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();

        this.sender = sender;
        this.group = group;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", attach='" + attach + '\'' +
                ", type=" + type +
                ", createAt=" + createdAt +
                ", updateAt=" + updatedAt +
                ", sender=" + sender +
                ", senderId='" + senderId + '\'' +
                ", receiver=" + receiver +
                ", receiverId='" + receiverId + '\'' +
                ", group=" + group +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
