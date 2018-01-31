#include <cstdlib>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <iostream>
#include <vector>
#include <list>
#include <unistd.h>    // needed for sleep
#include <sys/time.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>
using namespace std;

// Default frames per second to run animation loop
int FPS = 30;
// Maximum text length
const int BufferSize = 10;
// Text array
const char *levelArray[20] ={"Level 1", "Level 2", "Level 3", "Level 4", "Level 5",
                             "Level 6", "Level 7", "Level 8", "Level 9", "Level 10",
                             "Level 11", "Level 12", "Level 13", "Level 14", "Level 15"
                             "Level 16", "Level 17", "Level 18", "Level 19", "Level 20"};

/*
 * Information to draw on the window.
 */
struct XInfo {
    Display*    display;
    int      screen;
    Window   window;
    GC       gc;
};


/*
 * An abstract class representing displayable things.
 */
class Displayable {
	public:
	    virtual void paint (XInfo &xinfo) = 0;
};

/*
 * A text displayable
 */
class Text : public Displayable {
	public:
	  virtual void paint(XInfo& xinfo) {
	    XDrawImageString( xinfo.display, xinfo.window, xinfo.gc,
	                      this->x, this->y, this->s.c_str(), this->s.length() );
	  }

  	// constructor
  	Text(int x, int y, string s): x(x), y(y), s(s)  {}

	private:
	  int x;
	  int y;
	  string s; // string to show
};

/*
 * A rectangular displayable
 */
class Rectangular : public Displayable {
	public:
	  virtual void paint(XInfo& xinfo) {

	     // create a simple graphics context
	    GC gc = XCreateGC(xinfo.display, xinfo.window, 0, 0);
	    int screen = xinfo.screen;
	    XSetForeground(xinfo.display, gc, BlackPixel(xinfo.display, screen));
	    XSetBackground(xinfo.display, gc, WhitePixel(xinfo.display, screen));
	    XSetFillStyle(xinfo.display,  gc, FillSolid);
	    XSetLineAttributes(xinfo.display, gc,
	                       3,       						// 3 is line width
	                       LineSolid, CapButt, JoinRound);  // other line options

	    XFillRectangle(xinfo.display, xinfo.window, gc, x, y, width, height);
  	}

  	// constructor
  	Rectangular(int x, int y, int width, int height): x(x), y(y), width(width), height(height) {}

	private:
	  int x;
	  int y;
	  int width;
	  int height;
};
