/*
 * TextCollector.h
 *
 *  Created on: May 17, 2013
 *      Author: beardj
 */

#ifndef TEXTCOLLECTOR_H_
#define TEXTCOLLECTOR_H_

#include "JavaObjectWrapper.h"


class TextCollector {
public:
	TextCollector(JNIEnv *env, jobject obj);
	virtual ~TextCollector();

	void append(const char *text, int len);

	static void CollectText(void *stream, const char *text, int len);

private:
	JavaObjectWrapper *object;

};

#endif /* TEXTCOLLECTOR_H_ */
