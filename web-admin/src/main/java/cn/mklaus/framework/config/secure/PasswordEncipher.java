package cn.mklaus.framework.config.secure;


import com.alibaba.druid.util.Utils;

/**
 * @author klausxie
 * @date 2020-08-08
 */
public class PasswordEncipher {

    public static String encrypt(String password, String salt) {
        return Utils.md5(Utils.md5(password+salt) + salt);
    }

}
