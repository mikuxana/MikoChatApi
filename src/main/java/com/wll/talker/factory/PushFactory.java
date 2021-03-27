package com.wll.talker.factory;

import com.google.common.base.Strings;
import com.wll.talker.bean.api.base.PushModel;
import com.wll.talker.bean.card.GroupCard;
import com.wll.talker.bean.card.GroupMemberCard;
import com.wll.talker.bean.card.MessageCard;
import com.wll.talker.bean.card.UserCard;
import com.wll.talker.bean.db.*;
import com.wll.talker.utils.Hib;
import com.wll.talker.utils.PushDispatcher;
import com.wll.talker.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ��Ϣ�洢�봦��Ĺ�����
 *
 * created by detachment on 2020/6/1
 */
public class PushFactory {
    // ����һ����Ϣ�����ڵ�ǰ�ķ�����ʷ��¼�д洢��¼
    public static void pushNewMessage(User sender, Message message) {
        if (sender == null || message == null)
            return;

        // ��Ϣ��Ƭ���ڷ���
        MessageCard card = new MessageCard(message);
        // Ҫ���͵��ַ���
        String entity = TextUtil.toJson(card);

        // ������
        PushDispatcher dispatcher = new PushDispatcher();

        if (message.getGroup() == null
                && Strings.isNullOrEmpty(message.getGroupId())) {
            // �����ѷ�����Ϣ

            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null)
                return;

            // ��ʷ��¼���ֶν���
            PushHistory history = new PushHistory();
            // ��ͨ��Ϣ����
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setReceiver(receiver);
            // �����ߵ�ǰ���豸����Id
            history.setReceiverPushId(receiver.getPushId());


            // ���͵���ʵModel
            PushModel pushModel = new PushModel();
            // ÿһ����ʷ��¼���Ƕ����ģ����Ե����ķ���
            pushModel.add(history.getEntityType(), history.getEntity());

            // ����Ҫ���͵����ݣ����������߽��з���
            dispatcher.add(receiver, pushModel);

            // ���浽���ݿ�
            Hib.queryOnly(session -> session.save(history));
        } else {

            Group group = message.getGroup();
            // ��Ϊ�ӳټ����������Ϊnull����Ҫͨ��Id��ѯ
            if (group == null)
                group = GroupFactory.findById(message.getGroupId());

            // ���Ⱥ���û�У��򷵻�
            if (group == null)
                return;

            // ��Ⱥ��Ա������Ϣ
            Set<GroupMember> members = GroupFactory.getMembers(group);
            if (members == null || members.size() == 0)
                return;

            // �������Լ�
            members = members.stream()
                    .filter(groupMember -> !groupMember.getUserId()
                            .equalsIgnoreCase(sender.getId()))
                    .collect(Collectors.toSet());
            if (members.size() == 0)
                return;

            // һ����ʷ��¼�б�
            List<PushHistory> histories = new ArrayList<>();

            addGroupMembersPushModel(dispatcher, // ���͵ķ�����
                    histories, // ���ݿ�Ҫ�洢���б�
                    members,    // ���еĳ�Ա
                    entity, // Ҫ���͵�����
                    PushModel.ENTITY_TYPE_MESSAGE); // ���͵�����

            // ���浽���ݿ�Ĳ���
            Hib.queryOnly(session -> {
                for (PushHistory history : histories) {
                    session.saveOrUpdate(history);
                }
            });
        }


