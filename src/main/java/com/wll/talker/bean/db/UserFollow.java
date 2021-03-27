package com.wll.talker.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * �û���ϵ��Model��
 * �����û�ֱ�ӽ��к��ѹ�ϵ��ʵ��
 * created by detachment on 2020/5/8
 */
@Entity
@Table(name = "tb_user_follow")
public class UserFollow {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;


    // ����һ�������ˣ����עĳ�ˣ����������
    // ���1 -> ����Թ�ע�ܶ��ˣ����ÿһ�ι�ע����һ����¼
    // ����Դ����ܶ����ע����Ϣ�������Ƕ��1��
    // ����Ķ��һ�ǣ�User ��Ӧ ���UserFollow
    // optional ����ѡ������洢��һ����ע��¼һ��Ҫ��һ��"��"
    @ManyToOne(optional = false)
    // ��������ı��ֶ���ΪoriginId����Ӧ����User.id
    // ����������ݿ��еĴ洢�ֶ�
    @JoinColumn(name = "originId")
    private User origin;
    // ���������ȡ�����ǵ�Model�У�������Ϊnull����������£�����
    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;


    // �����ע��Ŀ�꣬���ע����
    // Ҳ�Ƕ��1������Ա��ܶ��˹�ע��ÿ��һ��ע����һ����¼
    // ���о��� ���UserFollow ��Ӧ һ�� User �����
    @ManyToOne(optional = false)
    // ��������ı��ֶ���ΪtargetId����Ӧ����User.id
    // ����������ݿ��еĴ洢�ֶ�
    @JoinColumn(name = "targetId")
    private User target;
    // ���������ȡ�����ǵ�Model�У�������Ϊnull����������£�����
    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;


    // ������Ҳ���Ƕ�target�ı�ע��, ����Ϊnull
    @Column
    private String alias;


    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
