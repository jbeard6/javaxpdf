/*
 * test.cpp
 *
 *  Created on: May 22, 2013
 *      Author: beardj
 */

#include <iostream>
#include <GString.h>
#include <PDFDoc.h>

using namespace std;

int main (int argc, char** argv) {
	for (int i = 1; i < argc; i++) {
		const char *arg = argv[i];
		cout << arg << ": ";
		
		GString *fileName = new GString(arg);

		PDFDoc *doc = new PDFDoc(fileName);
		if (doc->isOk()) {
			cout << "Success!" << endl;
		} else {
			cout << "Failed!" << endl;
		}

		delete doc;
	}
}
