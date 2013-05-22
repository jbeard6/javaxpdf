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
	GString *fileName = new GString("test.pdf");

	PDFDoc *doc = new PDFDoc(fileName);
	if (doc->isOk()) {
		cout << "Success!" << endl;
	}

	delete doc;
}
