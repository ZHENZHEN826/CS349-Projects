/*
 * a1-enhanced: Set different colours for each line of the blocks
 * Added codes are in rectangular struct and rectangular repaint
 */
#include "a1-enhanced.h"

/*
 * Get microseconds
 */
unsigned long now() {
    timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec * 1000000 + tv.tv_usec;
}


/*
 * Put out a message on error exits.
 */
void error( string str ) {
    cerr << str << endl;
    exit(0);
}


/*
 * Check if command line input is a positive number.
 */
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
 * Create screen, display and window
 */
void initX(int argc, char* argv[], XInfo& xinfo) {
    XSizeHints hints;
    // define window sizes
    hints.x = 100;
    hints.y = 100;
    hints.width = 850;
    hints.height = 250;
    hints.flags = PPosition | PSize;
    
    // Display opening uses the DISPLAY  environment variable.
    // It can go wrong if DISPLAY isn't set, or you don't have permission.
    xinfo.display = XOpenDisplay( "" );
    if ( !xinfo.display )   {
        error( "Can't open display." );
    }

    
    //Find out some things about the display you're using.
    // DefaultScreen is as macro to get default screen index.
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
        white );                    // window background colour

    // Extra window properties like a window title.
    XSetStandardProperties(
        xinfo.display,      // display containing the window
        xinfo.window,       // window whose properties are set
        "Frog",             // window's title
        "Frog",             // icon's title
        None,               // pixmap for the icon
        argv, argc,         // applications command line args
        &hints);            // size hints for the window

    // Create a graphics context.
    xinfo.gc = XCreateGC(xinfo.display, xinfo.window, 0, 0);   

    // Drawing demo with graphics context here.
    XSetBackground(xinfo.display, xinfo.gc, white);
    XSetForeground(xinfo.display, xinfo.gc, black);
    
    // Load a larger font.
    XFontStruct * font;
    font = XLoadQueryFont (xinfo.display, "10x20");
    XSetFont (xinfo.display, xinfo.gc, font->fid);
    // Select events.
    XSelectInput(xinfo.display, xinfo.window, KeyPressMask); 
    
    // Put the window on the screen.
    XMapRaised( xinfo.display, xinfo.window );

    XFlush(xinfo.display);

    // Give server time to setup.
    sleep(1);  
}


/*
 * Function to repaint a display list.
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

/*
 * The loop responding to events from the user.
 */
