package com.valuestudio.web.webservice;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * JSON数据解析类
 *
 * @author zuolong
 * @description
 */
public class JSONParseUtil {

    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    public static final String PAGE_TOTAL = "pageTotal";
    public static final String ITEM_TOTAL = "itemTotal";
    public static final String VERSION = "version";
    public static final String ROOT = "root";

    /**
     * 解析请求结果
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static boolean getRequestResult(JSONObject jsonObject)
            throws JSONException {
        return toBool(jsonObject.get(SUCCESS).toString().trim());
    }

    /**
     * 解析总页数
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static int getPageTotal(JSONObject jsonObject) throws JSONException {
        JSONObject messageObject = jsonObject.getJSONObject(MESSAGE);
        // 判断是否有pageTotal字段
        if (messageObject.has(PAGE_TOTAL)) {
            return toInt(messageObject.get(PAGE_TOTAL).toString()
                    .trim());
        } else {
            return 0;
        }
    }

    /**
     * 解析总记录数
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static int getItemTotal(JSONObject jsonObject) throws JSONException {
        JSONObject messageObject = jsonObject.getJSONObject(MESSAGE);
        // 判断是否有itemTotal字段
        if (messageObject.has(ITEM_TOTAL)) {
            return toInt(messageObject.get(ITEM_TOTAL).toString()
                    .trim());
        } else {
            return 0;
        }
    }

    /**
     * 解析版本号
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static String getVersion(JSONObject jsonObject) throws JSONException {
        JSONObject messageObject = jsonObject.getJSONObject(MESSAGE);
        // 判断是否有itemTotal字段
        if (messageObject.has(VERSION)) {
            return messageObject.get(VERSION).toString()
                    .trim();
        } else {
            return "";
        }
    }

    /**
     * 解析记录数
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static JSONArray getDatas(JSONObject jsonObject)
            throws JSONException {
        JSONObject messageObject = jsonObject.getJSONObject(MESSAGE);
        // 判断是否有root字段
        if (messageObject.has(ROOT)) {
            return messageObject.getJSONArray(ROOT);
        } else {
            return null;
        }
    }

    /**
     * 解析message
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static JSONObject getMessage(JSONObject jsonObject)
            throws JSONException {
        return jsonObject.getJSONObject(MESSAGE);
    }

    /**
     * 解析message数组
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static JSONArray getMessageArray(JSONObject jsonObject)
            throws JSONException {
        return jsonObject.getJSONArray(MESSAGE);
    }

    /**
     * 获取请求信息
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static Object getResultMsg(JSONObject jsonObject)
            throws JSONException {
        return jsonObject.get(MESSAGE);
    }

/**
 * 解析后通过反射赋值到对象
 *
 * @param clazz
 * @param jsonObj
 * @return
 * @throws Exception
 */
public static Object reflectObject(Class clazz, JSONObject jsonObj)
        throws JSONException, IllegalAccessException,
        InstantiationException {
    Object instance = clazz.newInstance();
    Field[] fields = clazz.getDeclaredFields();
    String attribute = null;
    for (Field field : fields) {
        attribute = field.getName();
        // 判断返回数据中是否含有相对字段并且不为“null”
        if (jsonObj.has(attribute) && !jsonObj.isNull(attribute)) {
            String value = jsonObj.get(attribute).toString();
            if (TextUtils.isEmpty(value)) {
                field.set(instance, "");
                continue;
            }
            if (field.getType() == int.class) {
                field.setInt(instance, Integer.parseInt(value));
            } else if (field.getType() == float.class) {
                field.setFloat(instance, Float.parseFloat(value));
            } else if (field.getType() == double.class) {
                field.setDouble(instance, Double.parseDouble(value));
            } else if (field.getType() == long.class) {
                field.setLong(instance, Long.parseLong(value));
            } else if (field.getType() == boolean.class) {
                field.setBoolean(instance, Boolean.parseBoolean(value));
            } else if (field.getType() == JSONArray.class) {
                field.set(instance, new JSONArray(value));
            } else {
                field.set(instance, field.getType().cast(value));
            }
        } else {
            if (!attribute.equals("serialVersionUID")) {// 过滤序列号类中的serialVersionUID字段
                if (field.getType() == int.class) {
                    field.setInt(instance, 0);
                } else if (field.getType() == float.class) {
                    field.setFloat(instance, 0);
                } else if (field.getType() == double.class) {
                    field.setDouble(instance, 0);
                } else if (field.getType() == long.class) {
                    field.setLong(instance, 0);
                } else if (field.getType() == boolean.class) {
                    field.setBoolean(instance, false);
                } else if (field.getType() == String.class) {
                    field.set(instance, "");
                } else {
                    field.set(instance, null);
                }
            }
        }
    }
    return instance;
}

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

}
