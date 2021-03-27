package com.wll.talker.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * �����¼��
 * created by detachment on 2020/5/8
 */
@Entity
@Table(name = "tb_apply")
public class Apply {

    public static final int TYPE_ADD_USER = 1; // ��Ӻ���
    public static final int TYPE_ADD_GROUP = 2; // ����Ⱥ

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;


    // �������֣������ǵ�������Ϣ������
    // eg: �������Ϊ���ѣ���
    @Column(nullable = false)
    private String description;


    // ���� ��Ϊ��
    // ���Ը���ͼƬ��ַ����������
    @Column(columnDefinition = "TEXT")
    private String attach;


    // ��ǰ���������
    @Column(nullable = false)
    private int type;


    // Ŀ��Id ������ǿ�������������������ϵ
    // type->TYPE_ADD_USER��User.id
    // type->TYPE_ADD_GROUP��Group.id
    @Column(nullable = false)
    private String targetId;


    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // ����Ϊ����ʱ������ڴ���ʱ���Ѿ�д��
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    // ������ ��Ϊ�� Ϊϵͳ��Ϣ
    // һ���˿����кܶ������
    @ManyToOne
    @JoinColumn(name = "applicantId")
    private User applicant;
    @Column(updatable = false, insertable = false)
    private String applicantId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
}
