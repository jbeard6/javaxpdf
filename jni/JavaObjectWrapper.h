/*
 * JavaObjectWrapper.h
 *
 *  Created on: May 17, 2013
 *      Author: beardj
 */

#ifndef JAVAOBJECTWRAPPER_H_
#define JAVAOBJECTWRAPPER_H_

class JavaObjectWrapper {

public:
	JavaObjectWrapper(JNIEnv *env, jobject obj);
	virtual ~JavaObjectWrapper();

	JNIEnv *GetJNIEnv() { return env; }
	jobject GetJObject() { return obj; }

	jmethodID GetMethodID(const char *methodName, const char *signature) { return this->env->GetMethodID(cls, methodName, signature); }

	virtual void CallVoidMethod(const char *methodName, const char *signature, ...);
	virtual jint CallIntMethod(const char *methodName, const char *signature, ...);

private:
	JNIEnv *env;
	jobject obj;
	jclass cls;
};

#endif /* JAVAOBJECTWRAPPER_H_ */
