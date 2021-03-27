package com.wll.talker.provider;

import com.google.common.base.Strings;
import com.wll.talker.bean.api.base.ResponseModel;
import com.wll.talker.bean.db.User;
import com.wll.talker.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * �������е�����ĽӿڵĹ��˺�����?
 *
 * created by detachment on 2020/5/8
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {

    // ʵ�ֽӿڵĹ��˷���
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // ����Ƿ��ǵ�¼ע��ӿ�
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login")
                || relationPath.startsWith("account/register")) {
            // ֱ���������߼�����������
            return;
        }


        // ��Headers��ȥ�ҵ���һ��token�ڵ�
        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) {

            // ��ѯ�Լ�����Ϣ
            final User self = UserFactory.findByToken(token);
            if (self != null) {
                // ����ǰ�������һ��������
                requestContext.setSecurityContext(new SecurityContext() {
                    // ���岿��
                    @Override
                    public Principal getUserPrincipal() {
                        // User ʵ�� Principal�ӿ�
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // ����������д���û���Ȩ�ޣ�role ��Ȩ������
                        // ���Թ������ԱȨ�޵ȵ�
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        // Ĭ��false���ɣ�HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        // �������
                        return null;
                    }
                });
                // д�������ĺ�ͷ���
                return;
            }
        }

        // ֱ�ӷ���һ���˻���Ҫ��¼��Model
        ResponseModel model = ResponseModel.buildAccountError();
        // ����һ������
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();
        // ���أ�ֹͣһ������ļ����·������ø÷�����֮�䷵������
        // �����ߵ�Service��ȥ
        requestContext.abortWith(response);

    }
}
