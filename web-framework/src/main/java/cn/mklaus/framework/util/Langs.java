package cn.mklaus.framework.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mklaus
 * @date 2018-01-02 上午11:27
 */
public class Langs {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String numberStr(int length) {
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int id = ThreadLocalRandom.current().nextInt(chars.length);
            sb.append(chars[id]);
        }
        return sb.toString();
    }

    public static int randomInt(int start, int end){
        if (end < start) {
            throw new IllegalArgumentException(start + " < " + end);
        }
        return ThreadLocalRandom.current().nextInt((end - start) + 1) + start;
    }

    public static String shortCode() {
        return ShortUrl.generate(uuid())[0];
    }

    public static String shortCode(String url) {
        return ShortUrl.generate(url)[0];
    }

    public static JSONObject toJSONObject(Object o) {
        return JSONObject.parseObject(JSONObject.toJSONString(o));
    }

    public static boolean IS_MAC_OS = System.getProperties().getProperty("os.name").toLowerCase().contains("mac os");

}