        // �����߽�����ʵ���ύ
        dispatcher.submit();

    }

    /**
     * ��Ⱥ��Ա����һ����Ϣ��
     * ����Ϣ�洢�����ݿ����ʷ��¼�У�ÿ���ˣ�ÿ����Ϣ����һ����¼
     */
    private static void addGroupMembersPushModel(PushDispatcher dispatcher,
                                                 List<PushHistory> histories,
                                                 Set<GroupMember> members,
                                                 String entity,
                                                 int entityTypeMessage) {
        for (GroupMember member : members) {
            // ����ͨ��Id��ȥ���û�
            User receiver = member.getUser();
            if (receiver == null)
                return;

            // ��ʷ��¼���ֶν���
            PushHistory history = new PushHistory();
            history.setEntityType(entityTypeMessage);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId());
            histories.add(history);

            // ����һ����ϢModel
            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());

            // ��ӵ������ߵ����ݼ���
            dispatcher.add(receiver, pushModel);
        }
    }


    /**
     * ֪ͨһЩ��Ա����������XXXȺ
     *
     * @param members ������Ⱥ�ĳ�Ա
     */
    public static void pushJoinGroup(Set<GroupMember> members) {

        // ������
        PushDispatcher dispatcher = new PushDispatcher();

        // һ����ʷ��¼�б�
        List<PushHistory> histories = new ArrayList<>();

        for (GroupMember member : members) {
            User receiver = member.getUser();
            if (receiver == null)
                return;

            // ÿ����Ա����Ϣ��Ƭ
//            GroupMemberCard memberCard = new GroupMemberCard(member);
//            String entity = TextUtil.toJson(memberCard);

            GroupCard groupCard = new GroupCard(member);
            String entity = TextUtil.toJson(groupCard);

            // ��ʷ��¼���ֶν���
            PushHistory history = new PushHistory();
            // �㱻��ӵ�Ⱥ������
            history.setEntityType(PushModel.ENTITY_TYPE_ADD_GROUP);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId());
            histories.add(history);

            // ����һ����ϢModel
            PushModel pushModel = new PushModel()
                    .add(history.getEntityType(), history.getEntity());

            // ��ӵ������ߵ����ݼ���
            dispatcher.add(receiver, pushModel);
            histories.add(history);
        }

        // ���浽���ݿ�Ĳ���
        Hib.queryOnly(session -> {
            for (PushHistory history : histories) {
                session.saveOrUpdate(history);
            }
        });

        // �ύ����
        dispatcher.submit();
    }

    /**
     * ֪ͨ�ϳ�Ա����һϵ���µĳ�Ա���뵽ĳ��Ⱥ
     *
     * @param oldMembers  �ϵĳ�Ա
     * @param insertCards �µĳ�Ա����Ϣ����
     */
    public static void pushGroupMemberAdd(Set<GroupMember> oldMembers, List<GroupMemberCard> insertCards) {
        // ������
        PushDispatcher dispatcher = new PushDispatcher();

        // һ����ʷ��¼�б�
        List<PushHistory> histories = new ArrayList<>();

        // ��ǰ�������û��ļ��ϵ�Json�ַ���
        String entity = TextUtil.toJson(insertCards);

        // ����ѭ����ӣ���oldMembersÿһ���ϵ��û�����һ����Ϣ����Ϣ������Ϊ�������û��ļ���
        // ֪ͨ�������ǣ�Ⱥ��Ա����˵�����
        addGroupMembersPushModel(dispatcher, histories, oldMembers,
                entity, PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS);

        // ���浽���ݿ�Ĳ���
        Hib.queryOnly(session -> {
            for (PushHistory history : histories) {
                session.saveOrUpdate(history);
            }
        });

        // �ύ����
        dispatcher.submit();
    }

    /**
     * �����˻��˳���Ϣ
     *
     * @param receiver ������
     * @param pushId   ���ʱ�̵Ľ����ߵ��豸Id
     */
    public static void pushLogout(User receiver, String pushId) {
        // ��ʷ��¼���ֶν���
        PushHistory history = new PushHistory();
        // �㱻��ӵ�Ⱥ������
        history.setEntityType(PushModel.ENTITY_TYPE_LOGOUT);
        history.setEntity("Account logout!!!");
        history.setReceiver(receiver);
        history.setReceiverPushId(pushId);
        // ���浽��ʷ��¼��
        Hib.queryOnly(session -> session.save(history));

        // ������
        PushDispatcher dispatcher = new PushDispatcher();
        // �������͵�����
        PushModel pushModel = new PushModel()
                .add(history.getEntityType(), history.getEntity());

        // ��Ӳ��ύ������������
        dispatcher.add(receiver, pushModel);
        dispatcher.submit();
    }

    /**
     * ��һ�����������ҵ���Ϣ��ȥ
     * �����ǣ��ҹ�ע����
     *
     * @param receiver ������
     * @param userCard �ҵĿ�Ƭ��Ϣ
     */
    public static void pushFollow(User receiver, UserCard userCard) {
        // һ�����໥��ע��
        userCard.setFollow(true);
        String entity = TextUtil.toJson(userCard);

        // ��ʷ��¼���ֶν���
        PushHistory history = new PushHistory();
        // �㱻��ӵ�Ⱥ������
        history.setEntityType(PushModel.ENTITY_TYPE_ADD_FRIEND);
        history.setEntity(entity);
        history.setReceiver(receiver);
        history.setReceiverPushId(receiver.getPushId());
        // ���浽��ʷ��¼��
        Hib.queryOnly(session -> session.save(history));

        // ����
        PushDispatcher dispatcher = new PushDispatcher();
        PushModel pushModel = new PushModel()
                .add(history.getEntityType(), history.getEntity());
        dispatcher.add(receiver, pushModel);
        dispatcher.submit();
    }
}
