#include <jni.h>
#include <string>

const std::string KEY = "Opu@Tea%4*1";
const std::string SIG = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";
const std::string AES_ENCRYPT_NAME = "aesEncrypt";
const std::string AES_DECRYPT_NAME = "aesDecrypt";

jstring callMethod(JNIEnv *env, jclass &clazz, jmethodID &methodId, jstring &handleStr);

extern "C"
JNIEXPORT jstring

JNICALL
Java_io_github_tubb_explode_CookieEncrypt_encrypt(
        JNIEnv *env,
        jclass clazz,
        jstring originCookie) {
    jmethodID aesEncryptMethodID = env->GetStaticMethodID(clazz, AES_ENCRYPT_NAME.c_str(), SIG.c_str());
    return callMethod(env, clazz, aesEncryptMethodID, originCookie);
}

JNIEXPORT jstring

JNICALL
Java_io_github_tubb_explode_CookieEncrypt_decrypt(
        JNIEnv *env,
        jclass clazz,
        jstring encryptedCookie) {
    jmethodID aesDecryptMethodID = env->GetStaticMethodID(clazz, AES_DECRYPT_NAME.c_str(), SIG.c_str());
    return callMethod(env, clazz, aesDecryptMethodID, encryptedCookie);
}

jstring callMethod(JNIEnv *env, jclass &clazz, jmethodID &methodId, jstring &handleStr) {
    const char *key_chars = KEY.c_str();
    jstring key = env->NewStringUTF(key_chars);
    jstring data = (jstring)env->CallStaticObjectMethod(clazz, methodId, handleStr, key);
    jboolean isCopy;
    env->ReleaseStringUTFChars(key, env->GetStringUTFChars(key, &isCopy));
    return data;
}