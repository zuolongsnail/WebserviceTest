package com.valuestudio.web.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义日志类
 *
 * @author zuolong
 * @version V1.0
 * @title LogUtil
 * @description 自定义日志类
 */
public class LogUtil {
    /**
     * 保存日志的文件名
     */
    public static String LOG_FILE_NAME = "valuestudio.log";
    /**
     * 是否打印日志
     */
    public static boolean isDebug = true;
    /**
     * 是否保存到文件
     */
    public static boolean saveFile = false;

    public static String LOG_DIR = "";

    public static int v(String tag, String msg) {
        if (saveFile) {
            log2File(tag, msg);
        }
        return android.util.Log.v(tag, msg);
    }

    public static int d(String tag, String msg) {
        if (saveFile) {
            log2File(tag, msg);
        }
        return android.util.Log.d(tag, msg);
    }

    public static int i(String tag, String msg) {
        if (saveFile) {
            log2File(tag, msg);
        }
        return android.util.Log.i(tag, msg);
    }

    public static int w(String tag, String msg) {
        if (saveFile) {
            log2File(tag, msg);
        }
        return android.util.Log.w(tag, msg);
    }

    public static int e(String tag, String msg) {
        if (saveFile) {
            log2File(tag, msg);
        }
        return android.util.Log.e(tag, msg);
    }

    /**
     * 写文件
     *
     * @param tag
     * @param content
     */
    public static void log2File(String tag, String content) {
        try {
            if (checkLogDirExists()) {
                FileWriter writer = new FileWriter(LOG_DIR + File.separator
                        + LOG_FILE_NAME, true);
                writer.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(System.currentTimeMillis()))
                        + " "
                        + tag + " " + content + "\r\n");
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkLogDirExists() {
        File dir = new File(Environment.getExternalStorageDirectory(),
                BaseConstant.APP_SD_PATH + BaseConstant.LOG_FILE_DIR);
        if (TextUtils.isEmpty(LOG_DIR)) {
            LOG_DIR = dir.getPath();
        }
        if (!dir.exists() || !dir.isDirectory()) {
            boolean ret = dir.mkdirs();
            return ret;
        } else {
            return true;
        }
    }
}