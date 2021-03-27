package com.wll.talker.service;

import com.google.common.base.Strings;
import com.wll.talker.bean.api.base.ResponseModel;
import com.wll.talker.bean.api.user.UpdateInfoModel;
import com.wll.talker.bean.card.UserCard;
import com.wll.talker.bean.db.User;
import com.wll.talker.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * �û���Ϣ�����Service
 *
 * created by detachment on 2020/5/8
 */
// 127.0.0.1/api/user/...
@Path("/user")
public class UserService extends BaseService {

    // �û���Ϣ�޸Ľӿ�
    // �����Լ��ĸ�����Ϣ
    @PUT
    //@Path("") //127.0.0.1/api/user ����Ҫд�����ǵ�ǰĿ¼
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if (!UpdateInfoModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        // �����û���Ϣ
        self = model.updateToUser(self);
        self = UserFactory.update(self);
        // �����Լ����û���Ϣ
        UserCard card = new UserCard(self, true);
        // ����
        return ResponseModel.buildOk(card);
    }

    // ��ȡ��ϵ��
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();

        // �õ��ҵ���ϵ��
        List<User> users = UserFactory.contacts(self);
        // ת��ΪUserCard
        List<UserCard> userCards = users.stream()
                // map�������൱��ת�ò�����User->UserCard
                .map(user -> new UserCard(user, true))
                .collect(Collectors.toList());
        // ����
        return ResponseModel.buildOk(userCards);
    }

    // ��ע�ˣ�
    // �򻯣���ע�˵Ĳ�����ʵ��˫��ͬʱ��ע
    @PUT // �޸���ʹ��Put
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();

        // ���ܹ�ע���Լ�
        if (self.getId().equalsIgnoreCase(followId)
                || Strings.isNullOrEmpty(followId)) {
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }


        // �ҵ���Ҳ��ע����
        User followUser = UserFactory.findById(followId);
        if (followUser == null) {
            // δ�ҵ���
            return ResponseModel.buildNotFoundUserError(null);
        }

        // ��עĬ��û�У����������չ
        followUser = UserFactory.follow(self, followUser, null);
        if (followUser == null) {
            // ��עʧ�ܣ����ط������쳣
            return ResponseModel.buildServiceError();
        }

        // TODO ֪ͨ�ҹ�ע�����ҹ�ע��

        // ���ع�ע���˵���Ϣ
        return ResponseModel.buildOk(new UserCard(followUser, true));
    }


    // ��ȡĳ�˵���Ϣ
    @GET
    @Path("{id}") // http://127.0.0.1/api/user/{id}
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }


        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id)) {
            // �����Լ������ز�ѯ���ݿ�
            return ResponseModel.buildOk(new UserCard(self, true));
        }


        User user = UserFactory.findById(id);
        if (user == null) {
            // û�ҵ�������û�ҵ��û�
            return ResponseModel.buildNotFoundUserError(null);
        }


        // �������ֱ���й�ע�ļ�¼�������ѹ�ע��Ҫ��ѯ��Ϣ���û�
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOk(new UserCard(user, isFollow));
    }


    // �����˵Ľӿ�ʵ��
    // Ϊ�˼򻯷�ҳ��ֻ����20������
    @GET // �����ˣ����漰���ݸ��ģ�ֻ�ǲ�ѯ����ΪGET
    // http://127.0.0.1/api/user/search/
    @Path("/search/{name:(.*)?}") // ����Ϊ�����ַ�������Ϊ��
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
        User self = getSelf();

        // �Ȳ�ѯ����
        List<User> searchUsers = UserFactory.search(name);
        // �Ѳ�ѯ���˷�װΪUserCard
        // �ж���Щ���Ƿ������Ѿ���ע���ˣ�
        // ����У��򷵻صĹ�ע״̬��Ӧ���Ѿ����ú�״̬

        // �ó��ҵ���ϵ��
        final List<User> contacts = UserFactory.contacts(self);

        // ��User->UserCard
        List<UserCard> userCards = searchUsers.stream()
                .map(user -> {
                    // �ж�������Ƿ������Լ����������ҵ���ϵ���е���
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            // ������ϵ�˵�����ƥ�䣬ƥ�����е�Id�ֶ�
                            || contacts.stream().anyMatch(
                            contactUser -> contactUser.getId()
                                    .equalsIgnoreCase(user.getId())
                    );

                    return new UserCard(user, isFollow);
                }).collect(Collectors.toList());
        // ����
        return ResponseModel.buildOk(userCards);
    }

}
