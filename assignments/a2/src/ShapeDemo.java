// import java.io.*;
// import java.util.*;
// import java.util.ArrayList;
// import java.awt.*;
// import java.awt.event.*;
// import java.awt.geom.*;
// import javax.swing.*;
// import javax.swing.event.ChangeEvent;
// import javax.swing.event.ChangeListener;
// import javax.vecmath.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import javax.vecmath.*;

import javax.swing.*;
import java.awt.event.*;

// create the window and run the demo
public class ShapeDemo extends JPanel implements Observer {
	private Model model;

    Shape shape;

    ShapeDemo(Model theModel) {
		this.model = theModel;
		
		
        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
            	shape = new Shape();
                

                // change shape type
                //shape.setIsClosed(true);
                //shape.setIsFilled(true);
                //shape.setColour(model.fillColor);

                // try setting scale to something other than 1 
                shape.setScale(1.0f);

                repaint();
            }

            public void mouseReleased(MouseEvent e) {
       
            }
        });

        this.addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e) {
                shape.addPoint(e.getX(), e.getY());
                repaint();      
            }
        }); 
    }
  
    // custom graphics drawing 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                            RenderingHints.VALUE_ANTIALIAS_ON);

        if (shape != null)
            shape.draw(g2);
    }

    public void update(Object observable) {

	}

    class Shape {

	    // shape points
	    ArrayList<Point2d> points;

	    public void clearPoints() {
	        points = new ArrayList<Point2d>();
	        pointsChanged = true;
	    }
	  
	    // add a point to end of shape
	    public void addPoint(Point2d p) {
	        if (points == null) clearPoints();
	        points.add(p);
	        pointsChanged = true;
	    }    

	    // add a point to end of shape
	    public void addPoint(double x, double y) {
	        if (points == null) clearPoints();
	        addPoint(new Point2d(x, y));  
	    }

	    public int npoints() {
	        return points.size();
	    }

	    // shape is polyline or polygon
	    Boolean isClosed = false; 

	    public Boolean getIsClosed() {
	        return isClosed;
	    }

	    public void setIsClosed(Boolean isClosed) {
	        this.isClosed = isClosed;
	    }    

	    // if polygon is filled or not
	    Boolean isFilled = false; 

	    public Boolean getIsFilled() {
	        return isFilled;
	    }

	    public void setIsFilled(Boolean isFilled) {
	        this.isFilled = isFilled;
	    }    

	    // drawing attributes
	    Color colour = model.strokeColor;//Color.BLACK;
	    float strokeThickness = model.strokeWidth;//3.0f;

	    public Color getColour() {
			return colour;
		}

		public void setColour(Color colour) {
			this.colour = colour;
		}

	    public float getStrokeThickness() {
			return strokeThickness;
		}

		public void setStrokeThickness(float strokeThickness) {
			this.strokeThickness = strokeThickness;
		}

	    // shape's transform

	    float scale = 1.0f;

	    public float getScale(){
	        return scale;
	    }

	    public void setScale(float scale){
	        this.scale = scale;
	    }

	    // some optimization to cache points for drawing
	    Boolean pointsChanged = false; // dirty bit
	    int[] xpoints, ypoints;
	    int npoints = 0;

	    void cachePointsArray() {
	        xpoints = new int[points.size()];
	        ypoints = new int[points.size()];
	        for (int i=0; i < points.size(); i++) {
	            xpoints[i] = (int)points.get(i).x;
	            ypoints[i] = (int)points.get(i).y;
	        }
	        npoints = points.size();
	        pointsChanged = false;
	    }
		
	    int drawShape = model.drawingShape;   // 0 for freeform line, 1 for straight line,
                               				  // 2 for rectangle, 3 for ellipse 

	    // let the shape draw itself
	    // (note this isn't good separation of shape View from shape Model)
	    public void draw(Graphics2D g2) {

	        // don't draw if points are empty (not shape)
	        if (points == null) return;

	        // see if we need to update the cache
	        if (pointsChanged) cachePointsArray();

	        // save the current g2 transform matrix 
	        AffineTransform M = g2.getTransform();

	        // multiply in this shape's transform
	        // (uniform scale)
	        g2.scale(scale, scale);

	        // call drawing functions
	        g2.setColor(colour);
	        g2.setStroke(new BasicStroke(strokeThickness)); 

	        double mouseStartX = points.get(0).x;
	        double mouseStartY = points.get(0).y;
	        double mouseEndX = points.get(npoints-1).x;
	        double mouseEndY = points.get(npoints-1).y;

	        if ( drawShape == 0){ //freeform
	        	g2.drawPolyline(xpoints, ypoints, npoints);
	        } else if (drawShape == 1) { // straight line 
	        	g2.draw(new Line2D.Double(mouseStartX, mouseStartY, mouseEndX, mouseEndY));
	        } else if (drawShape == 2) { // rectangle
	        	g2.setColor(model.fillColor);
	        	g2.fill(new Rectangle2D.Double(mouseStartX, mouseStartY, mouseEndX - mouseStartX, mouseEndY - mouseStartY));
	        	g2.setColor(colour);
	        	g2.draw(new Rectangle2D.Double(mouseStartX, mouseStartY, mouseEndX - mouseStartX, mouseEndY - mouseStartY));
	        } else if (drawShape == 3) { // ellipse
	        	g2.setColor(model.fillColor);
	        	g2.fill(new Ellipse2D.Double(mouseStartX, mouseStartY, mouseEndX - mouseStartX, mouseEndY - mouseStartY));
	        	g2.setColor(colour);
	        	g2.draw(new Ellipse2D.Double(mouseStartX, mouseStartY, mouseEndX - mouseStartX, mouseEndY - mouseStartY));
	        }        

	        // if (isFilled) {
	        //     g2.fillPolygon(xpoints, ypoints, npoints);
	        // } else {
	        //     // can adjust stroke size using scale
	        // 	g2.setStroke(new BasicStroke(strokeThickness / scale)); 
	        // 	if (isClosed)
	        //         g2.drawPolygon(xpoints, ypoints, npoints);
	        //     else
	        //         g2.drawPolyline(xpoints, ypoints, npoints);
	        // }

	        // reset the transform to what it was before we drew the shape
	        g2.setTransform(M);            
	    }
	    
	   
	    // let shape handle its own hit testing
	    // (x,y) is the point to test against
	    // (x,y) needs to be in same coordinate frame as shape, you could add
	    // a panel-to-shape transform as an extra parameter to this function
	    // (note this isn't good separation of shape Controller from shape Model)    
	    public boolean hittest(double x, double y)
	    {   
	    	if (points != null) {

	            // TODO Implement

	    	}
	    	
	    	return false;
	    }
	}
}
