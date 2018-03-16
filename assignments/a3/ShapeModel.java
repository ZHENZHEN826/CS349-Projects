import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Constructor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.undo.*;

public class ShapeModel {
    Shape shape;
    ShapeType myShapeType;

    Point startPoint; // Not update when rotate
    Point endPoint;
    Point centerPoint = null;

    boolean isSelected = true;
    boolean scaleSelected = false;
    boolean translateSelected = false;
    boolean rotateSelected = false;

    // Translation variables
    int absX = 0; // absolute position of the shape
    int absY = 0;
    
    int pX = 0; // position before dragged
    int pY = 0;
    
    // Scaling variables
    double absScaleX = 1.0;
    double absScaleY = 1.0;

    double pScale = 1.0;

    // Rotation variables
    double radians;

    double pRadians;

    // Bounding box when shape is created
    Rectangle boundingBox;
    int boundingHeight, boundingWidth, boundingX, boundingY;

    // Rotation and scaling handles' size
    int handleSize = 5;

    // Point absStartPoint;
    // Point absEndPoint;

    Point staticCenterPoint;
    Point staticStartPoint;
    Point staticEndPoint;

    public ShapeModel(Point startPoint, Point endPoint) {}

    public void setBoundingBox(){
        // AffineTransform t = new AffineTransform();
        // t.translate(absX, absY);
        // Shape newShape = t.createTransformedShape(shape);

        // boundingBox = newShape.getBounds();
        boundingBox = shape.getBounds();

        boundingHeight = boundingBox.height;
        boundingWidth = boundingBox.width;
        boundingX = boundingBox.x;
        boundingY = boundingBox.y;
    }

    public int getHandleSizeX(){
        return (int)(handleSize/absScaleX);
    }
    public int getHandleSizeY(){
        return (int)(handleSize/absScaleY);
    }

    /** Convert from the component's X coordinate to the model's X coordinate. */
    // protected double fromX(int x) {
    //     return (x - this.getInsets().left) / this.scale;
    // }

    /** Convert from the component's Y coordinate to the model's Y coordinate. */
    // protected double fromY(int y) {
    //     return (this.getHeight() - 1 - this.getInsets().bottom - y)
    //             / this.scale;
    // }

    public Point2D transformOnePoint (double x, double y){
        // hit testing
        Point2D M = new Point2D.Double(x, y);
        Point2D MT = new Point2D.Double();
        // create transformation matrix for this shape
        AffineTransform AT1;
        AT1 = new AffineTransform();

        
        AT1.translate(staticCenterPoint.getX(), staticCenterPoint.getY());
        AT1.translate(absX, absY);
        AT1.rotate(radians);
        AT1.translate(-staticCenterPoint.getX(), -staticCenterPoint.getY());

        AT1.translate(staticStartPoint.getX(), staticStartPoint.getY());
        AT1.scale(absScaleX, absScaleY);
        AT1.translate(-staticStartPoint.getX(), -staticStartPoint.getY());

        // AT1.scale(absScaleX, absScaleY);
        // create an inverse matrix of AT1
        try{
            // create an inverse matrix of AT1
            AffineTransform IAT1 = AT1.createInverse();
            // apply the inverse transformation to the handle position
            IAT1.transform(M, MT);
            // Hit test with transformed handle position
            
        } catch (NoninvertibleTransformException e){
            // error
        }     
        // double calX = MT.x;
        // double calY = MT.y;
        return MT;
    }

    public Point transformOnePoint2 (int x, int y){
        // hit testing
        Point M = new Point(x, y);
        Point MT = new Point();
        // create transformation matrix for this shape
        AffineTransform AT1;
        AT1 = new AffineTransform();

        
        AT1.translate(staticCenterPoint.getX(), staticCenterPoint.getY());
        AT1.translate(absX, absY);
        AT1.rotate(radians);
        AT1.translate(-staticCenterPoint.getX(), -staticCenterPoint.getY());

        AT1.translate(staticStartPoint.getX(), staticStartPoint.getY());
        AT1.scale(absScaleX, absScaleY);
        AT1.translate(-staticStartPoint.getX(), -staticStartPoint.getY());

        // AT1.scale(absScaleX, absScaleY);
        // create an inverse matrix of AT1
        try{
            // create an inverse matrix of AT1
            AffineTransform IAT1 = AT1.createInverse();
            // apply the inverse transformation to the handle position
            IAT1.transform(M, MT);
            // Hit test with transformed handle position
            
        } catch (NoninvertibleTransformException e){
            // error
        }     
        // double calX = MT.x;
        // double calY = MT.y;
        return MT;
    }

    public boolean onBottomCorner(double x, double y) {
        Point2D MT = transformOnePoint (x, y);
        double calX = MT.getX();
        double calY = MT.getY();
        return Math.abs(calX - (boundingX + boundingWidth)) < handleSize
                 && Math.abs(calY - (boundingY + boundingHeight)) < handleSize;
        // return Math.abs(fromX(x) - this.model.getBase()) < handleSize
        //         && Math.abs(fromY(y) - this.model.getHeight()) < handleSize;
    }

    public boolean onRotateHandle(double x, double y){
        Point2D MT = transformOnePoint (x, y);
        double calX = MT.getX();
        double calY = MT.getY();
        return Math.abs(calX - (boundingX + boundingWidth/2 )) < handleSize
                && Math.abs(calY - (boundingY - 10)) < handleSize;
    }

     // You will need to change the hittest to account for transformations.
    public boolean hitTest(Point2D p) {
        return this.getShape().contains(p);
    }

    public Shape getShape() {
        // For printing, do all transformation here

        // affinetransformation to translate
        AffineTransform t = new AffineTransform();

        if (staticCenterPoint != null){
            // System.out.println("centerPoint" + staticCenterPoint);
            // System.out.println("absX " + absX + " absY " + absY);
            System.out.println("Degrees" + Math.toDegrees(radians));
            System.out.println("absScaleX " + absScaleX + " absScaleY " + absScaleY);

            t.translate(staticCenterPoint.getX(), staticCenterPoint.getY());
            t.translate(absX, absY);
            t.rotate(radians);
            t.translate(-staticCenterPoint.getX(), -staticCenterPoint.getY());

            t.translate(staticStartPoint.getX(), staticStartPoint.getY());
            t.scale(absScaleX, absScaleY);
            t.translate(-staticStartPoint.getX(), -staticStartPoint.getY());
        }

        return t.createTransformedShape(shape);
        // return shape;
    }

   

    public Shape updateShape(Point startPoint, Point endPoint) {
        return shape;
    }


    /**
     * Given a ShapeType and the start and end point of the shape, ShapeFactory constructs a new ShapeModel
     * using the class reference in the ShapeType enum and returns it.
     */
    public static class ShapeFactory {
        public ShapeModel getShape(ShapeType shapeType, Point startPoint, Point endPoint) {
           

            try {
                Class<? extends ShapeModel> clazz = shapeType.shape;
                Constructor<? extends ShapeModel> constructor = clazz.getConstructor(Point.class, Point.class);
                
                return constructor.newInstance(startPoint, endPoint);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        
    }

    public enum ShapeType {
        Ellipse(EllipseModel.class),
        Rectangle(RectangleModel.class),
        Line(LineModel.class);

        public final Class<? extends ShapeModel> shape;
        ShapeType(Class<? extends ShapeModel> shape) {
            this.shape = shape;
        }
    }
}
