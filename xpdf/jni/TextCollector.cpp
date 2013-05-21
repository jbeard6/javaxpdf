/*
 * TextCollector.cpp
 *
 *  Created on: May 17, 2013
 *      Author: beardj
 */

#include <stddef.h>
#include "TextCollector.h"

TextCollector::TextCollector(JNIEnv *env, jobject obj) {
	object = new JavaObjectWrapper(env, obj);
}

TextCollector::~TextCollector() {
	delete object;
}

void TextCollector::append(const char* text, int len) {
	object->GetJNIEnv()->NewStringUTF(text);
}

static void TextCollector::CollectText(void *stream, const char *text, int len) {
	((TextCollector *) stream)->append(text, len);
}

