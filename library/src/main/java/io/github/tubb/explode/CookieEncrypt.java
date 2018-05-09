package io.github.tubb.explode;

/**
 * Created by Administrator on 2018/5/9.
 */

public final class CookieEncrypt {
    private CookieEncrypt() {}

    public static native String encrypt(String originCookie);

    public static native String decrypt(String encryptedCookie);

    public static String aesEncrypt(String originCookie, String key) {
        try {
            return AESCrypt.encrypt(key, originCookie);
        } catch (Exception e) {
            if (ExplodeLog.DEBUG)
                ExplodeLog.e("Encrypt cookie occur error", e);
        }
        return "";
    }

    public static String aesDecrypt(String encryptedCookie, String key) {
        try {
            return AESCrypt.decrypt(key, encryptedCookie);
        } catch (Exception e) {
            if (ExplodeLog.DEBUG)
                ExplodeLog.e("Encrypt cookie occur error", e);
        }
        return "";
    }
}
