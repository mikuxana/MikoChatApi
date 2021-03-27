package com.wll.talker.utils;

import com.wll.talker.provider.GsonProvider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * created by detachment on 2020/5/8
 */
public class TextUtil {
    /**
     * ����һ���ַ�����MD5��Ϣ
     *
     * @param str �ַ���
     * @return MD5ֵ
     */
    public static String getMD5(String str) {
        try {
            // ����һ��MD5���ܼ���ժҪ
            MessageDigest md = MessageDigest.getInstance("MD5");
            // ����md5����
            md.update(str.getBytes());
            // digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
            // BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * ��һ���ַ�������Base64����
     *
     * @param str ԭʼ�ַ���
     * @return ����Base64�������ַ���
     */
    public static String encodeBase64(String str) {
        return Base64
                .getEncoder()
                .encodeToString(str.getBytes());
    }

    /**
     * ���������ʵ��ת��ΪJson�ַ���
     *
     * @param obj Object
     * @return Json�ַ���
     */
    public static String toJson(Object obj) {
        return GsonProvider.getGson().toJson(obj);
    }
}
