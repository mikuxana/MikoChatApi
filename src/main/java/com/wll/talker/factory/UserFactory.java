package com.wll.talker.factory;

import com.google.common.base.Strings;
import com.wll.talker.bean.db.User;
import com.wll.talker.bean.db.UserFollow;
import com.wll.talker.utils.Hib;
import com.wll.talker.utils.TextUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * created by detachment on 2020/5/8
 */
public class UserFactory {
    // ͨ��Token�ֶβ�ѯ�û���Ϣ
    // ֻ���Լ�ʹ�ã���ѯ����Ϣ�Ǹ�����Ϣ����������Ϣ
    public static User findByToken(String token) {
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    // ͨ��Phone�ҵ�User
    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    // ͨ��Name�ҵ�User
    public static User findByName(String name) {
        return Hib.query(session -> (User) session
                .createQuery("from User where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    // ͨ��Name�ҵ�User
    public static User findById(String id) {
        // ͨ��Id��ѯ��������
        return Hib.query(session -> session.get(User.class, id));
    }

    /**
     * �����û���Ϣ�����ݿ�
     *
     * @param user User
     * @return User
     */
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }


    /**
     * ����ǰ���˻���PushId
     *
     * @param user   �Լ���User
     * @param pushId �Լ��豸��PushId
     * @return User
     */
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId))
            return null;

        // ��һ������ѯ�Ƿ��������˻���������豸
        // ȡ���󶨣��������ͻ���
        // ��ѯ���б��ܰ����Լ�
        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();

            for (User u : userList) {
                // ����Ϊnull
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });

