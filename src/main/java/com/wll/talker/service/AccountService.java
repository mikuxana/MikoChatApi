package com.wll.talker.service;

import com.google.common.base.Strings;
import com.wll.talker.bean.api.account.AccountRspModel;
import com.wll.talker.bean.api.account.LoginModel;
import com.wll.talker.bean.api.account.RegisterModel;
import com.wll.talker.bean.api.base.ResponseModel;
import com.wll.talker.bean.db.User;
import com.wll.talker.bean.db.UserFollow;
import com.wll.talker.factory.UserFactory;
import com.wll.talker.utils.Hib;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * created by detachment on 2020/5/8
 */
// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService extends BaseService {

    @Path("/login")  //�����ַ127.0.0.1/api/account/login
    @GET   //get����  ����ط�Ҳ���Լ����ַpath
    public String get() {
        return "��¼�ɹ�";
    }

    // ��¼
    @POST
    @Path("/login")
    // ָ�������뷵�ص���Ӧ��ΪJSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        if (!LoginModel.check(model)) {
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if (user != null) {

            // �����Я��PushId
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }

            // ���ص�ǰ���˻�
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            // ��¼ʧ��
            return ResponseModel.buildLoginError();
        }
    }


    // ע��
    @POST
    @Path("/register")
    // ָ�������뷵�ص���Ӧ��ΪJSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        if (!RegisterModel.check(model)) {
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            // �����˻�
            return ResponseModel.buildHaveAccountError();
        }

        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            // �����û���
            return ResponseModel.buildHaveNameError();
        }

        // ��ʼע���߼�
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getName());

        if (user != null) {

            // �����Я��PushId
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }

            // ���ص�ǰ���˻�
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            // ע���쳣
            return ResponseModel.buildRegisterError();
        }
    }


    // ���豸Id
    @POST
    @Path("/bind/{pushId}")
    // ָ�������뷵�ص���Ӧ��ΪJSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // ������ͷ�л�ȡtoken�ֶ�
    // pushId��url��ַ�л�ȡ
    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token,
                                               @PathParam("pushId") String pushId) {
        if (Strings.isNullOrEmpty(token) ||
                Strings.isNullOrEmpty(pushId)) {
            // ���ز����쳣
            return ResponseModel.buildParameterError();
        }

        // �õ��Լ��ĸ�����Ϣ
        // User user = UserFactory.findByToken(token);
        User self = getSelf();
        return bind(self, pushId);
    }


    /**
     * �󶨵Ĳ���
     *
     * @param self   �Լ�
     * @param pushId PushId
     * @return User
     */
    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        // �����豸Id�󶨵Ĳ���
        User user = UserFactory.bindPushId(self, pushId);

        if (user == null) {
            // ��ʧ�����Ƿ������쳣
            return ResponseModel.buildServiceError();

        }

        // ���ص�ǰ���˻�, �����Ѿ�����
        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOk(rspModel);
    }
}
