import java.awt.*;
import java.awt.geom.*;
// import java.awt.geom.Point2D;
// import java.awt.geom.AffineTransform;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import javax.swing.undo.*;

public class ShapeModel {
    Shape shape;
    ShapeType myShapeType;

    // Change with transformation
    Point startPoint=null; 
    Point endPoint=null;
    Point centerPoint = null;
    // Not changing
    Point staticCenterPoint=null;
    Point staticStartPoint=null;
    Point staticEndPoint=null;
    // What transformation is the shape doing?
    boolean isSelected = true;
    boolean scaleSelected = false;
    boolean translateSelected = false;
    boolean rotateSelected = false;
    boolean scaleXSelected = false;

    // Translation variables
    int absX = 0; // absolute position of the shape
    int absY = 0;
    
    int pX = 0; // position before dragged
    int pY = 0;
    
    // Scaling variables
    double absScaleX = 1.0;
    double absScaleY = 1.0;
    double absScaleOnX = 1.0;

    double pScaleX = 1.0;
    double pScaleY = 1.0;
    double pScaleOnX = 1.0;

    // Rotation variables
    double radians = 0.0;

    double pRadians = 0.0;

    // Bounding box when shape is created
    Rectangle2D boundingBox;
    double boundingHeight, boundingWidth, boundingX, boundingY;

    // Rotation and scaling handles' size
    double handleSize = 5.0;

    public ShapeModel(Point startPoint, Point endPoint) {}

    public void setBoundingBox(){
        boundingBox = shape.getBounds2D();

        boundingHeight = boundingBox.getHeight();
        boundingWidth = boundingBox.getWidth();
        boundingX = boundingBox.getX();
        boundingY = boundingBox.getY();
    }

    public double getHandleSizeX(){
        return (handleSize/absScaleX/absScaleOnX);
    }
    public double getHandleSizeY(){
        return (handleSize/absScaleY);
    }

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
        AT1.scale(absScaleX, absScaleY);
        AT1.scale(absScaleOnX, 1);
        AT1.translate(-staticCenterPoint.getX(), -staticCenterPoint.getY());

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
        return MT;
    }

    public boolean onBottomCorner(double x, double y) {
        Point2D MT = transformOnePoint (x, y);
        double calX = MT.getX();
        double calY = MT.getY();
        return Math.abs(calX - (boundingX + boundingWidth)) < handleSize
                 && Math.abs(calY - (boundingY + boundingHeight)) < handleSize;
    }

    public boolean onRotateHandle(double x, double y){
        Point2D MT = transformOnePoint (x, y);
        double calX = MT.getX();
        double calY = MT.getY();
        return Math.abs(calX - (boundingX + boundingWidth/2 )) < handleSize
                && Math.abs(calY - (boundingY - 10)) < handleSize;
    }

    public boolean onScaliingHandleX(double x, double y){
        Point2D MT = transformOnePoint (x, y);
        double calX = MT.getX();
        double calY = MT.getY();
        return Math.abs(calX - (boundingX + boundingWidth)) < handleSize
                && Math.abs(calY - (boundingY + boundingHeight/2)) < handleSize;
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
            // System.out.println("Degrees" + Math.toDegrees(radians));
            // System.out.println("absScaleX " + absScaleX + " absScaleY " + absScaleY);

            t.translate(staticCenterPoint.getX(), staticCenterPoint.getY());
            t.translate(absX, absY);
            t.rotate(radians);
            t.scale(absScaleX, absScaleY);
            t.scale(absScaleOnX, 1);
            t.translate(-staticCenterPoint.getX(), -staticCenterPoint.getY());
        }

        return t.createTransformedShape(shape);
        // return shape;
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
