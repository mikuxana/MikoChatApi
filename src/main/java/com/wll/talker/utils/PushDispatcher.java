package com.wll.talker.utils;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.base.Strings;
import com.wll.talker.bean.api.base.PushModel;
import com.wll.talker.bean.db.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ��Ϣ���͹�����
 *
 * created by detachment on 2020/6/1
 */
public class PushDispatcher {
    private static final String appId = "Wo7IeGHLHj7B6XGWR5htQA";
    private static final String appKey = "ATDSNbUoLn9WQHZXQS3xw8";
    private static final String masterSecret = "oUnmIaZaVCAs9AlrIDaTZ5";
    private static final String host = "http://sdk.open.api.igexin.com/apiex.htm";

    private final IGtPush pusher;
    // Ҫ�յ���Ϣ���˺����ݵ��б�
    private final List<BatchBean> beans = new ArrayList<>();

    public PushDispatcher() {
        // ������ķ�����
        pusher = new IGtPush(host, appKey, masterSecret);
    }

    /**
     * ���һ����Ϣ
     *
     * @param receiver ������
     * @param model    ���յ�����Model
     * @return �Ƿ���ӳɹ�
     */
    public boolean add(User receiver, PushModel model) {
        // ������飬�����н����ߵ��豸��Id
        if (receiver == null || model == null ||
                Strings.isNullOrEmpty(receiver.getPushId()))
            return false;

        String pushString = model.getPushString();
        if (Strings.isNullOrEmpty(pushString))
            return false;


        // ����һ��Ŀ��+����
        BatchBean bean = buildMessage(receiver.getPushId(), pushString);
        beans.add(bean);
        return true;
    }

    /**
     * ��Ҫ���͵����ݽ��и�ʽ����װ
     *
     * @param clientId �����ߵ��豸Id
     * @param text     Ҫ���յ�����
     * @return BatchBean
     */
    private BatchBean buildMessage(String clientId, String text) {
        // ͸����Ϣ������֪ͨ����ʾ��������MessageReceiver�յ�
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(text);
        template.setTransmissionType(0); //���TypeΪint�ͣ���д1���Զ�����app

        SingleMessage message = new SingleMessage();
        message.setData(template); // ��͸����Ϣ���õ�����Ϣģ����
        message.setOffline(true); // �Ƿ��������߷���
        message.setOfflineExpireTime(24 * 3600 * 1000); // ������Ϣʱ��

        // ��������Ŀ�꣬����appid��clientId
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);

        // ����һ����װ
        return new BatchBean(message, target);
    }


    // ������Ϣ���շ���
    public boolean submit() {
        // ��������Ĺ�����
        IBatch batch = pusher.getBatch();

        // �Ƿ���������Ҫ����
        boolean haveData = false;

        for (BatchBean bean : beans) {
            try {
                batch.add(bean.message, bean.target);
                haveData = true;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        // û�����ݾ�ֱ�ӷ���
        if (!haveData)
            return false;

        IPushResult result = null;
        try {
            result = batch.submit();
        } catch (IOException e) {
            e.printStackTrace();

            // ʧ������³����ظ�����һ��
            try {
                batch.retry();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        if (result != null) {
            try {
                Logger.getLogger("PushDispatcher")
                        .log(Level.INFO, (String) result.getResponse().get("result"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.getLogger("PushDispatcher")
                .log(Level.WARNING, "���ͷ�������Ӧ�쳣������");
        return false;

    }


    // ��ÿ���˷�����Ϣ��һ��Bean��װ
    private static class BatchBean {
        SingleMessage message;
        Target target;

        BatchBean(SingleMessage message, Target target) {
            this.message = message;
            this.target = target;
        }
    }
}
