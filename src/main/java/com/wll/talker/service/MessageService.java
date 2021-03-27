package com.wll.talker.service;

import com.wll.talker.bean.api.base.ResponseModel;
import com.wll.talker.bean.api.message.MessageCreateModel;
import com.wll.talker.bean.card.MessageCard;
import com.wll.talker.bean.db.Group;
import com.wll.talker.bean.db.Message;
import com.wll.talker.bean.db.User;
import com.wll.talker.factory.GroupFactory;
import com.wll.talker.factory.MessageFactory;
import com.wll.talker.factory.PushFactory;
import com.wll.talker.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * ��Ϣ���͵����
 *
 * created by detachment on 2020/6/2
 */
@Path("/msg")
public class MessageService extends BaseService {
    // ����һ����Ϣ��������
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<MessageCard> pushMessage(MessageCreateModel model) {
        if (!MessageCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();

        // ��ѯ�Ƿ��Ѿ������ݿ�������
        Message message = MessageFactory.findById(model.getId());
        if (message != null) {
            // ��������
            return ResponseModel.buildOk(new MessageCard(message));
        }

        if (model.getReceiverType() == Message.RECEIVER_TYPE_GROUP) {
            return pushToGroup(self, model);
        } else {
            return pushToUser(self, model);
        }
    }

    // ���͵���
    private ResponseModel<MessageCard> pushToUser(User sender, MessageCreateModel model) {
        User receiver = UserFactory.findById(model.getReceiverId());
        if (receiver == null) {
            // û���ҵ�������
            return ResponseModel.buildNotFoundUserError("Con't find receiver user");
        }

        if (receiver.getId().equalsIgnoreCase(sender.getId())) {
            // �����߽�������ͬһ���˾ͷ��ش�����Ϣʧ��
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        // �洢���ݿ�
        Message message = MessageFactory.add(sender, receiver, model);

        return buildAndPushResponse(sender, message);
    }

    // ���͵�Ⱥ
    private ResponseModel<MessageCard> pushToGroup(User sender, MessageCreateModel model) {
        // ��Ⱥ����Ȩ�����ʵ���
        Group group = GroupFactory.findById(sender, model.getReceiverId());
        if (group == null) {
            // û���ҵ�������Ⱥ���п������㲻��Ⱥ�ĳ�Ա
            return ResponseModel.buildNotFoundUserError("Con't find receiver group");
        }

        // ��ӵ����ݿ�
        Message message = MessageFactory.add(sender, group, model);

        // ��ͨ�õ������߼�
        return buildAndPushResponse(sender, message);
    }

    // ���Ͳ�����һ��������Ϣ
    private ResponseModel<MessageCard> buildAndPushResponse(User sender, Message message) {
        if (message == null) {
            // �洢���ݿ�ʧ��
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        // ��������
        PushFactory.pushNewMessage(sender, message);

        // ����
        return ResponseModel.buildOk(new MessageCard(message));
    }
}
