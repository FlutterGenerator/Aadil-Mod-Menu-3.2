#include <list>
#include <vector>
#include <string.h>
#include <pthread.h>
#include <thread>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include <iostream>
#include <dlfcn.h>
#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "Includes/Utils.h"
#include "KittyMemory/MemoryPatch.h"
#include "NepAU/AutoUpdate.cpp"

//Target lib here
#define targetLibName OBFUSCATE("libil2cpp.so")

#include "Menu/Setup.h"
#include "Includes/Macros.h"

// we will run our hacks in a new thread so our while loop doesn't block process main thread
void *hack_thread(void *) {
    LOGI(OBFUSCATE("pthread created"));

    //Check if target lib is loaded
    do {
        sleep(1);
    } while (!isLibraryLoaded(targetLibName));

    //Anti-lib rename
    /*
    do {
        sleep(1);
    } while (!isLibraryLoaded("libYOURNAME.so"));*/

    LOGI(OBFUSCATE("%s has been loaded"), (const char *) targetLibName);

#if defined(__aarch64__) //To compile this code for arm64 lib only. Do not worry about greyed out highlighting code, it still works

#else //To compile this code for armv7 lib only.

    LOGI(OBFUSCATE("Done"));
#endif

    //Anti-leech
    /*if (!iconValid || !initValid || !settingsValid) {
        //Bad function to make it crash
        sleep(5);
        int *p = 0;
        *p = 0;
    }*/

    return NULL;
}

void Changes(JNIEnv *env, jclass clazz, jobject obj,
                                        jint featNum, jstring featName, jint value,
                                        jboolean boolean, jstring str) {

    LOGD(OBFUSCATE("Feature name: %d - %s | Value: = %d | Bool: = %d | Text: = %s"), featNum,
         env->GetStringUTFChars(featName, 0), value,
         boolean, str != NULL ? env->GetStringUTFChars(str, 0) : "");

    //BE CAREFUL NOT TO ACCIDENTLY REMOVE break;

    switch (featNum) {
        case 0:
        break;
    }
}

__attribute__((constructor))
void lib_main() {
    // Create a new thread so it does not block the main thread, means the game would not freeze
    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);
}

int RegisterMenu(JNIEnv *env) {
    JNINativeMethod methods[] = {
            {OBFUSCATE("Icon"), OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(Icon)},
            {OBFUSCATE("IconWebViewData"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(IconWebViewData)},
            {OBFUSCATE("IsGameLibLoaded"),  OBFUSCATE("()Z"), reinterpret_cast<void *>(isGameLibLoaded)},
            {OBFUSCATE("Init"),  OBFUSCATE("(Landroid/content/Context;Landroid/widget/TextView;Landroid/widget/TextView;)V"), reinterpret_cast<void *>(Init)},
            {OBFUSCATE("SettingsList"),  OBFUSCATE("()[Ljava/lang/String;"), reinterpret_cast<void *>(SettingsList)},
            {OBFUSCATE("GetFeatureList"),  OBFUSCATE("()[Ljava/lang/String;"), reinterpret_cast<void *>(GetFeatureList)},
            {OBFUSCATE("UnlimitedAll3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(UnlimitedAll3)},
            {OBFUSCATE("NoGravity3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(NoGravity3)},
            {OBFUSCATE("God1_3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(God1_3)},
            {OBFUSCATE("God2_3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(God2_3)},
            {OBFUSCATE("God3_3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(God3_3)},
            {OBFUSCATE("JumpLimit3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(JumpLimit3)},
            {OBFUSCATE("JumpHeight3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(JumpHeight3)},
            {OBFUSCATE("AirJumpHeight3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(AirJumpHeight3)},
            {OBFUSCATE("InstantLane3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(InstantLane3)},
            {OBFUSCATE("Score3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(Score3)},
            {OBFUSCATE("CompleteAchievement3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(CompleteAchievement3)},
            {OBFUSCATE("FollowCamera3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(FollowCamera3)},
            {OBFUSCATE("StopCamera3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(StopCamera3)},
            {OBFUSCATE("StopTrain3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(StopTrain3)},
            {OBFUSCATE("NoBoun3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(NoBoun3)},
            {OBFUSCATE("Speed1_3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(Speed1_3)},
            {OBFUSCATE("Speed2_3"),  OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(Speed2_3)},
    };

    jclass clazz = env->FindClass(OBFUSCATE("com/aadil/Menu"));
    if (!clazz)
        return JNI_ERR;
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0)
        return JNI_ERR;
    return JNI_OK;
}

int RegisterPreferences(JNIEnv *env) {
    JNINativeMethod methods[] = {
            {OBFUSCATE("Changes"), OBFUSCATE("(Landroid/content/Context;ILjava/lang/String;IZLjava/lang/String;)V"), reinterpret_cast<void *>(Changes)},
    };
    jclass clazz = env->FindClass(OBFUSCATE("com/aadil/Preferences"));
    if (!clazz)
        return JNI_ERR;
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0)
        return JNI_ERR;
    return JNI_OK;
}

int RegisterMain(JNIEnv *env) {
    JNINativeMethod methods[] = {
            {OBFUSCATE("CheckOverlayPermission"), OBFUSCATE("(Landroid/content/Context;)V"), reinterpret_cast<void *>(CheckOverlayPermission)},
    };
    jclass clazz = env->FindClass(OBFUSCATE("com/aadil/Main"));
    if (!clazz)
        return JNI_ERR;
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0)
        return JNI_ERR;

    return JNI_OK;
}

int RegisterLauncher(JNIEnv *env) {
    JNINativeMethod methods[] = {
            {OBFUSCATE("Aadil"), OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(Aadil)},
            {OBFUSCATE("Aadil2"), OBFUSCATE("()Ljava/lang/String;"), reinterpret_cast<void *>(Aadil2)},
    };
    jclass clazz = env->FindClass(OBFUSCATE("com/aadil/Launcher"));
    if (!clazz)
        return JNI_ERR;
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0)
        return JNI_ERR;
    return JNI_OK;
}

extern "C"
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (RegisterMenu(env) != 0)
        return JNI_ERR;
    if (RegisterPreferences(env) != 0)
        return JNI_ERR;
    if (RegisterMain(env) != 0)
        return JNI_ERR;
    if (RegisterLauncher(env) != 0)
        return JNI_ERR;
    return JNI_VERSION_1_6;
}
