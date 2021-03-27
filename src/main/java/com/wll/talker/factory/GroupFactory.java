package com.wll.talker.factory;

import com.google.common.base.Strings;
import com.wll.talker.bean.api.group.GroupCreateModel;
import com.wll.talker.bean.db.Group;
import com.wll.talker.bean.db.GroupMember;
import com.wll.talker.bean.db.User;
import com.wll.talker.utils.Hib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Ⱥ���ݴ�����
 *
 * created by detachment on 2020/6/1
 */
public class GroupFactory {
    // ͨ��Id��ȺModel
    public static Group findById(String groupId) {
        return Hib.query(session -> session.get(Group.class, groupId));
    }

    // ��ѯһ��Ⱥ��ͬʱ����˱�����Ⱥ�ĳ�Ա
    public static Group findById(User user, String groupId) {
        GroupMember member = getMember(user.getId(), groupId);
        if (member != null) {
            return member.getGroup();
        }
        return null;
    }

    // ͨ�����ֲ���Ⱥ
    public static Group findByName(String name) {
        return Hib.query(session -> (Group) session
                .createQuery("from Group where lower(name)=:name ")
                .setParameter("name", name.toLowerCase())
                .uniqueResult());
    }

    // ��ȡһ��Ⱥ�����г�Ա
    public static Set<GroupMember> getMembers(Group group) {
        return Hib.query(session -> {
            @SuppressWarnings("unchecked")
            List<GroupMember> members = session.createQuery("from GroupMember where group=:group")
                    .setParameter("group", group)
                    .list();

            return new HashSet<>(members);
        });
    }

    // ��ȡһ���˼��������Ⱥ
    public static Set<GroupMember> getMembers(User user) {
        return Hib.query(session -> {
            @SuppressWarnings("unchecked")
            List<GroupMember> members = session.createQuery("from GroupMember where userId=:userId")
                    .setParameter("userId", user.getId())
                    .list();

            return new HashSet<>(members);
        });
    }

    // ����Ⱥ
    public static Group create(User creator, GroupCreateModel model, List<User> users) {
        return Hib.query(session -> {
            Group group = new Group(creator, model);
            session.save(group);

            GroupMember ownerMember = new GroupMember(creator, group);
            // ���ó���Ȩ�ޣ�������
            ownerMember.setPermissionType(GroupMember.PERMISSION_TYPE_ADMIN_SU);
            // ���棬��û���ύ�����ݿ�
            session.save(ownerMember);

            for (User user : users) {
                GroupMember member = new GroupMember(user, group);
                // ���棬��û���ύ�����ݿ�
                session.save(member);
            }

            //session.flush();
            //session.load(group, group.getId());

            return group;
        });
    }

    // ��ȡһ��Ⱥ�ĳ�Ա
    public static GroupMember getMember(String userId, String groupId) {
        return Hib.query(session -> (GroupMember) session
                .createQuery("from GroupMember where userId=:userId and groupId=:groupId")
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .setMaxResults(1)
                .uniqueResult()
        );
    }

    // ��ѯ
    @SuppressWarnings("unchecked")
    public static List<Group> search(String name) {
        if (Strings.isNullOrEmpty(name))
            name = ""; // ��֤����Ϊnull����������ٺ����һ���жϺͶ���Ĵ���
        final String searchName = "%" + name + "%"; // ģ��ƥ��

        return Hib.query(session -> {
            // ��ѯ��������name���Դ�Сд������ʹ��like��ģ������ѯ��
            // ͷ��������������Ʋ��ܲ�ѯ��
            return (List<Group>) session.createQuery("from Group where lower(name) like :name")
                    .setParameter("name", searchName)
                    .setMaxResults(20) // ����20��
                    .list();
        });
    }

    // ��Ⱥ��ӳ�Ա
    public static Set<GroupMember> addMembers(Group group, List<User> insertUsers) {
        return Hib.query(session -> {

            Set<GroupMember> members = new HashSet<>();

            for (User user : insertUsers) {
                GroupMember member = new GroupMember(user, group);
                // ���棬��û���ύ�����ݿ�
                session.save(member);
                members.add(member);
            }

            // ��������ˢ��
            /*
            for (GroupMember member : members) {
                // ����ˢ�£�����й�����ѯ����ѭ�������Ľϸ�
                session.refresh(member);
            }
            */

            return members;
        });
    }
}
