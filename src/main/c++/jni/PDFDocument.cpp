#include <stddef.h>
#include <jni.h>
#include <PDFDoc.h>
#include <Page.h>
#include "Handle.h"
#include "PDFDocument.h"

jobject createPdfPage(JNIEnv *env, jobject jDocument, Page *page) {
	jclass pdfPageClass = env->FindClass("com/foolabs/xpdf/PDFPage");
	jmethodID constructor = env->GetMethodID(pdfPageClass, "<init>", "(Lcom/foolabs/xpdf/PDFDocument;J)V");

	jlong _handle = getHandleFor<Page>(page);

	return env->NewObject(pdfPageClass, constructor, jDocument, _handle);
}

JNIEXPORT jlong JNICALL Java_com_foolabs_xpdf_PDFDocument__1createInstance
  (JNIEnv *env, jclass cls, jstring fileName)
{
	const char *path = env->GetStringUTFChars(fileName, JNI_FALSE);
	GString *gFilePath = new GString(path);
	PDFDoc *doc = new PDFDoc(gFilePath);

	if (doc->isOk())
	{
		return getHandleFor(doc);
	}
	else
	{
		return -1;
	}
}


JNIEXPORT jint JNICALL Java_com_foolabs_xpdf_PDFDocument__1getPageCount
  (JNIEnv *env, jobject obj)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	return doc->getNumPages();
}

JNIEXPORT jboolean JNICALL Java_com_foolabs_xpdf_PDFDocument__1isEncrypted
  (JNIEnv *env, jobject obj)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	return doc->isEncrypted();
}

JNIEXPORT jobject JNICALL Java_com_foolabs_xpdf_PDFDocument__1getPage
  (JNIEnv *env, jobject obj, jint pageNumber)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	Page *page = doc->getCatalog()->getPage(pageNumber);

	return createPdfPage(env, obj, page);
}

JNIEXPORT void JNICALL Java_com_foolabs_xpdf_PDFDocument_finalize
  (JNIEnv *env, jobject obj)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	delete doc;
}
