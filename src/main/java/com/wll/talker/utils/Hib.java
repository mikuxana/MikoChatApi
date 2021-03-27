package com.wll.talker.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * created by detachment on 2020/5/8
 */
public class Hib {
    // ȫ��SessionFactory
    private static SessionFactory sessionFactory;

    static {
        // ��̬��ʼ��sessionFactory
        init();
    }

    private static void init() {
        // ��hibernate.cfg.xml�ļ���ʼ��
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            // build һ��sessionFactory
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // �������ӡ�����������
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * ��ȡȫ�ֵ�SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    /**
     * ��SessionFactory�еõ�һ��Session�Ự
     *
     * @return Session
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * �ر�sessionFactory
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


    // �û���ʵ�ʵĲ�����һ���ӿ�
    public interface QueryOnly {
        void query(Session session);
    }

    // ��Session����ٵ�һ�����߷���
    public static void queryOnly(QueryOnly query) {
        // �ؿ�һ��Session
        Session session = sessionFactory.openSession();
        // ��������
        final Transaction transaction = session.beginTransaction();

        try {
            // ���ô��ݽ����Ľӿڣ�
            // �����ýӿڵķ�����Session���ݽ�ȥ
            query.query(session);
            // �ύ
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // �ع�
            try {
                transaction.rollback();
            } catch (RuntimeException e1) {
                e1.printStackTrace();
            }
        } finally {
            // ���۳ɹ�ʧ�ܣ�����Ҫ�ر�Session
            session.close();
        }
    }


    // �û���ʵ�ʵĲ�����һ���ӿ�
    // ���з���ֵT
    public interface Query<T> {
        T query(Session session);
    }

    // ��Session�����Ĺ��߷�����
    // ����һ������ֵ
    public static <T> T query(Query<T> query) {
        // �ؿ�һ��Session
        Session session = sessionFactory.openSession();
        // ��������
        final Transaction transaction = session.beginTransaction();

        T t = null;
        try {
            // ���ô��ݽ����Ľӿڣ�
            // �����ýӿڵķ�����Session���ݽ�ȥ
            t = query.query(session);
            // �ύ
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // �ع�
            try {
                transaction.rollback();
            } catch (RuntimeException e1) {
                e1.printStackTrace();
            }
        } finally {
            // ���۳ɹ�ʧ�ܣ�����Ҫ�ر�Session
            session.close();
        }

        return t;
    }


}
