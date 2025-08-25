package com.dubu.download.msnetwork.utils;

import com.dubu.download.msnetwork.NvsServerClient;
import com.dubu.download.msnetwork.custom.SimpleDownListener;
import com.dubu.download.msnetwork.server.task.XExecutor;


/**
 * 下载帮助类
 * @author cq
 * @date 2025/07/04
 */
public class HttpDownloadManager {


    /**
     * 下载实现方法
     *
     * @param tag      下载标识  Download tag
     * @param url      下载地址 Download address
     * @param filePath 文件路径 File path
     * @param fileName 文件名 File name
     * @param listener 下载监听器 Download listener
     */
    public static void download(String tag, String url, String filePath, String fileName, SimpleDownListener listener) {
        NvsServerClient.get().download(tag, url, filePath, fileName, listener);
    }

    /**
     * 取消某个请求
     *
     * @param tag 唯一标识
     */
    public static void cancelRequest(Object tag) {
        NvsServerClient.get().cancelRequest(tag);
    }


    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        NvsServerClient.get().cancelAll();
    }


    /**
     * 所有任务执行完成 回调 添加
     */
    public static void addOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        NvsServerClient.get().onAllTaskEnd(listener);
    }

    /**
     * 所有任务执行完成 回调 移除
     */
    public static void removeOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        NvsServerClient.get().removeOnAllTaskEndListener(listener);
    }

    /**
     * 所有任务执行完成
     */
    public static void removeAllTask() {
        NvsServerClient.get().removeAllTask();
    }

    /**
     * 所有任务执行完成
     */
    public static void removeAllTaskAllen() {
        NvsServerClient.get().removeAllTaskAllen();
    }
}
