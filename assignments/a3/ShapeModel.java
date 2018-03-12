import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShapeModel {
    Shape shape;
    // Point2D mousePosition;
    boolean isSelected = true;

    double scale = 1.0;
    // translation
    // rotation

    Rectangle boundingBox;// = shape.getBounds2D();
    int boundingHeight, boundingWidth, boundingX, boundingY, halfBoundingWidth;
    

    public ShapeModel(Point startPoint, Point endPoint) { }

    public void setBoundingBox(){
        boundingBox = shape.getBounds();
        boundingHeight = boundingBox.height;
        boundingWidth = boundingBox.width;
        boundingX = boundingBox.x;
        boundingY = boundingBox.y;
    }

    public Shape getShape() {
        return shape;
    }

    // You will need to change the hittest to account for transformations.
    public boolean hitTest(Point2D p) {
        return this.getShape().contains(p);
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
