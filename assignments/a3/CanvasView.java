import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.*;
// import java.util.Observable;
// import java.util.Observer;

public class CanvasView extends JPanel implements Observer {
    DrawingModel model;
    Point2D lastMouse;
    Point2D startMouse;
    boolean mouseDragged;
    boolean scaling = false;
    Point2D clickPosition = null;

    public CanvasView(DrawingModel model) {
        super();
        this.model = model;

        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mousePressed(e);
                clickPosition = e.getPoint();

                for(ShapeModel shape : model.getShapes()) {
                    // g2.draw(shape.getShape());
                    if (shape.hitTest(clickPosition)){
                        shape.isSelected = true;
                        System.out.println("Hit!!!");
                        break;
                    } else {
                        shape.isSelected = false;
                    }

                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastMouse = e.getPoint();
                startMouse = e.getPoint();

                for(ShapeModel shape : model.getShapes()) {
                    shape.scaleSelected = shape.onBottomCorner(e.getX(), e.getY());
                    if (!shape.scaleSelected)
                        shape.isSelected = false;

                    System.out.println("scale selected: " + shape.scaleSelected);
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                lastMouse = e.getPoint();
                mouseDragged = true;

                for(ShapeModel shape : model.getShapes()) {
                    if (shape.scaleSelected) {
                        scaling = true;
                        //shape.updateShape((Point) lastMouse);
                        model.updateShape(shape, (Point) lastMouse);
                    }
                }
                
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (mouseDragged && !scaling){
                    ShapeModel shape = new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse);
                    model.addShape(shape);
                }
                
                startMouse = null;
                lastMouse = null;
                mouseDragged = false;
                scaling = false;
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
                shape.setBoundingBox();
                int x = shape.boundingX + shape.boundingWidth;
                int y = shape.boundingY + shape.boundingHeight;
                g2.fillRect(x - 5, y - 5, 10, 10);
                // Draw rotation handles
                x = shape.boundingX + (shape.boundingWidth/2);
                y = shape.boundingY - 10;
                g2.fillOval(x - 5 , y - 5, 10, 10);
                System.out.println("Draw two dots!");
            }

        }
    }

    private void drawCurrentShape(Graphics2D g2) {
        if (startMouse == null || scaling) {
            return;
        }

        g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.draw(new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse).getShape());
    }
}
