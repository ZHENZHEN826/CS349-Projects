import java.awt.*;
import java.awt.geom.Rectangle2D;

import java.lang.*;

public class RectangleModel extends ShapeModel {

    public RectangleModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        /* Fix the problem that rectangles can only be drawn
         * from the top left corner to the bottom right corner
         */
        Rectangle rect = new java.awt.Rectangle(startPoint);
        rect.add(endPoint);
        this.shape = new Rectangle2D.Double(rect.x, rect.y, rect.width, rect.height);

        // Rectangle2D rect = new Rectangle2D.Double(startPoint.x,startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);

        // this.shape = rect;
    }
}
