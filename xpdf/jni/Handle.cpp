/*
 * Handle.cpp
 *
 *  Created on: May 22, 2013
 *      Author: beardj
 */
#include "Handle.h"

jfieldID getHandleField(JNIEnv *env, jobject obj, const char *fieldName)
{
	jclass cls = env->GetObjectClass(obj);
	return env->GetFieldID(cls, fieldName, "J"); // J is the long type signature
}
