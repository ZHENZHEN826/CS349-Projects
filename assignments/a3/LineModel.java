import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class LineModel extends ShapeModel {

    private Point a;
    private Point b;

    public LineModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
    
        this.a = startPoint;
        this.b = endPoint;

        Path2D path = new Path2D.Double();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);
        this.shape = path;
    }

    @Override
    public boolean hitTest(Point2D p) {
        System.out.println("p: " + p);
        Point ptSrc = (Point)p;
        System.out.println("ptSrc: " + ptSrc);
        Point ptDst = new Point();

        AffineTransform AT1;
        AT1 = new AffineTransform();

        // System.out.println("absX " + absX + " absY " + absY);
        // AT1.translate(this.centerPoint.getX(), this.centerPoint.getY());
        AT1.translate(-this.absX, -this.absY);
        // AT1.rotate(-this.radians);
        // AT1.scale(-this.absScaleX, this.absScaleY);
        // AT1.translate(-this.centerPoint.getX(), -this.centerPoint.getY());

        AT1.transform(ptSrc, ptDst);
        // System.out.println("ptDst: " + ptDst);
        // System.out.println("a: " + a + "b: " +b);
        return pointToLineDistance(a,b,ptSrc) < 10;
    }

    public double pointToLineDistance(Point A, Point B, Point P) {
        double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
        return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
    }
}