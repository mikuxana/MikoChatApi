package com.wll.talker.factory;

import com.wll.talker.bean.api.message.MessageCreateModel;
import com.wll.talker.bean.db.Group;
import com.wll.talker.bean.db.Message;
import com.wll.talker.bean.db.User;
import com.wll.talker.utils.Hib;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ��Ϣ���ݴ洢����
 *
 * created by detachment on 2020/6/1
 */
public class MessageFactory {
    // ��ѯĳһ����Ϣ
    public static Message findById(String id) {
        return Hib.query(session -> session.get(Message.class, id));
    }

    // ���һ����ͨ��Ϣ
    public static Message add(User sender, User receiver, MessageCreateModel model) {
        Message message = new Message(sender, receiver, model);
        return save(message);
    }

    // ���һ��Ⱥ��Ϣ
    public static Message add(User sender, Group group, MessageCreateModel model) {
        Message message = new Message(sender, group, model);
        return save(message);
    }

    private static Message save(Message message) {
//        Logger.getLogger("Message")
//                .log(Level.INFO, String.valueOf(message));
        return Hib.query(session -> {
            session.save(message);

            // д�뵽���ݿ�
            session.flush();

            // �����Ŵ����ݿ��в�ѯ����
            session.refresh(message);
            return message;
        });
    }

}
