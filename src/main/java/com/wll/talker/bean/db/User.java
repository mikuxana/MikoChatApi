package com.wll.talker.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * �û���Model����Ӧ���ݿ�
 * created by detachment on 2020/5/8
 */
@Entity
@Table(name = "tb_user")
public class User implements Principal {

    // ����һ������
    @Id
    @PrimaryKeyJoinColumn
    // �������ɴ洢������ΪUUID
    @GeneratedValue(generator = "uuid")
    // ��uuid������������Ϊuuid2��uuid2�ǳ����UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // ��������ģ�������Ϊnull
    @Column(updatable = false, nullable = false)
    private String id;

    // �û�������Ψһ
    @Column(nullable = false, length = 128, unique = true)
    private String name;

    // �绰����Ψһ
    @Column(nullable = false, length = 62, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    // ͷ������Ϊnull
    @Column
    private String portrait;

    @Column
    private String description;

    // �Ա��г�ʼֵ�����в�Ϊ��
    @Column(nullable = false)
    private int sex = 0;

    // token ������ȡ�û���Ϣ������token����Ψһ
    @Column(unique = true)
    private String token;

    // �������͵��豸ID
    @Column
    private String pushId;

    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    // ���һ���յ���Ϣ��ʱ��
    @Column
    private LocalDateTime lastReceivedAt = LocalDateTime.now();


    // �ҹ�ע���˵��б���
    // ��Ӧ�����ݿ���ֶ�ΪTB_USER_FOLLOW.originId
    @JoinColumn(name = "originId")
    // ����Ϊ�����أ�Ĭ�ϼ���User��Ϣ��ʱ�򣬲�����ѯ�������
    @LazyCollection(LazyCollectionOption.EXTRA)
    // 1�Զ࣬һ���û������кܶ��ע�ˣ�ÿһ�ι�ע����һ����¼
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> following = new HashSet<>();


    // ��ע�ҵ��˵��б�
    // ��Ӧ�����ݿ���ֶ�ΪTB_USER_FOLLOW.targetId
    @JoinColumn(name = "targetId")
    // ����Ϊ�����أ�Ĭ�ϼ���User��Ϣ��ʱ�򣬲�����ѯ�������
    @LazyCollection(LazyCollectionOption.EXTRA)
    // 1�Զ࣬һ���û����Ա��ܶ��˹�ע��ÿһ�ι�ע����һ����¼
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> followers = new HashSet<>();

    // �����д�����Ⱥ
    // ��Ӧ���ֶ�Ϊ��Group.ownerId
    @JoinColumn(name = "ownerId")
    // �����ؼ��Ϸ�ʽΪ�����ܵĲ����ؾ�������ݣ�
    // ������groups.size()������ѯ�����������ؾ����Group��Ϣ
    // ֻ�е��������ϵ�ʱ��ż��ؾ��������
    @LazyCollection(LazyCollectionOption.EXTRA)
    // FetchType.LAZY�������أ������û���Ϣʱ�������������
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Group> groups = new HashSet<>();


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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
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

    public LocalDateTime getLastReceivedAt() {
        return lastReceivedAt;
    }

    public void setLastReceivedAt(LocalDateTime lastReceivedAt) {
        this.lastReceivedAt = lastReceivedAt;
    }

    public Set<UserFollow> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UserFollow> following) {
        this.following = following;
    }

    public Set<UserFollow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<UserFollow> followers) {
        this.followers = followers;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}
