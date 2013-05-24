/*
 * GlobalParameters.cpp
 *
 *  Created on: May 23, 2013
 *      Author: beardj
 */

#include <string.h>
#include <jni.h>
#include <GString.h>
#include <GlobalParams.h>
#include "Handle.h"
#include "GlobalParameters.h"

jboolean GBoolToJBoolean(GBool value)
{
	if (value == gTrue)
	{
		return JNI_TRUE;
	}
	return JNI_FALSE;
}

GBool JBooleanToGBool(jboolean value)
{
	if (value == JNI_TRUE)
	{
		return gTrue;
	}
	return gFalse;
}

JNIEXPORT jlong JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1getInstance
  (JNIEnv *env, jclass cls)
{
	if (globalParams == NULL) {
		// Initialize default parameters
		char *configFileName = strdup("");
		globalParams = new GlobalParams(configFileName);
	}

	return getHandleFor(globalParams);
}

JNIEXPORT jstring JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1getTextEncoding
  (JNIEnv *env, jobject obj)
{
	char *textEncoding = globalParams->getTextEncodingName()->getCString();
	return env->NewStringUTF(textEncoding);
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1setTextEncoding
  (JNIEnv *env, jobject obj, jstring jenc)
{
	const char *textEncName = env->GetStringUTFChars(jenc, JNI_FALSE);
	globalParams->setTextEncoding(strdup(textEncName));
}

JNIEXPORT jstring JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1getTextEOL
  (JNIEnv *env, jobject obj)
{
	EndOfLineKind eol = globalParams->getTextEOL();

	const char *result;
	switch (eol) {
		case eolUnix:
			result = "unix";
			break;

		case eolDOS:
			result = "dos";
			break;

		case eolMac:
			result = "mac";
			break;

		default:
			result = "unknown";
			break;
	}

	return env->NewStringUTF(result);
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1setTextEOL
  (JNIEnv *env, jobject obj, jstring jeol)
{
	const char *eol = env->GetStringUTFChars(jeol, JNI_FALSE);

	if (globalParams->setTextEOL(strdup(eol)) != gTrue)
	{
		jclass illegalArgumentException = env->FindClass("java/lang/IllegalArgumentException");
		env->ThrowNew(illegalArgumentException, "Invalid EOL Style");
	}
}

JNIEXPORT jboolean JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1getTextPageBreaks
  (JNIEnv *env, jobject obj)
{
	return GBoolToJBoolean(globalParams->getTextPageBreaks());
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1setTextPageBreaks
  (JNIEnv *env, jobject obj, jboolean pageBreaks)
{
	globalParams->setTextPageBreaks(JBooleanToGBool(pageBreaks));
}

JNIEXPORT jboolean JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1getErrQuiet
  (JNIEnv *env, jobject obj)
{
	return GBoolToJBoolean(globalParams->getErrQuiet());
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_GlobalParameters__1setErrQuiet
  (JNIEnv *env, jobject obj, jboolean suppress)
{
	globalParams->setErrQuiet(JBooleanToGBool(suppress));
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_GlobalParameters_finalize
  (JNIEnv *env, jobject obj)
{
	delete globalParams;
}
