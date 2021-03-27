package com.wll.talker.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * ��������Jersey��Jsonת����
 * �����滻JacksonJsonProvider
 * <p>
 * �ù���������ˣ���Http�����е���������ת��ΪModelʵ�壬
 * ͬʱҲʵ���˰ѷ��ص�Modelʵ��ת��ΪJson�ַ���
 * �������Http�ķ������С�
 *
 * @param <T> �������ͷ��Ͷ���
 *
 * created by detachment on 2020/5/8
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {
    // ����һ��ȫ�ֵ�Gson
    private static final Gson gson;

    static {
        // Gson ��ʼ��
        GsonBuilder builder = new GsonBuilder()
                // ���л�Ϊnull���ֶ�
                .serializeNulls()
                // �����������@Exposeע��ı���
                .excludeFieldsWithoutExposeAnnotation()
                // ֧��Map
                .enableComplexMapKeySerialization();
        // ��Ӷ�Java8LocalDateTimeʱ�����͵�֧��
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter());
        gson = builder.create();
    }

    /**
     * ȡ��һ��ȫ�ֵ�Gson
     *
     * @return Gson
     */
    public static Gson getGson() {
        return gson;
    }

    public GsonProvider() {
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    /**
     * ��Json���ַ�������, ת��ΪT���͵�ʵ��
     */
    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations,
                      MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                      InputStream entityStream) throws IOException, WebApplicationException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(entityStream, "UTF-8"))) {
            return gson.fromJson(reader, genericType);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    /**
     * ��һ��T���ʵ�������Http�������
     */
    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        //TypeAdapter<T> adapter = gson.getAdapter((TypeToken<T>) TypeToken.get(genericType));
        try (JsonWriter jsonWriter = gson.newJsonWriter(new OutputStreamWriter(entityStream, Charset.forName("UTF-8")))) {
            gson.toJson(t, genericType, jsonWriter);
            jsonWriter.close();
        }
    }
}
