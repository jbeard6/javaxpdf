#ifndef HANDLE_H_
#define HANDLE_H_

#include <jni.h>

jfieldID getHandleField(JNIEnv *, jobject, const char *);

template <typename T>
jlong getHandleFor(T *t)
{
	return reinterpret_cast<jlong>(t);
}

template <typename T>
T *getHandle(JNIEnv *env, jobject obj)
{
	jlong handle = env->GetLongField(obj, getHandleField(env, obj, "_handle"));
	return reinterpret_cast<T *>(handle);
}

template <typename T>
void setHandle(JNIEnv *env, jobject obj, T *t)
{
    jlong handle = getHandleFor(t);
    env->SetLongField(obj, getHandleField(env, obj, "_handle"), handle);
}

#endif /* HANDLE_H_ */
