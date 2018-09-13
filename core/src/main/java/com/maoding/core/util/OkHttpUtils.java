package com.maoding.core.util;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.core.util.OkHttpUtils
 * 作者: 张成亮
 * 描述: 调用网络服务
 **/
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.common.okhttp.BaseHttpCallback;
import okhttp3.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils extends OkHttpClientHttpRequestFactory {
    private static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";
    private static final String MEDIA_TYPE_FILE = "application/octet-stream; charset=utf-8";
    private static final String MEDIA_TYPE_DEFAULT = MEDIA_TYPE_JSON;

    private static final int CONNECTION_TIME_OUT = 20;
    private static final int READ_TIME_OUT = 30;
    private static final int WRITE_TIME_OUT = 30;

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build();

    private static final String CHARSET_NAME = "UTF-8";

    private static OkHttpClient getHttpClient(){
        if (httpClient == null){
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }

    /**
     * 同步请求
     * @throws IOException 网络访问异常
     */
    public static Response execute(Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

    /**
     * 异步请求（带回调）
     */
    public static void enqueue(Request request, Callback responseCallback) {
        httpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 异步请求（不在意返回结果，实现空callback)
     */
    public static void enqueue(Request request) {
        httpClient.newCall(request).enqueue(new BaseHttpCallback());
    }


    /**
     * 同步PostJson
     * @throws Exception 网络访问异常
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
     * @throws Exception 网络访问异常
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
     * @throws IOException
     */
    public static Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    /**
     * 异步获取Response
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
     * 这里使用了HttpClinet的API
     */
    public static String formatParams(List<BasicNameValuePair> params) {
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数
     */
    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params) {
        return url + "?" + formatParams(params);
    }

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数
     */
    public static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }

    /**
     * 取消指定请求
     */
    public void cancelRequest(Object tag) {
        for (Call call : httpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : httpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelAllRequest() {
        for (Call call : httpClient.dispatcher().queuedCalls())
            call.cancel();

        for (Call call : httpClient.dispatcher().runningCalls())
            call.cancel();
    }

    /**
     * 描述     使用post连接发送数据并等待返回
     * 日期     2018/8/9
     * @author  张成亮
     * @return  服务返回信息，如果调用错误产生UnsupportedOperationException异常
     * @param   url 服务地址
     * @param   data 调用参数
     **/
    public static <T> AjaxMessage postData(String url, T data) {
        //创建申请对象
        Request request = createPostRequest(url,data);
        //同步调用
        AjaxMessage result;
        try {
            //发出申请
            Response response = getHttpClient().newCall(request)
                    .execute();

            //获取返回值
            result = getAjaxMessage(response);
        } catch (IOException e) {
            String logMessage = getErrorMessage(url,true);
            String showMessage = getErrorMessage(url,false);

            if (StringUtils.isNotEmpty(e.getMessage())) {
                logMessage += ":" + e.getMessage();
                showMessage += ":" + e.getMessage();
            }
            TraceUtils.error(logMessage);
            throw new UnsupportedOperationException(showMessage);
        }

        return result;
    }

    /**
     * 描述     使用post连接发送数据并等待返回
     * 日期     2018/8/16
     * @author  张成亮
     * @return  服务返回信息内的data属性，如果调用错误产生UnsupportedOperationException异常
     * @param   url 服务地址
     * @param   data 调用参数
     * @param   clazz 期待返回内容的类型
     **/
    public static <T,R> R postData(String url, T data, Class<? extends R> clazz) {
        //发出申请并取得返回值
        AjaxMessage ajaxMessage = postData(url,data);

        //返回调用结果内的data属性
        return getData(url,ajaxMessage,clazz);
    }

    //从返回值解析出卯丁定义接口返回值
    private static AjaxMessage getAjaxMessage(Response response) throws IOException {
        //获取返回值
        if (response == null || !response.isSuccessful()){
            throw new IOException();
        }

        //获取返回值内的卯丁定义的接口返回值
        AjaxMessage apiResult = null;
        ResponseBody body = response.body();
        if (body != null) {
            try {
                apiResult = BeanUtils.createFromJson(body.string(), AjaxMessage.class);
            } catch (Exception e) {
                throw new IOException();
            }
        }
        return apiResult;
    }

    //从返回值内解析出实际包含的数据,如果包含错误则产生IOException异常
    private static <R> R getData(String url, AjaxMessage ajaxMessage, Class<? extends R> clazz) {
        R result = null;
        //如果调用结果正常，返回结果，否则报告出现异常
        if (ajaxMessage != null) {
            if ("0".equals(ajaxMessage.getCode())) {
                result = BeanUtils.createFrom(ajaxMessage.getData(),clazz);
            } else {
                String logMessage = getErrorMessage(url,true);
                String showMessage = getErrorMessage(url,false);

                if (ObjectUtils.isNotEmpty(ajaxMessage.getInfo())) {
                    logMessage += ":" + ajaxMessage.getInfo();
                    showMessage += ":" + ajaxMessage.getInfo();
                }
                TraceUtils.error(logMessage);
                throw new UnsupportedOperationException(showMessage);
            }
        }

        return result;
    }

    //获取错误字符串
    private static String getErrorMessage(String url,boolean isAddCaller){
        String msg = "访问" + url + "时出现错误";
        if (isAddCaller){
            msg += ":" + TraceUtils.getCaller();
        }
        return msg;
    }

    /**
     * 描述     使用post连接发送数据,不等待返回数据
     * 日期     2018/8/9
     * @author  张成亮
     * @param   url 服务地址
     * @param   data 调用参数
     **/
    public static <T> void postDataAsync(String url, T data, Callback callback) {
        //创建申请对象
        Request request = createPostRequest(url,data);
        //异步调用
        getHttpClient().newCall(request)
                .enqueue((callback != null) ? callback : new BaseHttpCallback());
    }

    public static <T> void postDataAsync(String url, T data) {
        postDataAsync(url,data,null);
    }

    //创建发送JSON申请对象
    private static <T> Request createPostRequest(String url, T data){
        String json = StringUtils.toJsonString(data);
        MediaType mediaType = MediaType.parse(MEDIA_TYPE_DEFAULT);
        RequestBody body = RequestBody.create(mediaType, json);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }
}