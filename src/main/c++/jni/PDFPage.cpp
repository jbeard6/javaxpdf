#include <iostream>
#include <jni.h>
#include "Page.h"
#include "Catalog.h"
#include "TextOutputDev.h"
#include "Handle.h"
#include "TextCollector.h"
#include "PDFPage.h"

JNIEXPORT jint JNICALL Java_com_foolabs_xpdf_PDFPage__1getNumber(
		JNIEnv *env, jobject obj) {
	Page *page = getHandle<Page>(env, obj);
	return (jint) page->getNum();
}

JNIEXPORT void JNICALL Java_com_foolabs_xpdf_PDFPage__1getText
	  (JNIEnv *env, jobject obj, jobject javaCollector, jboolean physicalLayout, jdouble fixedPitch, jboolean rawOrder) {
	Page *page = getHandle<Page>(env, obj);
	TextCollector *collector = new TextCollector(env, javaCollector);

	GBool gPhysicalLayout = physicalLayout ? gTrue : gFalse;
	GBool gRawOrder = rawOrder ? gTrue : gFalse;

	TextOutputDev *outputDevice = new TextOutputDev(&TextCollector::CollectText,
			collector, gPhysicalLayout, fixedPitch, gRawOrder);

	if (outputDevice->isOk()) {
		const double hDPI = 72;
		const double vDPI = 72;
		const int rotate = 0;
		const GBool useMediaBox = gFalse;
		const GBool crop = gTrue;
		const GBool printing = gFalse;
		Catalog *catalog = NULL;

		page->display(outputDevice, hDPI, vDPI, rotate, useMediaBox, crop,
				printing, catalog);
	}

	delete collector; // All text should already be in Java
	delete outputDevice;
}

JNIEXPORT void JNICALL Java_com_foolabs_xpdf_PDFPage_finalize
  (JNIEnv *env, jobject obj)
{
	Page *page = getHandle<Page>(env, obj);
	std::cerr << "Ignoring request to delete page!" << std::endl;
	//delete page;
}
