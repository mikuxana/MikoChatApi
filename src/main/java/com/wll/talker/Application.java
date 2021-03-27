package com.wll.talker;

import com.wll.talker.provider.AuthRequestFilter;
import com.wll.talker.provider.GsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * created by detachment on 2020/5/21
 */
import java.util.logging.Logger;


public class Application extends ResourceConfig  {

    public Application() {
        // ע���߼�����İ���
        packages("com.wll.talker.service");
//        packages(AccountService.class.getPackage().getName());

        // ע�����ǵ�ȫ������������
        register(AuthRequestFilter.class);

        // ע��Json������
        // register(JacksonJsonProvider.class);
        // �滻������ΪGson
        register(GsonProvider.class);

        // ע����־��ӡ���
        register(Logger.class);
    }

}

