/*
 * TextCollector.cpp
 *
 *  Created on: May 17, 2013
 *      Author: beardj
 */

#include <stddef.h>
#include <string>
#include "TextCollector.h"

TextCollector::TextCollector(JNIEnv *env, jobject obj) {
	object = new JavaObjectWrapper(env, obj);
}

TextCollector::~TextCollector() {
	delete object;
}

void TextCollector::append(const char* text, int len) {
	const jbyte *fsck = (const jbyte*) text; // What could possibly go wrong?

	jbyteArray data = object->GetJNIEnv()->NewByteArray(len);
	if (data != NULL)
	{
		object->GetJNIEnv()->SetByteArrayRegion(data, 0, len, fsck);
		object->CallVoidMethod("append", "([B)V", data);
	}

	object->GetJNIEnv()->DeleteLocalRef(data);
}

void TextCollector::CollectText(void *stream, char *text, int len) {
	((TextCollector *) stream)->append(text, len);
}

