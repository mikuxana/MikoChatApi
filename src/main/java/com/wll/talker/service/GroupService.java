package com.wll.talker.service;

import com.google.common.base.Strings;
import com.wll.talker.bean.api.base.ResponseModel;
import com.wll.talker.bean.api.group.GroupCreateModel;
import com.wll.talker.bean.api.group.GroupMemberAddModel;
import com.wll.talker.bean.api.group.GroupMemberUpdateModel;
import com.wll.talker.bean.card.ApplyCard;
import com.wll.talker.bean.card.GroupCard;
import com.wll.talker.bean.card.GroupMemberCard;
import com.wll.talker.bean.db.Group;
import com.wll.talker.bean.db.GroupMember;
import com.wll.talker.bean.db.User;
import com.wll.talker.factory.GroupFactory;
import com.wll.talker.factory.PushFactory;
import com.wll.talker.factory.UserFactory;
import com.wll.talker.provider.LocalDateTimeConverter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ⱥ��Ľӿڵ����
 *
 * created by detachment on 2020/6/28
 */
@Path("/group")
public class GroupService extends BaseService {
    /**
     * ����Ⱥ
     *
     * @param model ��������
     * @return Ⱥ��Ϣ
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> create(GroupCreateModel model) {
        if (!GroupCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        // ������
        User creator = getSelf();
        // �����߲������б���
        model.getUsers().remove(creator.getId());
        if (model.getUsers().size() == 0) {
            return ResponseModel.buildParameterError();
        }

        // ����Ƿ�����
        if (GroupFactory.findByName(model.getName()) != null) {
            return ResponseModel.buildHaveNameError();
        }

        List<User> users = new ArrayList<>();
        for (String s : model.getUsers()) {
            User user = UserFactory.findById(s);
            if (user == null)
                continue;
            users.add(user);
        }
        // û��һ����Ա
        if (users.size() == 0) {
            return ResponseModel.buildParameterError();
        }

        Group group = GroupFactory.create(creator, model, users);
        if (group == null) {
            // �������쳣
            return ResponseModel.buildServiceError();
        }

        // �ù���Ա����Ϣ���Լ�����Ϣ��
        GroupMember creatorMember = GroupFactory.getMember(creator.getId(), group.getId());
        if (creatorMember == null) {
            // �������쳣
            return ResponseModel.buildServiceError();
        }

        // �õ�Ⱥ�ĳ�Ա�������е�Ⱥ��Ա������Ϣ���Ѿ�����ӵ�Ⱥ����Ϣ
        Set<GroupMember> members = GroupFactory.getMembers(group);
        if (members == null) {
            // �������쳣
            return ResponseModel.buildServiceError();
        }

        members = members.stream()
                .filter(groupMember -> !groupMember.getId().equalsIgnoreCase(creatorMember.getId()))
                .collect(Collectors.toSet());

        // ��ʼ��������
        PushFactory.pushJoinGroup(members);

        return ResponseModel.buildOk(new GroupCard(creatorMember));
    }

    /**
     * ����Ⱥ��û�д��ݲ�����������������е�Ⱥ
     *
     * @param name �����Ĳ���
     * @return Ⱥ��Ϣ�б�
     */
    @GET
    @Path("/search/{name:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>> search(@PathParam("name") @DefaultValue("") String name) {
        User self = getSelf();
        List<Group> groups = GroupFactory.search(name);
        if (groups != null && groups.size() > 0) {
            List<GroupCard> groupCards = groups.stream()
                    .map(group -> {
                        GroupMember member = GroupFactory.getMember(self.getId(), group.getId());
                        return new GroupCard(group, member);
                    }).collect(Collectors.toList());
            return ResponseModel.buildOk(groupCards);
        }
        return ResponseModel.buildOk();
    }

    /**
     * ��ȡ�Լ���ǰ��Ⱥ���б�
     *
     * @param dateStr ʱ���ֶΣ������ݣ��򷵻�ȫ����ǰ��Ⱥ�б���ʱ�䣬�򷵻����ʱ��֮��ļ����Ⱥ
     * @return Ⱥ��Ϣ�б�
     */
    @GET
    @Path("/list/{date:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>> list(@DefaultValue("") @PathParam("date") String dateStr) {
        User self = getSelf();

        // ��ʱ��
        LocalDateTime dateTime = null;
        if (!Strings.isNullOrEmpty(dateStr)) {
            try {
                dateTime = LocalDateTime.parse(dateStr, LocalDateTimeConverter.FORMATTER);
            } catch (Exception e) {
                dateTime = null;
            }
        }

        Set<GroupMember> members = GroupFactory.getMembers(self);
        if (members == null || members.size() == 0)
            return ResponseModel.buildOk();


        final LocalDateTime finalDateTime = dateTime;
        List<GroupCard> groupCards = members.stream()
                .filter(groupMember -> finalDateTime == null // ʱ�����Ϊnull��������
                        || groupMember.getUpdateAt().isAfter(finalDateTime) // ʱ�䲻Ϊnull,����Ҫ���ҵ����ʱ��֮��
                )
                .map(GroupCard::new) // ת������
                .collect(Collectors.toList());

        return ResponseModel.buildOk(groupCards);
    }

