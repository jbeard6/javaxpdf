/*
 * JavaObjectWrapper.cpp
 *
 *  Created on: May 17, 2013
 *      Author: beardj
 */

#include <stdarg.h>
#include <stddef.h>
#include "JavaObjectWrapper.h"

JavaObjectWrapper::JavaObjectWrapper(JNIEnv *env, jobject obj)
{
	this->env = env;
	this->obj = obj;
	this->cls = env->GetObjectClass(obj);
}

JavaObjectWrapper::~JavaObjectWrapper() {
	this->cls = NULL;
	this->obj = NULL;
	this->env = NULL;
}

void JavaObjectWrapper::CallVoidMethod(const char* methodName,
		const char* signature, ...)
{
    va_list args;
    va_start(args,signature);

    jmethodID methodID = GetMethodID(methodName, signature);
	this->env->CallVoidMethodV(obj, methodID, args);

	va_end(args);
}

jint JavaObjectWrapper::CallIntMethod(const char* methodName,
		const char* signature, ...)
{
    va_list args;
    va_start(args,signature);

    jmethodID methodID = GetMethodID(methodName, signature);
    jint result = this->env->CallIntMethodV(obj, methodID, args);

	va_end(args);

	return result;
}
