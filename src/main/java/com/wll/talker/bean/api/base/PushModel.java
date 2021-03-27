package com.wll.talker.bean.api.base;

import com.google.gson.annotations.Expose;
import com.wll.talker.utils.TextUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * һ�����͵ľ���Model���ڲ�ά����һ�����飬������Ӷ��ʵ��
 * ÿ�����͵���ϸ�����ǣ���ʵ���������Json������Ȼ����Json�ַ���
 * ��������Ŀ���ǣ����ٶ�����ͣ�����ж����Ϣ��Ҫ���Ϳ��Ժϲ�����
 *
 * created by detachment on 2020/6/1
 */
@SuppressWarnings("WeakerAccess")
public class PushModel {
    public static final int ENTITY_TYPE_LOGOUT = -1;
    public static final int ENTITY_TYPE_MESSAGE = 200;
    public static final int ENTITY_TYPE_ADD_FRIEND = 1001;
    public static final int ENTITY_TYPE_ADD_GROUP = 1002;
    public static final int ENTITY_TYPE_ADD_GROUP_MEMBERS = 1003;
    public static final int ENTITY_TYPE_MODIFY_GROUP_MEMBERS = 2001;
    public static final int ENTITY_TYPE_EXIT_GROUP_MEMBERS = 3001;

    private List<Entity> entities = new ArrayList<>();

    public PushModel add(Entity entity) {
        entities.add(entity);
        return this;
    }

    public PushModel add(int type, String content) {
        return add(new Entity(type, content));
    }

    public String getPushString() {
        if (entities.size() == 0)
            return null;
        return TextUtil.toJson(entities);
    }

    /**
     * �����ʵ�����ͣ������ʵ���а�װ��ʵ������ݺ�����
     * ������Ӻ��ѵ����ͣ�
     * content���û���Ϣ��Json�ַ���
     * type=ENTITY_TYPE_ADD_FRIEND
     */
    public static class Entity {
        public Entity(int type, String content) {
            this.type = type;
            this.content = content;
        }

        // ��Ϣ����
        @Expose
        public int type;
        // ��Ϣʵ��
        @Expose
        public String content;
        // ��Ϣ����ʱ��
        @Expose
        public LocalDateTime createAt = LocalDateTime.now();
    }
}