    /**
     * ��ȡһ��Ⱥ����Ϣ, �������Ⱥ�ĳ�Ա
     *
     * @param id Ⱥ��Id
     * @return Ⱥ����Ϣ
     */
    @GET
    @Path("/{groupId}")
    //http:.../api/group/0000-0000-0000-0000
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> getGroup(@PathParam("groupId") String id) {
        if (Strings.isNullOrEmpty(id))
            return ResponseModel.buildParameterError();

        User self = getSelf();
        GroupMember member = GroupFactory.getMember(self.getId(), id);
        if (member == null) {
            return ResponseModel.buildNotFoundGroupError(null);
        }

        return ResponseModel.buildOk(new GroupCard(member));
    }

    /**
     * ��ȡһ��Ⱥ�����г�Ա��������ǳ�Ա֮һ
     *
     * @param groupId Ⱥid
     * @return ��Ա�б�
     */
    @GET
    @Path("/{groupId}/member")
    //http:.../api/group/0000-0000-0000-0000/member
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> members(@PathParam("groupId") String groupId) {
        User self = getSelf();

        // û�����Ⱥ
        Group group = GroupFactory.findById(groupId);
        if (group == null)
            return ResponseModel.buildNotFoundGroupError(null);

        // ���Ȩ��
        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);
        if (selfMember == null)
            return ResponseModel.buildNoPermissionError();

        // ���еĳ�Ա
        Set<GroupMember> members = GroupFactory.getMembers(group);
        if (members == null)
            return ResponseModel.buildServiceError();

        // ����
        List<GroupMemberCard> memberCards = members
                .stream()
                .map(GroupMemberCard::new)
                .collect(Collectors.toList());

        return ResponseModel.buildOk(memberCards);
    }

    /**
     * ��Ⱥ��ӳ�Ա�Ľӿ�
     *
     * @param groupId ȺId������������Ⱥ�Ĺ�����֮һ
     * @param model   ��ӳ�Ա��Model
     * @return ��ӳ�Ա�б�
     */
    @POST
    @Path("/{groupId}/member")
    //http:.../api/group/0000-0000-0000-0000/member
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> memberAdd(@PathParam("groupId") String groupId, GroupMemberAddModel model) {
        if (Strings.isNullOrEmpty(groupId) || !GroupMemberAddModel.check(model))
            return ResponseModel.buildParameterError();

        // �õ��ҵ���Ϣ
        User self = getSelf();

        // �Ƴ���֮���ٽ����ж�����
        model.getUsers().remove(self.getId());
        if (model.getUsers().size() == 0)
            return ResponseModel.buildParameterError();

        // û�����Ⱥ
        Group group = GroupFactory.findById(groupId);
        if (group == null)
            return ResponseModel.buildNotFoundGroupError(null);

        // �ұ����ǳ�Ա, ͬʱ�ǹ���Ա�������ϼ���
        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);
        if (selfMember == null || selfMember.getPermissionType() == GroupMember.PERMISSION_TYPE_NONE)
            return ResponseModel.buildNoPermissionError();


        // ���еĳ�Ա
        Set<GroupMember> oldMembers = GroupFactory.getMembers(group);
        Set<String> oldMemberUserIds = oldMembers.stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toSet());


        List<User> insertUsers = new ArrayList<>();
        for (String s : model.getUsers()) {
            // ����
            User user = UserFactory.findById(s);
            if (user == null)
                continue;
            // �Ѿ���Ⱥ����
            if(oldMemberUserIds.contains(user.getId()))
                continue;

            insertUsers.add(user);
        }
        // û��һ�������ĳ�Ա
        if (insertUsers.size() == 0) {
            return ResponseModel.buildParameterError();
        }

        // ������Ӳ���
        Set<GroupMember> insertMembers =  GroupFactory.addMembers(group, insertUsers);
        if(insertMembers==null)
            return ResponseModel.buildServiceError();


        // ת��
        List<GroupMemberCard> insertCards = insertMembers.stream()
                .map(GroupMemberCard::new)
                .collect(Collectors.toList());

        // ֪ͨ��������
        // 1.֪ͨ�����ĳ�Ա���㱻������XXXȺ
        PushFactory.pushJoinGroup(insertMembers);

        // 2.֪ͨȺ���ϵĳ�Ա����XXX��XXX����Ⱥ
        PushFactory.pushGroupMemberAdd(oldMembers, insertCards);

        return ResponseModel.buildOk(insertCards);
    }


    /**
     * ���ĳ�Ա��Ϣ���������Ҫô�ǹ���Ա��Ҫô���ǳ�Ա����
     *
     * @param memberId ��ԱId�����Բ�ѯ��Ӧ��Ⱥ������
     * @param model    �޸ĵ�Model
     * @return ��ǰ��Ա����Ϣ
     */
    @PUT
    @Path("/member/{memberId}")
    //http:.../api/group/member/0000-0000-0000-0000
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupMemberCard> modifyMember(@PathParam("memberId") String memberId, GroupMemberUpdateModel model) {
        return null;
    }


    /**
     * �������һ��Ⱥ��
     * ��ʱ�ᴴ��һ����������룬��д���Ȼ��������Ա������Ϣ
     * ����Աͬ�⣬��ʵ���ǵ�����ӳ�Ա�ĽӿڰѶ�Ӧ���û���ӽ�ȥ
     *
     * @param groupId ȺId
     * @return �������Ϣ
     */
    @POST
    @Path("/applyJoin/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<ApplyCard> join(@PathParam("groupId") String groupId) {
        return null;
    }

}
