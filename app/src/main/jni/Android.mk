LOCAL_PATH		:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := TestJniLibName
LOCAL_SRC_FILES := com_winter_testblur_StackNative.c jnitest.c stackblur.c
LOCAL_LDLIBS    := -lm -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)