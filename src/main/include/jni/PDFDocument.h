/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_foolabs_xpdf_PDFDocument */

#ifndef _Included_com_foolabs_xpdf_PDFDocument
#define _Included_com_foolabs_xpdf_PDFDocument
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_foolabs_xpdf_PDFDocument
 * Method:    _createInstance
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_foolabs_xpdf_PDFDocument__1createInstance
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_foolabs_xpdf_PDFDocument
 * Method:    _getPageCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_foolabs_xpdf_PDFDocument__1getPageCount
  (JNIEnv *, jobject);

/*
 * Class:     com_foolabs_xpdf_PDFDocument
 * Method:    _isEncrypted
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_foolabs_xpdf_PDFDocument__1isEncrypted
  (JNIEnv *, jobject);

/*
 * Class:     com_foolabs_xpdf_PDFDocument
 * Method:    _getPage
 * Signature: (I)Lcom/foolabs/xpdf/PDFPage;
 */
JNIEXPORT jobject JNICALL Java_com_foolabs_xpdf_PDFDocument__1getPage
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_foolabs_xpdf_PDFDocument
 * Method:    finalize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_foolabs_xpdf_PDFDocument_finalize
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
