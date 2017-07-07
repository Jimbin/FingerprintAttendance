package com.example.wangchang.testbottomnavigationbar;

/**
 * Created by lele on 2017/6/17.
 */

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 将字节流转换为字符串的工具类
 */
public class HttpUtils {

    //将InputSAtream转为字符串
    public static String readMyInputStream(InputStream is) {
        byte[] result;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer))!=-1) {
                baos.write(buffer,0,len);
            }
            is.close();
            baos.close();
            result = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            String errorStr = "获取数据失败。";
            return errorStr;
        }
        return new String(result);
    }

    public static void getHttpData(final  String Path ,final Handler handler)
    {
        /**
         * 点击按钮事件，在主线程中开启一个子线程进行网络请求
         * （因为在4.0只有不支持主线程进行网络请求，所以一般情况下，建议另开启子线程进行网络请求等耗时操作）。
         */
        new Thread() {
            public void run() {
                int code;
                try {
                    URL url = new URL(Path);
                    /**
                     * 这里网络请求使用的是类HttpURLConnection，另外一种可以选择使用类HttpClient。
                     */
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");//使用GET方法获取
                    conn.setConnectTimeout(5000);
                    code = conn.getResponseCode();
                    if (code == 200) {
                        /**
                         * 如果获取的code为200，则证明数据获取是正确的。
                         */
                        InputStream is = conn.getInputStream();
                        String result = HttpUtils.readMyInputStream(is);

                        /**
                         * 子线程发送消息到主线程，并将获取的结果带到主线程，让主线程来更新UI。
                         */
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = 1;
                        handler.sendMessage(msg);

                    } else {

                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    /**
                     * 如果获取失败，或出现异常，那么子线程发送失败的消息（FAILURE）到主线程，主线程显示Toast，来告诉使用者，数据获取是失败。
                     */
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            };
        }.start();  }

    public static void postHttpData(final  String Path,final Handler handler,final String body)
    {
        /**
         * 点击按钮事件，在主线程中开启一个子线程进行网络请求
         * （因为在4.0只有不支持主线程进行网络请求，所以一般情况下，建议另开启子线程进行网络请求等耗时操作）。
         */
        new Thread() {
            public void run() {
                int code;
                try {
                    URL url = new URL(Path);
                    /**
                     * 这里网络请求使用的是类HttpURLConnection，另外一种可以选择使用类HttpClient。
                     */
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");//使用GET方法获取
                    conn.setConnectTimeout(5000);

                    //请求头的信息

                    conn.setRequestProperty("Content-Length", String.valueOf(body.length()));
                    conn.setRequestProperty("Cache-Control", "max-age=0");
                    conn.setRequestProperty("Origin", "http://192.168.1.100:8081");

                    //设置conn可以写请求的内容
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(body.getBytes());

                    //响应码
                    code = conn.getResponseCode();
                    if (code == 200) {
                        /**
                         * 如果获取的code为200，则证明数据获取是正确的。
                         */
                        InputStream is = conn.getInputStream();
                        String result = HttpUtils.readMyInputStream(is);

                        /**
                         * 子线程发送消息到主线程，并将获取的结果带到主线程，让主线程来更新UI。
                         */
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = 1;
                        handler.sendMessage(msg);

                    } else {

                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    /**
                     * 如果获取失败，或出现异常，那么子线程发送失败的消息（FAILURE）到主线程，主线程显示Toast，来告诉使用者，数据获取是失败。
                     */
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            };
        }.start();  }

}