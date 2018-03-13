import java.awt.*;
import java.awt.geom.Rectangle2D;

import java.lang.*;

public class RectangleModel extends ShapeModel {
    Point oldStartPoint;

    public RectangleModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        /* Fix the problem that rectangles can only be drawn
         * from the top left corner to the bottom right corner
         */
        oldStartPoint = startPoint;
        Rectangle rect = new java.awt.Rectangle(startPoint);
        rect.add(endPoint);
        this.shape = new Rectangle2D.Double(rect.x, rect.y, rect.width, rect.height);

        // Rectangle2D rect = new Rectangle2D.Double(startPoint.x,startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);

        // this.shape = rect;
    }

    @Override
    public Shape updateShape(Point endPoint) {
        Rectangle rect = new java.awt.Rectangle(oldStartPoint);
        rect.add(endPoint);
        return new Rectangle2D.Double(rect.x, rect.y, rect.width, rect.height);
        
    }
}
