import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EllipseModel extends ShapeModel {
	Point oldStartPoint;

    public EllipseModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        oldStartPoint = startPoint;
        Rectangle rect = new java.awt.Rectangle(startPoint);
        rect.add(endPoint);
        this.shape = new Ellipse2D.Double(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public Shape updateShape(Point endPoint) {
        Rectangle rect = new java.awt.Rectangle(oldStartPoint);
        rect.add(endPoint);
        return new Ellipse2D.Double(rect.x, rect.y, rect.width, rect.height);
    }
}
