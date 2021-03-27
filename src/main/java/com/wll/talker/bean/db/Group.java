package com.wll.talker.bean.db;

import com.wll.talker.bean.api.group.GroupCreateModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * created by detachment on 2020/5/8
 */
@Entity
@Table(name = "tb_group")
public class Group {

    // ����һ������
    @Id
    @PrimaryKeyJoinColumn
    // �������ɴ洢������ΪUUID���Զ�����UUID
    @GeneratedValue(generator = "uuid")
    // ��uuid������������Ϊuuid2��uuid2�ǳ����UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // ��������ģ�������Ϊnull
    @Column(updatable = false, nullable = false)
    private String id;

    // Ⱥ����
    @Column(nullable = false)
    private String name;

    // Ⱥ����
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String picture;


    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    // Ⱥ�Ĵ�����
    // optional: ��ѡΪfalse��������һ��������
    // fetch: ���ط�ʽFetchType.EAGER�������أ�
    // ��ζ�ż���Ⱥ����Ϣ��ʱ��ͱ������owner����Ϣ
    // cascade����������ΪALL�����еĸ��ģ����£�ɾ���ȣ��������й�ϵ����
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User owner;
    @Column(nullable = false, updatable = false, insertable = false)
    private String ownerId;

    public Group() {

    }

    public Group(User owner, GroupCreateModel model) {
        this.owner = owner;
        this.name = model.getName();
        this.description = model.getDesc();
        this.picture = model.getPicture();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
