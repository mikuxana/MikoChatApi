package com.wll.talker.service;

import com.wll.talker.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * created by detachment on 2020/5/8
 */
public class BaseService {
    // ���һ��������ע�⣬��ע����securityContext��ֵ
    // �����ֵΪ���ǵ��������������ص�SecurityContext
    @Context
    protected SecurityContext securityContext;


    /**
     * ����������ֱ�ӻ�ȡ�Լ�����Ϣ
     *
     * @return User
     */
    protected User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