        if (pushId.equalsIgnoreCase(user.getPushId())) {
            // �����ǰ��Ҫ�󶨵��豸Id��֮ǰ�Ѿ��󶨹���
            // ��ô����Ҫ�����
            return user;
        } else {
            // �����ǰ�˻�֮ǰ���豸Id������Ҫ�󶨵Ĳ�ͬ
            // ��ô��Ҫ�����¼����֮ǰ���豸�˳��˻���
            // ��֮ǰ���豸����һ���˳���Ϣ
            if (Strings.isNullOrEmpty(user.getPushId())) {
                // TODO ����һ���˳���Ϣ
            }

            // �����µ��豸Id
            user.setPushId(pushId);
            return update(user);
        }
    }

    /**
     * ʹ���˻���������е�¼
     */
    public static User login(String account, String password) {
        final String accountStr = account.trim();
        // ��ԭ�Ľ���ͬ���Ĵ���Ȼ�����ƥ��
        final String encodePassword = encodePassword(password);

        // Ѱ��
        User user = Hib.query(session -> (User) session
                .createQuery("from User where phone=:phone and password=:password")
                .setParameter("phone", accountStr)
                .setParameter("password", encodePassword)
                .uniqueResult());

        if (user != null) {
            // ��User���е�¼����������Token
            user = login(user);
        }
        return user;


    }


    /**
     * �û�ע��
     * ע��Ĳ�����Ҫд�����ݿ⣬���������ݿ��е�User��Ϣ
     *
     * @param account  �˻�
     * @param password ����
     * @param name     �û���
     * @return User
     */
    public static User register(String account, String password, String name) {
        // ȥ���˻��е���λ�ո�
        account = account.trim();
        // ��������
        password = encodePassword(password);

        User user = createUser(account, password, name);
        if (user != null) {
            user = login(user);
        }
        return user;
    }


    /**
     * ע�Ჿ�ֵ��½��û��߼�
     *
     * @param account  �ֻ���
     * @param password ���ܺ������
     * @param name     �û���
     * @return ����һ���û�
     */
    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        // �˻������ֻ���
        user.setPhone(account);

        // ���ݿ�洢
        return Hib.query(session -> {
            session.save(user);
            return user;
        });
    }


    /**
     * ��һ��User���е�¼����
     * �������Ƕ�Token���в���
     *
     * @param user User
     * @return User
     */
    private static User login(User user) {
        // ʹ��һ�������UUIDֵ�䵱Token
        String newToken = UUID.randomUUID().toString();
        // ����һ��Base64��ʽ��
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);

        return update(user);
    }


    /**
     * ��������м��ܲ���
     *
     * @param password ԭ��
     * @return ����
     */
    private static String encodePassword(String password) {
        // ����ȥ����λ�ո�
        password = password.trim();
        // ����MD5�ǶԳƼ��ܣ����λ����ȫ����Ҳ��Ҫ�洢
        password = TextUtil.getMD5(password);
        // �ٽ���һ�ζԳƵ�Base64���ܣ���Ȼ���Բ�ȡ���εķ���
        return TextUtil.encodeBase64(password);
    }


    /**
     * ��ȡ�ҵ���ϵ�˵��б�
     *
     * @param self User
     * @return List<User>
     */
    public static List<User> contacts(User self) {
        return Hib.query(session -> {
            // ���¼���һ���û���Ϣ��self�У��͵�ǰ��session��
            session.load(self, self.getId());

            // ��ȡ�ҹ�ע����
            Set<UserFollow> flows = self.getFollowing();

            // ʹ�ü�д��ʽ
            return flows.stream()
                    .map(UserFollow::getTarget)
                    .collect(Collectors.toList());

        });
    }

    /**
     * ��ע�˵Ĳ���
     *
     * @param origin ������
     * @param target ����ע����
     * @param alias  ��ע��
     * @return ����ע���˵���Ϣ
     */
    public static User follow(final User origin, final User target, final String alias) {
        UserFollow follow = getUserFollow(origin, target);
        if (follow != null) {
            // �ѹ�ע��ֱ�ӷ���
            return follow.getTarget();
        }

        return Hib.query(session -> {
            // ��Ҫ���������ص����ݣ���Ҫ����loadһ��
            session.load(origin, origin.getId());
            session.load(target, target.getId());

            // �ҹ�ע�˵�ʱ��ͬʱ��Ҳ��ע�ң�
            // ������Ҫ�������UserFollow����
            UserFollow originFollow = new UserFollow();
            originFollow.setOrigin(origin);
            originFollow.setTarget(target);
            // ��ע���Ҷ����ı�ע��������Ĭ��û�б�ע
            originFollow.setAlias(alias);

            // ���������������Ǳ���ע���˵ļ�¼
            UserFollow targetFollow = new UserFollow();
            targetFollow.setOrigin(target);
            targetFollow.setTarget(origin);

            // �������ݿ�
            session.save(originFollow);
            session.save(targetFollow);

            return target;
        });
    }


    /**
     * ��ѯ�������Ƿ��Ѿ���ע
     *
     * @param origin ������
     * @param target ����ע��
     * @return �����м���UserFollow
     */
    public static UserFollow getUserFollow(final User origin, final User target) {
        return Hib.query(session -> (UserFollow) session
                .createQuery("from UserFollow where originId = :originId and targetId = :targetId")
                .setParameter("originId", origin.getId())
                .setParameter("targetId", target.getId())
                .setMaxResults(1)
                // Ψһ��ѯ����
                .uniqueResult());
    }

    /**
     * ������ϵ�˵�ʵ��
     *
     * @param name ��ѯ��name������Ϊ��
     * @return ��ѯ�����û����ϣ����nameΪ�գ��򷵻�������û�
     */
    @SuppressWarnings("unchecked")
    public static List<User> search(String name) {
        if (Strings.isNullOrEmpty(name))
            name = ""; // ��֤����Ϊnull����������ٺ����һ���жϺͶ���Ĵ���
        final String searchName = "%" + name + "%"; // ģ��ƥ��

        return Hib.query(session -> {
            // ��ѯ��������name���Դ�Сд������ʹ��like��ģ������ѯ��
            // ͷ��������������Ʋ��ܲ�ѯ��
            return (List<User>) session.createQuery("from User where lower(name) like :name and portrait is not null and description is not null")
                    .setParameter("name", searchName)
                    .setMaxResults(20) // ����20��
                    .list();

        });

    }
}
