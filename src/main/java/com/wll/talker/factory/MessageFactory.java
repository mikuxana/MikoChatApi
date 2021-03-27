package com.wll.talker.factory;

import com.wll.talker.bean.api.message.MessageCreateModel;
import com.wll.talker.bean.db.Group;
import com.wll.talker.bean.db.Message;
import com.wll.talker.bean.db.User;
import com.wll.talker.utils.Hib;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 消息数据存储的类
 *
 * created by detachment on 2020/6/1
 */
public class MessageFactory {
    // 查询某一个消息
    public static Message findById(String id) {
        return Hib.query(session -> session.get(Message.class, id));
    }

    // 添加一条普通消息
    public static Message add(User sender, User receiver, MessageCreateModel model) {
        Message message = new Message(sender, receiver, model);
        return save(message);
    }

    // 添加一条群消息
    public static Message add(User sender, Group group, MessageCreateModel model) {
        Message message = new Message(sender, group, model);
        return save(message);
    }

    private static Message save(Message message) {
//        Logger.getLogger("Message")
//                .log(Level.INFO, String.valueOf(message));
        return Hib.query(session -> {
            session.save(message);

            // 写入到数据库
            session.flush();

            // 紧接着从数据库中查询出来
            session.refresh(message);
            return message;
        });
    }

}
