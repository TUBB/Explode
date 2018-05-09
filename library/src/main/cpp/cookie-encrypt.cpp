#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG    "Explode" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__) // 定义LOGD类型

const std::string KEY = "wfklakfka";
const std::string SIG = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";
const std::string AES_ENCRYPT_NAME = "aesEncrypt";
const std::string AES_DECRYPT_NAME = "aesDecrypt";

extern "C"
JNIEXPORT jstring

JNICALL
Java_io_github_tubb_explode_CookieEncrypt_encrypt(
        JNIEnv *env,
        jclass clazz,
        jstring originCookie) {
    jmethodID aesEncryptMethodID = env->GetStaticMethodID(clazz, AES_ENCRYPT_NAME.c_str(), SIG.c_str());
    jstring data = (jstring)env->CallStaticObjectMethod(clazz, aesEncryptMethodID, originCookie, env->NewStringUTF(KEY.c_str()));
    return data;
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_io_github_tubb_explode_CookieEncrypt_decrypt(
        JNIEnv *env,
        jclass clazz,
        jstring encryptedCookie) {
    jmethodID aesDecryptMethodID = env->GetStaticMethodID(clazz, AES_DECRYPT_NAME.c_str(), SIG.c_str());
    jstring data = (jstring)env->CallStaticObjectMethod(clazz, aesDecryptMethodID, encryptedCookie, env->NewStringUTF(KEY.c_str()));
    return data;
}