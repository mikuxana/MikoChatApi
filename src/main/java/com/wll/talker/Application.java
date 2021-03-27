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
        // 注册逻辑处理的包名
        packages("com.wll.talker.service");
//        packages(AccountService.class.getPackage().getName());

        // 注册我们的全局请求拦截器
        register(AuthRequestFilter.class);

        // 注册Json解析器
        // register(JacksonJsonProvider.class);
        // 替换解析器为Gson
        register(GsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);
    }

}

