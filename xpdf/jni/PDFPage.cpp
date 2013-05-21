#include "Page.h"
#include "TextOutputDev.h"
#include "Handle.h"
#include "PDFPage.h"

jobject createPdfPage(JNIEnv *env, Page *page) {
	jclass pdfPageClass = env->FindClass("net/josephbeard/xpdf/PDFPage");
	jmethodID constructor = env->GetMethodID(pdfPageClass, "<init>", "(J)V");

	jlong _handle = getHandleFor<Page>(page);

	return env->NewObject(pdfPageClass, constructor, _handle);
}

JNIEXPORT jint JNICALL Java_net_josephbeard_xpdf_PDFPage__1getNumber(
		JNIEnv *env, jobject obj) {
	Page *page = getHandle<Page>(env, obj);
	return (jint) page->getNum();
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_PDFPage__1getText(JNIEnv *env,
		jobject obj, jobject javaCollector, jboolean fixedPitch,
		jboolean rawOrder) {
	Page *page = getHandle<Page>(env, obj);
	TextCollector *collector = new TextCollector(env, javaCollector);

	GBool gFixedPitch = fixedPitch ? gTrue : gFalse;
	GBool gRawOrder = rawOrder ? gTrue : gFalse;

	TextOutputDev *outputDevice = new TextOutputDev(&TextCollector::CollectText,
			collector, gFalse, gFixedPitch, gRawOrder);

	if (outputDevice->isOk()) {
		const double hDPI = 72;
		const double vDPI = 72;
		const int rotate = 0;
		const GBool useMediaBox = gFalse;
		const GBool crop = gTrue;
		const GBool printing = gFalse;

		page->display(outputDevice, hDPI, vDPI, rotate, useMediaBox, crop,
				printing);
	}

	delete collector; // All text should already be in Java
	delete outputDevice;
}

JNIEXPORT void JNICALL Java_net_josephbeard_xpdf_PDFPage_finalize
  (JNIEnv *env, jobject obj)
{
	Page *page = getHandle<Page>(env, obj);
	delete page;
}
