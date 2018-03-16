import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
// import java.awt.geom.Point2D;
// import java.awt.geom.AffineTransform;
import java.util.*;
// import java.util.Observable;
// import java.util.Observer;

public class CanvasView extends JPanel implements Observer {
    DrawingModel model;

    Point2D startMouse;
    Point2D lastMouse;
    Point2D clickPosition;
    
    int lastX, lastY;

    boolean scaling = false;
    boolean scalingX = false;
    boolean moving = false;
    boolean rotating = false;
    boolean drawing = true;
   
    public CanvasView(DrawingModel model) {
        super();
        this.model = model;

        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Click - Select or unselect a shape
                super.mousePressed(e);

                // Get clicke position
                clickPosition = e.getPoint();

                // After one shape is selected, unselect all the other shapes 
                boolean setRestUnselected = false;

                // Go through the shape list and do hit test
                for(ShapeModel shape : model.getShapes()) {
                    if (setRestUnselected){
                        shape.isSelected = false;
                    } else if (shape.hitTest(clickPosition)){
                        shape.isSelected = true;
                        setRestUnselected = true;
                    } else {
                        shape.isSelected = false;
                    }
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Press+drag: outside any shape - drawing
                //             right bottom corner - scaling
                //             inside a shape - translation
                //             top round handle - rotation
                super.mousePressed(e);
                
                startMouse = e.getPoint();
                lastMouse = e.getPoint();

                lastX = e.getX();
                lastY = e.getY();


                for(ShapeModel shape : model.getShapes()) {
                    if (shape.onBottomCorner(startMouse.getX(), startMouse.getY()) && shape.isSelected){
                        // Scale handle selected
                        shape.scaleSelected = true;
                    } 
                    else if (shape.onRotateHandle(startMouse.getX(), startMouse.getY()) && shape.isSelected){
                        // Rotate handle selected
                        shape.rotateSelected = true;
                    }
                    else if (shape.onScaliingHandleX(startMouse.getX(), startMouse.getY()) && shape.isSelected){
                        // X-axis scale handle selected
                        shape.scaleXSelected = true;
                    }
                    else if (shape.hitTest(startMouse) && shape.isSelected) {
                        // To translate - shape must already selected and press inside the shape
                        shape.translateSelected = true;
                    }  
                    else {
                        shape.isSelected = false;
                    }
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // update transformation info
                super.mouseDragged(e);

                lastMouse = e.getPoint();

                drawing = true;

                for(ShapeModel shape : model.getShapes()) {
                    if (shape.scaleSelected) {
                        scaling = true;
                        drawing = false;

                        model.scale(shape, (Point)lastMouse);
                        break;
                    } 
                    else if (shape.translateSelected){
                        moving = true;
                        drawing = false;

                        model.translate(shape, e.getX() - lastX, e.getY() - lastY);

                        lastX = e.getX();
                        lastY = e.getY();

                        break;
                    }
                    else if (shape.rotateSelected){
                        rotating = true;
                        drawing = false;

                        model.rotate(shape, (Point)lastMouse);
                        break;
                    } else if (shape.scaleXSelected){
                        scalingX = true;
                        drawing = false;

                        model.scaleX(shape, (Point)lastMouse);
                    }
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // store transformation info for printing, undoing/redoing
                super.mouseReleased(e);
         
                if (drawing){
                    // Draw a new shape
                    ShapeModel shape = new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse);
                    
                    model.addShape(shape);
                    model.addShapeField(shape, (Point) startMouse, (Point) lastMouse);
                    
                }

                for(ShapeModel shape : model.getShapes()) {
                    if (shape.scaleSelected){
                        model.endScale(shape);
                    } else if (shape.translateSelected){
                        model.endTranslate(shape);
                    } else if (shape.rotateSelected){
                        model.endRotate(shape);
                    } else if (shape.scaleXSelected){
                        model.endScaleX(shape);
                    }
                    shape.scaleSelected = false;
                    shape.scaleXSelected = false;
                    shape.translateSelected = false;
                    shape.rotateSelected = false;
                }

                startMouse = null;
                lastMouse = null;

                scaling = false;
                scalingX = false;
                moving = false;
                rotating = false;
                drawing = false;
            }
        };

        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);

        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        setBackground(Color.WHITE);

        drawAllShapes(g2);
        drawCurrentShape(g2);
    }

    private void drawAllShapes(Graphics2D g2) {
        g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for(ShapeModel shape : model.getShapes()) {
            g2.draw(shape.getShape());
            if (shape.isSelected){
                // Draw scale handles
                AffineTransform save = g2.getTransform();
                
                g2.translate(shape.staticCenterPoint.getX(), shape.staticCenterPoint.getY());
                g2.translate(shape.absX, shape.absY);
                g2.rotate(shape.radians);
                g2.scale(shape.absScaleOnX, 1);
                g2.scale(shape.absScaleX, shape.absScaleY);
                g2.translate(-shape.staticCenterPoint.getX(), -shape.staticCenterPoint.getY());

                // Draw the scale handle
                double x = shape.boundingX + shape.boundingWidth;
                double y = shape.boundingY + shape.boundingHeight;

                g2.fill(new Rectangle2D.Double(x - shape.getHandleSizeX(), y - shape.getHandleSizeY(),
                               2*shape.getHandleSizeX(),2*shape.getHandleSizeY()));
                
                // Draw the X-axis scale handle
                x = shape.boundingX + shape.boundingWidth;
                y = shape.boundingY + shape.boundingHeight/2;

                g2.fill(new Rectangle2D.Double(x - shape.getHandleSizeX(), y - shape.getHandleSizeY(),
                               2*shape.getHandleSizeX(),2*shape.getHandleSizeY()));

                // Draw the rotation handle
                x = shape.boundingX + (shape.boundingWidth/2);
                y = shape.boundingY - 10;
                g2.fill(new Ellipse2D.Double(x - shape.getHandleSizeX(), y - shape.getHandleSizeY(),
                               2*shape.getHandleSizeX(),2*shape.getHandleSizeY()));

                g2.setTransform(save);
            }

        }
    }

    private void drawCurrentShape(Graphics2D g2) {
        if (startMouse == null || scaling || moving || rotating || scalingX) {
            return;
        }

        g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.draw(new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse).getShape());
    }
}
