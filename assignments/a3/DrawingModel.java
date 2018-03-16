import java.util.*;
import java.util.List;
import java.awt.*;

// import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.undo.*;

public class DrawingModel extends Observable {
    List<ShapeModel> shapes = new ArrayList<>();

    ShapeModel.ShapeType shapeType = ShapeModel.ShapeType.Rectangle;

    UndoManager undoManager = new UndoManager();
    RectUndoable recUndoable;


    public ShapeModel.ShapeType getShape() {
        return shapeType;
    }

    public void setShape(ShapeModel.ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public DrawingModel() { }

    public void updateViews() {
        setChanged();
        notifyObservers();
    }

    public List<ShapeModel> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    // public List<ShapeModel> getReverseShapes() {
    //     return Collections.reverse(shapes.clone());
    // }
    public double diagonal (int x, int y){
        return Math.sqrt(x * x + y * y);
    }

    public double findRadians(double x, double y){

        if ((x > 0) && (y < 0)){
            // 1st quadrant
            return Math.toRadians(90.0) - Math.atan(Math.abs(y) / x);
        } else if ((x > 0) && (y > 0)) {
            // 2nd quadrant
            return Math.toRadians(90.0) + Math.atan(y / x);
        } else if ((x < 0) && (y > 0)) {
            // 3rd quadrant
            return Math.toRadians(270.0) - Math.atan(y / Math.abs(x));
        } else if ((x < 0) && (y < 0)) {
            // 4th quadrant
            return Math.toRadians(270.0) + Math.atan(Math.abs(y) / Math.abs(x));
        } else {
            System.out.println("findRadians: what's this case?"+ x + " " + y);
            return 0.0;
        }
    }

    public void rotate(ShapeModel shape, Point lastMouse){
        // transfer the center point of the shape to origin, get the transformed lastMouse Position
        
        // System.out.println("centerPoint "+ shape.centerPoint);
        // System.out.println("lastmouse" + lastMouse);
        //Point ptDst = shape.transformOnePoint2(lastMouse.x, lastMouse.y);
        Point ptDst = new Point(lastMouse.x, lastMouse.y);
        ptDst.translate(-shape.staticCenterPoint.x, -shape.staticCenterPoint.y);
        // Point ptDst = shape.transformOnePoint(lastMouse.x, lastMouse.y);
        // determine degree of rotation
        // System.out.println("ptDst" + ptDst);
        double x = ptDst.getX();
        double y = ptDst.getY();
        double radians = findRadians(x, y);
        // System.out.println("Degrees" + Math.toDegrees(radians));
        // save the degree
        if (x !=0 || y != 0){
            shape.radians = radians;
        }

    }

    public void scale(ShapeModel shape, Point2D startMouse, Point2D lastMouse){
        
        // Point at top left corner
        Point2D topLeft = shape.staticStartPoint;
        Point2D bottomRight = shape.staticEndPoint;
        // Inverse lastMouse
        Point2D MT = shape.transformOnePoint(lastMouse.getX(), lastMouse.getY());
        //rotate topLeft and bottomRight

        double x = MT.getX();
        double y = MT.getY();

        // Mouse position
        // double x = lastMouse.getX();
        // double y = lastMouse.getY();

        double oldWidth = bottomRight.getX() - topLeft.getX();
        double newWidth = x - topLeft.getX();

        double oldHeight = bottomRight.getY() - topLeft.getY();
        double newHeight = y - topLeft.getY();

        shape.absScaleX = newWidth/oldWidth;
        shape.absScaleY = newHeight/oldHeight;

        // shape.endPoint = lastMouse;

        updateViews();
    }

    public void endScale(ShapeModel shape, Point startMouse, Point lastMouse){
        // shape.startPoint = startMouse;
        // shape.endPoint = lastMouse;
    }
    public void translate(ShapeModel shape, int dx, int dy){
        shape.absX += dx;
        shape.absY += dy;

        shape.centerPoint.x += dx;
        shape.centerPoint.y += dy;

        shape.startPoint.x += dx;
        shape.startPoint.y += dy;

        shape.endPoint.x += dx;
        shape.endPoint.y += dy;
        updateViews();
    }

    public void endTranslate(ShapeModel shape){
        recUndoable = new RectUndoable(shape, shape.pX, shape.pY, shape.absX, shape.absY);
        undoManager.addEdit(recUndoable);
        shape.pX = shape.absX;
        shape.pY = shape.absY;
    }

    public class RectUndoable extends AbstractUndoableEdit{
        // position for undo
        public int p_translateX = 0;
        public int p_translateY = 0;

        // position for redo
        public int n_translateX = 0;
        public int n_translateY = 0;

        public ShapeModel shape;

        public RectUndoable(ShapeModel shape, int px, int py, int x, int y){
            this.shape = shape;
            // position for undo
            p_translateX = px;
            p_translateY = py;
            // position for redo
            n_translateX = x;
            n_translateY = y;
        }

        public void undo() throws CannotRedoException {
            super.undo();
            this.shape.absX = p_translateX;
            this.shape.absY = p_translateY;
            System.out.println("Model: undo location to " + shape.absX + "," + shape.absY);
            updateViews();
        }

        public void redo() throws CannotRedoException {
            super.redo();
            this.shape.absX = n_translateX;
            this.shape.absY = n_translateY;
            System.out.println("Model: redo location to " + shape.absX + "," + shape.absY);
            updateViews();
        }

    }

    public void updateShape(ShapeModel shape, Point startPoint, Point endPoint) {
        // ShapeModel oldShape = shape;

        // int shapeIndex = shapes.indexOf(oldShape);

        Shape tmp = shape.updateShape(startPoint, endPoint);
        shape.startPoint = startPoint;
        shape.endPoint = endPoint;
        //     //final ShapeModel newShape = shape.shape=;
        // ShapeModel newShape = getNewShape(shape, tmp);

        // UndoableEdit undoableEdit = new AbstractUndoableEdit() {

            
        //     public void undo() throws CannotUndoException {
        //         super.undo();
        //         shapes.remove(shapeIndex);
        //         shapes.add(shapeIndex, oldShape);
        //         //shapes.remove(shapes.size() - 1);
        //         //value = oldValue;
        //         //System.out.println("Undo: update shape newShape: "+ newShape);
        //         System.out.println("Undo: update shape oldShape: "+ oldShape);
                
        //         updateViews();
        //     }

            

        //     public void redo() throws CannotRedoException {
        //         super.redo();
        //         shapes.remove(shapeIndex);

                

        //         shapes.add(shapeIndex, newShape);
        //         //value = newValue;
        //         System.out.println("Redo: update shape newShape: "+ newShape);
        //         System.out.println("Redo: update shape oldShape: "+ oldShape);
        //         updateViews();
        //     }

        // };
        
        // Add this undoable edit to the undo manager
        // undoManager.addEdit(undoableEdit);
        shape = getNewShape(shape, tmp);
        //shape.updateShape(endPoint)
        
        updateViews();
    }

    public ShapeModel getNewShape(ShapeModel shape, Shape tmp){
        shape.shape = tmp;
        return shape;
    }

    public void addDuplicate(){
        for(ShapeModel shape : this.shapes) {
            if (shape.isSelected) {
                int newStartX= shape.startPoint.x + 10;
                int newStartY= shape.startPoint.y + 10;
                Point newStartPoint = new Point(newStartX, newStartY);

                int newEndX = shape.endPoint.x + 10;
                int newEndY = shape.endPoint.y + 10;
                Point newEndPoint = new Point(newEndX, newEndY);
                ShapeModel newShape = new ShapeModel.ShapeFactory().getShape(shape.myShapeType, newStartPoint, newEndPoint);
                this.addShapeField(newShape, newStartPoint, newEndPoint);
                this.shapes.add(newShape);

                shape.isSelected = false;
                newShape.isSelected = true;

                updateViews();
                break;
            }
        } 
        // unselect 
        for (int i =0; i < shapes.size() - 1; i++){
            shapes.get(i).isSelected = false;
        }
        
    }

    public void addShapeField(ShapeModel shape, Point startPoint, Point endPoint){
        // Set shape variables when new shape is drawn
        shape.myShapeType = this.getShape();

        shape.startPoint = startPoint;
        shape.endPoint = endPoint;
        shape.centerPoint = new Point((startPoint.x+endPoint.x)/2, (startPoint.y + endPoint.y)/2);
        
        shape.staticStartPoint = startPoint;
        shape.staticEndPoint = endPoint;
        shape.staticCenterPoint = new Point((startPoint.x+endPoint.x)/2, (startPoint.y + endPoint.y)/2);

        shape.absX = 0;
        shape.absY = 0;
        shape.pX = 0;
        shape.pY = 0;

        shape.setBoundingBox();
    }

    public void addShape(ShapeModel shape) {
        UndoableEdit undoableEdit = new AbstractUndoableEdit() {
            // final int oldValue = value;
            // final int newValue = v;
            final ShapeModel newShape = shape;
            //final ShapeModel oldShape = shapes.get(shapes.size() - 1);

            // Method that is called when we must redo the undone action
            public void redo() throws CannotRedoException {
                super.redo();
                shapes.add(newShape);
                //value = newValue;
                // System.out.println("Model: redo value to "+ newShape);
                updateViews();
            }

            public void undo() throws CannotUndoException {
                super.undo();
                shapes.remove(newShape);
                //shapes.remove(shapes.size() - 1);
                //value = oldValue;
                // System.out.println("Model: undo value to "+ newShape);
                updateViews();
            }
        };
        
        // Add this undoable edit to the undo manager
        undoManager.addEdit(undoableEdit);
        this.shapes.add(shape);
        updateViews();
    }


    

    public void undo(){
        if(undoManager.canUndo()){
            try {
                undoManager.undo();
            } catch (CannotRedoException ex) {
            }               
            updateViews();          
        }
    }

    public void redo(){
        if(undoManager.canRedo()){
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
            }               
            updateViews();          
        }
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }
}
