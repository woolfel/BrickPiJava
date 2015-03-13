/*
 * ScannedColumn.h
 *
 *  Created on: Mar 9, 2015
 *      Author: peter
 */

#include <vector>

using namespace std;

class ScannedColumn {
public:
	ScannedColumn();
	virtual ~ScannedColumn();
	void addHeight( int height, int center );
	void setColumnIndex( int index );
	int getColumnIndex( void );
	int getHeight( int index );
	int getCenter( int index );
	int getCount();
	void printString(void);
private:
	int columnIndex;
	int size;
	int *results;
	int *centerPoint;
};
