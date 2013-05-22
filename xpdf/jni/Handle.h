#ifndef HANDLE_H_
#define HANDLE_H_

#include <jni.h>

jfieldID getHandleField(JNIEnv *, jobject, const char *);

template <typename T>
jlong getHandleFor(T *t);

template <typename T>
T *getHandle(JNIEnv *env, jobject obj);

template <typename T>
void setHandle(JNIEnv *env, jobject obj, T *t);


#endif /* HANDLE_H_ */
