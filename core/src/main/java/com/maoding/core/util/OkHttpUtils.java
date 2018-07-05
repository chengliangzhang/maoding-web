package com.maoding.core.util;

/**
 * Created by Wuwq on 2017/1/3.
 */

import okhttp3.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

public class OkHttpUtils {

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 该不会开启异步线程。
     *
     * @param request
     *
     * @return
     *
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        httpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    /**
     * 同步PostJson
     *
     * @param url
     * @param param
     *
     * @return
     *
     * @throws Exception
     */
    public static Response postJson(String url, Object param) throws Exception {
        String json = JsonUtils.obj2json(param);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return execute(request);
    }

    /**
     * 异步PostJson
     *
     * @param url
     * @param param
     * @param responseCallback
     *
     * @throws Exception
     */
    public static void postJsonAsync(String url, Object param, Callback responseCallback) throws Exception {
        String json = JsonUtils.obj2json(param);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        if (responseCallback == null)
            enqueue(request);
        else
            enqueue(request, responseCallback);
    }

    /**
     * 同步获取Response
     *
     * @param url
     *
     * @throws IOException
     */
    public static Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    /**
     * 异步获取Response
     *
     * @param url
     *
     * @return
     *
     * @throws IOException
     */
    public static void getAsync(String url, Callback responseCallback) throws IOException {
        Request request = new Request.Builder().url(url).build();
        if (responseCallback == null)
            enqueue(request);
        else
            enqueue(request, responseCallback);
    }

    /**
     * 这里使用了HttpClinet的API。只是为了方便
     *
     * @param params
     *
     * @return
     */
    public static String formatParams(List<BasicNameValuePair> params) {
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     *
     * @param url
     * @param params
     *
     * @return
     */
    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params) {
        return url + "?" + formatParams(params);
    }

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     *
     * @param url
     * @param name
     * @param value
     *
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }
}