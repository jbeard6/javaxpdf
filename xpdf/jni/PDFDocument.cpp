#include <PDFDoc.h>
#include "Handle.h"
#include "PDFDocument.h"

JNIEXPORT jlong JNICALL Java_net_josephbeard_xpdf_PDFDocument__1createInstance
  (JNIEnv *env, jclass cls, jstring fileName)
{
	PDFDoc *doc = new PDFDoc(fileName);

	return getHandleFor(doc);
}


JNIEXPORT jint JNICALL Java_net_josephbeard_xpdf_PDFDocument__1getPageCount
  (JNIEnv *env, jobject obj)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	return doc->getNumPages();
}

JNIEXPORT jboolean JNICALL Java_net_josephbeard_xpdf_PDFDocument__1isEncrypted
  (JNIEnv *env, jobject obj)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	return doc->isEncrypted();
}

JNIEXPORT jobject JNICALL Java_net_josephbeard_xpdf_PDFDocument__1getPage
  (JNIEnv *env, jobject obj, jint pageNumber)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	Page *page = doc->getCatalog()->getPage(pageNumber);

	return createPdfPage(env, page);
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_PDFDocument_finalize
  (JNIEnv *env, jobject obj)
{
	PDFDoc *doc = getHandle<PDFDoc>(env, obj);
	delete doc;
}
