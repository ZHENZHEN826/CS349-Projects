#include <cstdlib>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <iostream>
#include <vector>
#include <list>
#include <unistd.h> // needed for sleep
#include <sys/time.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>

using namespace std;

// frames per second to run animation loop
int FPS = 30;

const int BufferSize = 10;

const char *levelArray[5] ={"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};

// get microseconds
unsigned long now() {
    timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec * 1000000 + tv.tv_usec;
}

/*
 * Function to put out a message on error exits.
 */
void error( string str ) {
    cerr << str << endl;
    exit(0);
}

bool isPositiveNumber(char number[]) {
    // Negative numbers
    if (number[0] == '-')
        return false;
    for (int i = 0; number[i] != 0; i++) {
        if (!isdigit(number[i]))
            return false;
    }
    return true;
}

/*
 * Information to draw on the window.
 */
struct XInfo {
    Display*    display;
    int      screen;
    Window   window;
    GC       gc;
};

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

class Rectangular : public Displayable {
public:
  virtual void paint(XInfo& xinfo) {

     // create a simple graphics context
    GC gc = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    int screen = xinfo.screen; //bug?
    XSetForeground(xinfo.display, gc, BlackPixel(xinfo.display, screen));
    XSetBackground(xinfo.display, gc, WhitePixel(xinfo.display, screen));
    XSetFillStyle(xinfo.display,  gc, FillSolid);
    XSetLineAttributes(xinfo.display, gc,
                       3,       // 3 is line width
                       LineSolid, CapButt, JoinRound);         // other line options

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


void initX(int argc, char* argv[], XInfo& xinfo) {
    XSizeHints hints;

    hints.x = 100;
    hints.y = 100;
    hints.width = 850;
    hints.height = 250;
    hints.flags = PPosition | PSize;

    /*
    * Display opening uses the DISPLAY  environment variable.
    * It can go wrong if DISPLAY isn't set, or you don't have permission.
    */
    xinfo.display = XOpenDisplay( "" );
    if ( !xinfo.display )   {
        error( "Can't open display." );
    }

    /*
    * Find out some things about the display you're using.
    */
    // DefaultScreen is as macro to get default screen index
    xinfo.screen = DefaultScreen( xinfo.display ); 

    unsigned long white, black;
    white = XWhitePixel( xinfo.display, xinfo.screen ); 
    black = XBlackPixel( xinfo.display, xinfo.screen );

    xinfo.window = XCreateSimpleWindow(
        xinfo.display,              // display where window appears
        DefaultRootWindow( xinfo.display ), // window's parent in window tree
        hints.x, hints.y,           // upper left corner location
        hints.width, hints.height,  // size of the window
        10,                         // width of window's border
        black,                      // window border colour
        white );                        // window background colour

    // extra window properties like a window title
    XSetStandardProperties(
        xinfo.display,      // display containing the window
        xinfo.window,       // window whose properties are set
        "Frog",             // window's title
        "Frog",               // icon's title
        None,               // pixmap for the icon
        argv, argc,         // applications command line args
        &hints);            // size hints for the window

    xinfo.gc = XCreateGC(xinfo.display, xinfo.window, 0, 0);   // create a graphics context

    // drawing demo with graphics context here ...
    XSetBackground(xinfo.display, xinfo.gc, white);
    XSetForeground(xinfo.display, xinfo.gc, black);
    
    // load a larger font
    XFontStruct * font;
    font = XLoadQueryFont (xinfo.display, "10x20");
    XSetFont (xinfo.display, xinfo.gc, font->fid);

    XSelectInput(xinfo.display, xinfo.window, KeyPressMask); // select events

    /*
     * Put the window on the screen.
     */
    XMapRaised( xinfo.display, xinfo.window );

    XFlush(xinfo.display);

    sleep(1);  // give server time to setup
}

/*
 * Function to repaint a display list
 */
void repaint( list<Displayable*> dList, XInfo& xinfo) {
  list<Displayable*>::const_iterator begin = dList.begin();
  list<Displayable*>::const_iterator end = dList.end();

  XClearWindow(xinfo.display, xinfo.window);
  while ( begin != end ) {
    Displayable* d = *begin;
    d->paint(xinfo);
    begin++;
  }
  XFlush(xinfo.display);
}

// The loop responding to events from the user.
void eventloop(XInfo& xinfo) {
    XEvent event;
    KeySym key;
    char text[BufferSize];
    // list of Displayables
    list<Displayable*> dList;

    // level info
    Text* txt = NULL;
    int level = 0;
    //txt = new Text(725, 25, levelArray[level]);
    //txt->paint(xinfo);

    // Three moving block + frog
    XPoint blockPos1;
    blockPos1.x = 0;
    blockPos1.y = 50;
    int blockWidth1 = 50;
    XPoint blockDir1;
    blockDir1.x = 1;
    blockDir1.y = 0;
    int Interval3 = 850/3;
    
    
    XPoint blockPos2;
    blockPos2.x = 0;
    blockPos2.y = 100;
    int blockWidth2 = 20;
    int blockHeight2 = 50;
    XPoint blockDir2;
    blockDir2.x = -1;
    blockDir2.y = 0;
    int Interval4 = 850/4;
    

    XPoint blockPos3;
    blockPos3.x = 0;
    blockPos3.y = 150;
    int blockWidth3 = 100;
    int blockHeight3 = 50;
    XPoint blockDir3;
    blockDir3.x = 1;
    blockDir3.y = 0;
    int Interval2 = 850/2;
    

    // Frog postition, size, and velocity
    XPoint frogPos;
    frogPos.x = 400;
    frogPos.y = 200;
    int frogSize = 50;
    XPoint frogDir;
    frogDir.x = 50;
    frogDir.y = 50;


    XFlush(xinfo.display);

    //Rectangular* rec = NULL;
    //rec = new Rectangular(frogPos.x, frogPos.y, frogSize, frogSize);
    //rec->paint(xinfo);

    // time of last window paint
    unsigned long lastRepaint = 0;

    XWindowAttributes w;
    XGetWindowAttributes(xinfo.display, xinfo.window, &w);

    while ( true ) { // event loop until 'exit'
        if (XPending(xinfo.display) > 0) { 
            XNextEvent( xinfo.display, &event ); // wait for next event

            switch ( event.type ) {
            case KeyPress: // any keypress
                int i = XLookupString( 
                    (XKeyEvent*)&event, text, BufferSize, &key, 0 );
                if ( i == 1 && text[0] == 'q' ) {
                    XCloseDisplay(xinfo.display);
                    return;
                }
                if ( i == 1 && text[0] == 'n' && (frogPos.y == 0)) {
                    XClearWindow(xinfo.display, xinfo.window);
                    level += 1;
                    blockDir1.x = level+1;
                    blockDir2.x = -(level+1);
                    blockDir3.x = level+1;
                    // update frog
                    frogPos.x =400;
                    frogPos.y = 200;
                    //rec = new Rectangular(frogPos.x, frogPos.y, frogSize, frogSize);
                    //rec->paint(xinfo);
                    // update text
                    //txt = new Text(725, 25, levelArray[level]);
                    //txt->paint(xinfo);
                    repaint(dList, xinfo);
                    break;
                }
                switch(key){
                    case XK_Up:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog position
                        if (frogPos.y - frogSize/2 > 0)
                            frogPos.y -= frogDir.y;
                        
                        repaint(dList, xinfo);
                        break;
                    case XK_Down:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog position
                        if ((frogPos.y + frogSize < w.height) && (frogPos.y > 0))
                            frogPos.y += frogDir.y;

                        repaint(dList, xinfo);
                        break;
                    case XK_Left:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog position
                        if (frogPos.x - frogSize/2 > 0)
                            frogPos.x -= frogDir.x;
                        
                        repaint(dList, xinfo);
                        break;
                    case XK_Right:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog position
                        if (frogPos.x + frogSize < w.width)
                            frogPos.x += frogDir.x;
                        
                        repaint(dList, xinfo);
                        break;
                    }

                break;
            }
        }

        unsigned long end = now();  // get time in microsecond

        if (end - lastRepaint > 1000000 / FPS) { 

            // clear background
            XClearWindow(xinfo.display, xinfo.window);

            dList.clear();
            // Level info
            dList.push_back(new Text(725, 25, levelArray[level]));
            // frog
            dList.push_back(new Rectangular(frogPos.x, frogPos.y, frogSize, frogSize));
            // blocks in second row
            dList.push_back(new Rectangular(blockPos1.x, blockPos1.y, blockWidth1, blockWidth1));
            dList.push_back(new Rectangular(blockPos1.x + Interval3, blockPos1.y, blockWidth1, blockWidth1));
            dList.push_back(new Rectangular(blockPos1.x + 2*Interval3, blockPos1.y, blockWidth1, blockWidth1));
            // blocks in third row
            dList.push_back(new Rectangular(blockPos2.x, blockPos2.y, blockWidth2, blockHeight2));
            dList.push_back(new Rectangular(blockPos2.x + Interval4, blockPos2.y, blockWidth2, blockHeight2));
            dList.push_back(new Rectangular(blockPos2.x + 2*Interval4, blockPos2.y, blockWidth2, blockHeight2));
            dList.push_back(new Rectangular(blockPos2.x + 3*Interval4, blockPos2.y, blockWidth2, blockHeight2));
            // blocks in forth row
            dList.push_back(new Rectangular(blockPos3.x, blockPos3.y, blockWidth3, blockHeight3));
            dList.push_back(new Rectangular(blockPos3.x + Interval2, blockPos3.y, blockWidth3, blockHeight3));
            
            repaint(dList, xinfo);
            blockPos1.x += blockDir1.x;
            blockPos2.x += blockDir2.x;
            blockPos3.x += blockDir3.x;

/*
            // bounce ball
            if (ballPos.x + ballSize/2 > w.width ||
                ballPos.x - ballSize/2 < 0)
                ballDir.x = -ballDir.x;
            if (ballPos.y + ballSize/2 > w.height ||
                ballPos.y - ballSize/2 < 0)
                ballDir.y = -ballDir.y;
*/
            XFlush( xinfo.display );

            lastRepaint = now(); // remember when the paint happened
        }

        // IMPORTANT: sleep for a bit to let other processes work
        if (XPending(xinfo.display) == 0) {
            usleep(1000000 / FPS - (end - lastRepaint));
        }

    } //while
    XCloseDisplay(xinfo.display);
}

int main( int argc, char* argv[] ) {
    if (argc == 2 ){
        if (isPositiveNumber(argv[1])){
            FPS = atoi(argv[1]);
        } else {
            error("Please enter a positive FPS.");
        }
    }
    
    XInfo xinfo;
    initX(argc, argv, xinfo);
    eventloop(xinfo);
}