void eventloop(XInfo& xinfo) {
    XEvent event;
    KeySym key;
    char text[BufferSize];
    // List of Displayables
    list<Displayable*> dList;

    // Level info
    Text* txt = NULL;
    int level = 0;

    // Initialize three types of moving blocks, and the frog
    // blocks on the second line:
    XPoint blockPos1;
    blockPos1.x = 0; blockPos1.y = 50;          // initial position
    
    XPoint blockDir1;
    blockDir1.x = 1; blockDir1.y = 0;           // moving direction
    int blockWidth1 = 50;                       // block width
    int Interval3 = 850/3;                      // space between two blocks
    
    // blocks on the third line:
    XPoint blockPos2;
    blockPos2.x = 850/4*3; blockPos2.y = 100;   // initial position
    
    XPoint blockDir2;
    blockDir2.x = -1; blockDir2.y = 0;          // moving direction 
    int blockWidth2 = 20;                       // block width
    int blockHeight2 = 50;                      // block height
    int Interval4 = 850/4;                      // space between two blocks
    
    // blocks on the forth line:
    XPoint blockPos3;
    blockPos3.x = 0; blockPos3.y = 150;
    
    XPoint blockDir3;
    blockDir3.x = 1; blockDir3.y = 0;
    int blockWidth3 = 100;
    int blockHeight3 = 50;
    int Interval2 = 850/2;
    

    // Frog postition, size, and velocity
    XPoint frogPos;
    frogPos.x = 400; frogPos.y = 200;           // Initially, in the middle of last line
    
    XPoint frogDir;
    frogDir.x = 50; frogDir.y = 50;
    int frogSize = 50;                          // Frog is a 50*50 square


    XFlush(xinfo.display);

    // time of last window paint
    unsigned long lastRepaint = 0;
    // window infomation
    XWindowAttributes w;
    XGetWindowAttributes(xinfo.display, xinfo.window, &w);

    // event loop until 'exit'
    while ( true ) { 
        // any event pending?
        if (XPending(xinfo.display) > 0) { 
            // wait for next event
            XNextEvent( xinfo.display, &event ); 

            switch ( event.type ) {
            // any key press
            case KeyPress: 
                int i = XLookupString( 
                    (XKeyEvent*)&event, text, BufferSize, &key, 0 );
                // press q to exit
                if ( i == 1 && text[0] == 'q' ) {
                    XCloseDisplay(xinfo.display);
                    return;
                }
                // press n to go to next level while the frog is in goal area
                if ( i == 1 && text[0] == 'n' && (frogPos.y == 0)) {
                    XClearWindow(xinfo.display, xinfo.window);
                    // increase the level, blocks move faster
                    level += 1;
                    blockDir1.x = level+1;
                    blockDir2.x = -(level+1);
                    blockDir3.x = level+1;
                    // update the frog's position
                    frogPos.x =400;
                    frogPos.y = 200;
                   
                    repaint(dList, xinfo);
                    break;
                }
                switch(key){
                    // arrow key UP
                    case XK_Up:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog's position
                        // do nothing if already at top layer
                        if (frogPos.y - frogSize/2 > 0)
                            frogPos.y -= frogDir.y;
                        
                        repaint(dList, xinfo);
                        break;
                    // arrow key DOWN
                    case XK_Down:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog position
                        // do nothing if already at bottom layer
                        if ((frogPos.y + frogSize < w.height) && (frogPos.y > 0))
                            frogPos.y += frogDir.y;

                        repaint(dList, xinfo);
                        break;
                    // arrow key LEFT
                    case XK_Left:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog's position
                        // do nothing if there is a wall on the left
                        if (frogPos.x - frogSize/2 > 0)
                            frogPos.x -= frogDir.x;
                        
                        repaint(dList, xinfo);
                        break;
                    // arrow key RIGHT
                    case XK_Right:
                        XClearWindow(xinfo.display, xinfo.window);
                        // update frog's position
                        // do nothing if there is a wall on the right
                        if (frogPos.x + frogSize < w.width)
                            frogPos.x += frogDir.x;
                        
                        repaint(dList, xinfo);
                        break;
                    }
                break;
            }
        }

        // get current time in microsecond
        unsigned long end = now();  
        // if it's time to repaint animation, 1000000 = 1s
        if (end - lastRepaint > 1000000 / FPS) { 

            // clear background
            XClearWindow(xinfo.display, xinfo.window);

            dList.clear();
            // Level info
            dList.push_back(new Text(725, 25, levelArray[level]));
            // frog
            dList.push_back(new Rectangular(frogPos.x, frogPos.y, frogSize, frogSize, BLACK));
            // blocks in second row
            dList.push_back(new Rectangular(blockPos1.x, blockPos1.y, blockWidth1, blockWidth1, RED));
            dList.push_back(new Rectangular(blockPos1.x + Interval3, blockPos1.y, blockWidth1, blockWidth1, RED));
            dList.push_back(new Rectangular(blockPos1.x + 2*Interval3, blockPos1.y, blockWidth1, blockWidth1, RED));
            dList.push_back(new Rectangular(blockPos1.x + 3*Interval3, blockPos1.y, blockWidth1, blockWidth1, RED));
            // blocks in third row
            dList.push_back(new Rectangular(blockPos2.x, blockPos2.y, blockWidth2, blockHeight2, GREEN));
            dList.push_back(new Rectangular(blockPos2.x - Interval4, blockPos2.y, blockWidth2, blockHeight2, GREEN));
            dList.push_back(new Rectangular(blockPos2.x - 2*Interval4, blockPos2.y, blockWidth2, blockHeight2, GREEN));
            dList.push_back(new Rectangular(blockPos2.x - 3*Interval4, blockPos2.y, blockWidth2, blockHeight2, GREEN));
            dList.push_back(new Rectangular(blockPos2.x - 4*Interval4, blockPos2.y, blockWidth2, blockHeight2, GREEN));
            // blocks in forth row
            dList.push_back(new Rectangular(blockPos3.x, blockPos3.y, blockWidth3, blockHeight3, BLUE));
            dList.push_back(new Rectangular(blockPos3.x + Interval2, blockPos3.y, blockWidth3, blockHeight3, BLUE));
            dList.push_back(new Rectangular(blockPos3.x + 2*Interval2, blockPos3.y, blockWidth3, blockHeight3, BLUE));
            
            repaint(dList, xinfo);

            // Q16. Check collision:
            // Collide on second layer, check frog' top left and top right positions with the 4 moving blocks' positions
            bool collideOnSecondLayer = ((frogPos.y == 50) &&
                //check frog's top left position with first block 
                (((frogPos.x >= blockPos1.x) && (frogPos.x <= blockPos1.x + blockWidth1)) ||
                 //check frog's top right position with first block                        
                 ((frogPos.x + frogSize >= blockPos1.x) && (frogPos.x + frogSize <= blockPos1.x + blockWidth1)) ||   
                 //check frog's top left position with second block
                 ((frogPos.x >= blockPos1.x + Interval3) && (frogPos.x <= blockPos1.x + Interval3 + blockWidth1)) || 
                 //check frog's top right position with second block... 
                 ((frogPos.x + frogSize >= blockPos1.x + Interval3) && (frogPos.x + frogSize <= blockPos1.x + Interval3 + blockWidth1)) ||
                 ((frogPos.x >= blockPos1.x + 2*Interval3) && (frogPos.x <= blockPos1.x + 2*Interval3 + blockWidth1)) ||
                 ((frogPos.x + frogSize >= blockPos1.x + 2*Interval3) && (frogPos.x + frogSize <= blockPos1.x + 2*Interval3 + blockWidth1)) ||
                 ((frogPos.x >= blockPos1.x + 3*Interval3) && (frogPos.x <= blockPos1.x + 3*Interval3 + blockWidth1)) ||
                 ((frogPos.x + frogSize >= blockPos1.x + 3*Interval3) && (frogPos.x + frogSize <= blockPos1.x + 3*Interval3 + blockWidth1))));
            // Collide on third layer
            bool collideOnThirdLayer = ((frogPos.y == 100) &&
                (((blockPos2.x >= frogPos.x) && (blockPos2.x <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x + blockWidth2 >= frogPos.x) && (blockPos2.x + blockWidth2 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - Interval4 >= frogPos.x) && (blockPos2.x - Interval4 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - Interval4 + blockWidth2 >= frogPos.x) && (blockPos2.x - Interval4 + blockWidth2 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - 2*Interval4 >= frogPos.x) && (blockPos2.x - 2*Interval4 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - 2*Interval4 + blockWidth2 >= frogPos.x) && (blockPos2.x - 2*Interval4 + blockWidth2 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - 3*Interval4 >= frogPos.x) && (blockPos2.x - 3*Interval4 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - 3*Interval4 + blockWidth2 >= frogPos.x) && (blockPos2.x - 3*Interval4 + blockWidth2 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - 4*Interval4 >= frogPos.x) && (blockPos2.x - 4*Interval4 <=  frogPos.x + frogSize)) ||
                 ((blockPos2.x - 4*Interval4 + blockWidth2 >= frogPos.x) && (blockPos2.x - 4*Interval4 + blockWidth2 <=  frogPos.x + frogSize))));
            // Collide on forth layer
            bool collideOnForthLayer = ((frogPos.y == 150) && 
                (((frogPos.x >= blockPos3.x) && (frogPos.x <= blockPos3.x + blockWidth3)) ||
                 ((frogPos.x + frogSize >= blockPos3.x) && (frogPos.x + frogSize <= blockPos3.x + blockWidth3)) ||
                 ((frogPos.x >= blockPos3.x + Interval2) && (frogPos.x <= blockPos3.x + Interval2 + blockWidth3)) ||
                 ((frogPos.x + frogSize >= blockPos3.x + Interval2) && (frogPos.x + frogSize <= blockPos3.x + Interval2 + blockWidth3)) ||
                 ((frogPos.x >= blockPos3.x + 2*Interval2) && (frogPos.x <= blockPos3.x + 2*Interval2 + blockWidth3)) ||
                 ((frogPos.x + frogSize >= blockPos3.x + 2*Interval2) && (frogPos.x + frogSize <= blockPos3.x + 2*Interval2 + blockWidth3))));

            // If collision happens
            if (collideOnSecondLayer || collideOnThirdLayer || collideOnForthLayer){
                // reset the level to 1 
                level = 0;
                // move the frog back to the initial position
                frogPos.x = 400; frogPos.y = 200;
                // rest blocks moving speed
                blockDir1.x = level+1;
                blockDir2.x = -(level+1);
                blockDir3.x = level+1;
            }

            // Q15. At the moment when a block reaches one side of the window, a new block comes out from the other side of the window.
            if (blockPos1.x + 2*Interval3 > 800){
                blockPos1.x = -50;
            }
            if (blockPos2.x - 3*Interval4 < 0){
                blockPos2.x = 850;
            }
            if (blockPos3.x + Interval2 > 750){
                blockPos3.x = -100;
            }

            // uodate blocks positions
            blockPos1.x += blockDir1.x;
            blockPos2.x += blockDir2.x;
            blockPos3.x += blockDir3.x;

            XFlush( xinfo.display );

            // remember when the paint happened
            lastRepaint = now(); 
        }

        // IMPORTANT: sleep for a bit to let other processes work
        if (XPending(xinfo.display) == 0) {
            usleep(1000000 / FPS - (end - lastRepaint));
        }

    } //while
    XCloseDisplay(xinfo.display);
}

int main( int argc, char* argv[] ) {
    // FPS can be changed on command line
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