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
    TranslateUndoable translateUndoable;
    ScaleUndoable scaleUndoable;
    ScaleXUndoable scaleXUndoable;
    RotateUndoable rotateUndoable;


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

        Point ptDst = new Point(lastMouse.x, lastMouse.y);
        ptDst.translate(-shape.absX, -shape.absY);
        ptDst.translate(-shape.staticCenterPoint.x, -shape.staticCenterPoint.y);

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

    public void scaleX(ShapeModel shape, Point lastMouse){
        // Point at top left corner
        Point2D topLeft = shape.staticStartPoint;
        Point2D bottomRight = shape.staticEndPoint;
        
        Point ptSrc = new Point(lastMouse.x, lastMouse.y);
        Point ptDst = new Point();

        AffineTransform AT1;
        AT1 = new AffineTransform();
        
        AT1.translate(shape.centerPoint.getX(), shape.centerPoint.getY());
        AT1.scale(1/shape.absScaleX, 1/shape.absScaleY);
        AT1.rotate(-shape.radians);
        AT1.translate(-shape.centerPoint.getX(), -shape.centerPoint.getY());
        AT1.transform(ptSrc, ptDst);

        double x = ptDst.getX();
        double y = ptDst.getY();

        double oldWidth = bottomRight.getX() - topLeft.getX();
        double z1 = topLeft.getX()+bottomRight.getX()-x;
        double newWidth = x - z1;

        shape.absScaleOnX = newWidth/oldWidth;

        updateViews();
    }

    public void scale(ShapeModel shape, Point lastMouse){
        // Point at top left corner
        Point2D topLeft = shape.staticStartPoint;
        Point2D bottomRight = shape.staticEndPoint;
        
        Point ptSrc = new Point(lastMouse.x, lastMouse.y);
        Point ptDst = new Point();

        AffineTransform AT1;
        AT1 = new AffineTransform();
        
        AT1.translate(shape.centerPoint.getX(), shape.centerPoint.getY());
        AT1.scale(1/shape.absScaleOnX, 1);
        AT1.rotate(-shape.radians);
        AT1.translate(-shape.centerPoint.getX(), -shape.centerPoint.getY());
        AT1.transform(ptSrc, ptDst);

        double x = ptDst.getX();
        double y = ptDst.getY();

        double oldWidth = bottomRight.getX() - topLeft.getX();
        double z1 = topLeft.getX()+bottomRight.getX()-x;
        double newWidth = x - z1;

        double oldHeight = bottomRight.getY() - topLeft.getY();
        double z2 = topLeft.getY()+bottomRight.getY()-y;
        double newHeight = y - z2;

        shape.absScaleX = newWidth/oldWidth;
        shape.absScaleY = newHeight/oldHeight;


        updateViews();
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

    public void endRotate(ShapeModel shape){
        rotateUndoable = new RotateUndoable(shape, shape.pRadians, shape.radians);
        undoManager.addEdit(rotateUndoable);
        shape.pRadians = shape.radians;
    }

    public class RotateUndoable extends AbstractUndoableEdit{
        // position for undo
        public double p_radian = 0;

        // position for redo
        public double n_radian = 0;

        public ShapeModel shape;

        public RotateUndoable(ShapeModel shape, double pr, double r){
            this.shape = shape;
            // position for undo
            p_radian = pr;

            // position for redo
            n_radian = r;
        }

        public void undo() throws CannotRedoException {
            super.undo();
            this.shape.radians = p_radian;
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: undo rotate to " + shape.radians);
            updateViews();
        }

        public void redo() throws CannotRedoException {
            super.redo();
            this.shape.radians = n_radian;
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: redo rotate to " + shape.radians);
            updateViews();
        }

    }

    public void endScale(ShapeModel shape){
        scaleUndoable = new ScaleUndoable(shape, shape.pScaleX, shape.pScaleY, shape.absScaleX, shape.absScaleY);
        undoManager.addEdit(scaleUndoable);
        shape.pScaleX = shape.absScaleX;
        shape.pScaleY = shape.absScaleY;
    }

    public class ScaleUndoable extends AbstractUndoableEdit{
        // position for undo
        public double p_scaleX = 0;
        public double p_scaleY = 0;

        // position for redo
        public double n_scaleX = 0;
        public double n_scaleY = 0;

        public ShapeModel shape;

        public ScaleUndoable(ShapeModel shape, double px, double py, double x, double y){
            this.shape = shape;
            // position for undo
            p_scaleX = px;
            p_scaleY = py;
            // position for redo
            n_scaleX = x;
            n_scaleY = y;
        }

        public void undo() throws CannotRedoException {
            super.undo();
            this.shape.absScaleX = p_scaleX;
            this.shape.absScaleY = p_scaleY;
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: undo scaling to " + shape.absScaleX + "," + shape.absScaleY);
            updateViews();
        }

        public void redo() throws CannotRedoException {
            super.redo();
            this.shape.absScaleX = n_scaleX;
            this.shape.absScaleY = n_scaleY;
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: redo scaling to " + shape.absScaleX + "," + shape.absScaleY);
            updateViews();
        }

    }

    public void endScaleX(ShapeModel shape){
        scaleXUndoable = new ScaleXUndoable(shape, shape.pScaleOnX, shape.absScaleOnX);
        undoManager.addEdit(scaleXUndoable);
        shape.pScaleOnX = shape.absScaleOnX;
    }

    public class ScaleXUndoable extends AbstractUndoableEdit{
        // position for undo
        public double p_scaleX = 0;
        public double p_scaleY = 0;

        // position for redo
        public double n_scaleX = 0;
        public double n_scaleY = 0;

        public ShapeModel shape;

        public ScaleXUndoable(ShapeModel shape, double px,double x){
            this.shape = shape;
            // position for undo
            p_scaleX = px;
            // position for redo
            n_scaleX = x;
        }

        public void undo() throws CannotRedoException {
            super.undo();
            this.shape.absScaleOnX = p_scaleX;
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: undo scaling to " + shape.absScaleX + "," + shape.absScaleY);
            updateViews();
        }

        public void redo() throws CannotRedoException {
            super.redo();
            this.shape.absScaleOnX = n_scaleX;
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: redo scaling to " + shape.absScaleX + "," + shape.absScaleY);
            updateViews();
        }

    }

    public void endTranslate(ShapeModel shape){
        translateUndoable = new TranslateUndoable(shape, shape.pX, shape.pY, shape.absX, shape.absY);
        undoManager.addEdit(translateUndoable);
        shape.pX = shape.absX;
        shape.pY = shape.absY;
    }

    public class TranslateUndoable extends AbstractUndoableEdit{
        // position for undo
        public int p_translateX = 0;
        public int p_translateY = 0;

        // position for redo
        public int n_translateX = 0;
        public int n_translateY = 0;

        public ShapeModel shape;

        public TranslateUndoable(ShapeModel shape, int px, int py, int x, int y){
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
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: undo location to " + shape.absX + "," + shape.absY);
            updateViews();
        }

        public void redo() throws CannotRedoException {
            super.redo();
            this.shape.absX = n_translateX;
            this.shape.absY = n_translateY;
            for(ShapeModel shape : getShapes()) {
                shape.isSelected = false;
            }
            this.shape.isSelected = true;
            System.out.println("Model: redo location to " + shape.absX + "," + shape.absY);
            updateViews();
        }

    }

    public void addDuplicate(){
        for(ShapeModel shape : this.shapes) {
            if (shape.isSelected) {

                int newStartX = shape.staticStartPoint.x;// + 10;
                int newStartY = shape.staticStartPoint.y;// + 10;
                Point newStartPoint = new Point(newStartX, newStartY);

                int newEndX = shape.staticEndPoint.x;// + 10;
                int newEndY = shape.staticEndPoint.y;// + 10;
                Point newEndPoint = new Point(newEndX, newEndY);
                
                ShapeModel newShape = new ShapeModel.ShapeFactory().getShape(shape.myShapeType, newStartPoint, newEndPoint);

                this.copyTransformation(newShape, shape);
                // this.addShapeField(newShape, newStartPoint, newEndPoint);
                shape.isSelected = false;
                newShape.isSelected = true;

                newShape.absX += 10;
                newShape.absY += 10;

                newShape.pX += 10;
                newShape.pY += 10;

                // newShape.startPoint.x += 10;
                // newShape.startPoint.y += 10;

                // newShape.endPoint.x += 10;
                // newShape.endPoint.y += 10;

                // newShape.centerPoint.x += 10;
                // newShape.centerPoint.y += 10;

                // newShape.centerPoint = new Point((newShape.startPoint.x+newShape.endPoint.x)/2, (newShape.startPoint.y + newShape.endPoint.y)/2);


                // newShape.staticStartPoint.x += 10;
                // newShape.staticStartPoint.y += 10;
                // newShape.staticEndPoint.x += 10;
                // newShape.staticEndPoint.y += 10;
                // newShape.staticCenterPoint.x += 10;
                // newShape.staticCenterPoint.y += 10;

                newShape.setBoundingBox();


                UndoableEdit undoableEdit = new AbstractUndoableEdit() {
                    final ShapeModel addNewShape = newShape;

                    // Method that is called when we must redo the undone action
                    public void redo() throws CannotRedoException {
                        super.redo();
                        shapes.add(addNewShape);
                        addNewShape.isSelected = true;
                        // System.out.println("Model: redo value to "+ newShape);
                        updateViews();
                    }

                    public void undo() throws CannotUndoException {
                        super.undo();
                        shapes.remove(addNewShape);
                        for(ShapeModel shape : getShapes()) {
                            shape.isSelected = false;
                        }
                        // System.out.println("Model: undo value to "+ newShape);
                        updateViews();
                    }
                };
                
                // Add this undoable edit to the undo manager
                undoManager.addEdit(undoableEdit);
                this.shapes.add(newShape);
                updateViews();

                break;
            }
        } 
        // unselect 
        for (int i =0; i < shapes.size() - 1; i++){
            shapes.get(i).isSelected = false;
        }
        
    }

    public void copyTransformation (ShapeModel newShape, ShapeModel old){
        newShape.myShapeType = old.myShapeType;
        newShape.absX = old.absX;
        newShape.absY = old.absY;
        newShape.pX = old.pX;
        newShape.pY = old.pY;

        newShape.absScaleX = old.absScaleX;
        newShape.absScaleY = old.absScaleY;
        newShape.pScaleX = old.pScaleX;
        newShape.pScaleY = old.pScaleY;

        newShape.radians = old.radians;
        newShape.pRadians = old.pRadians;

        newShape.startPoint = old.startPoint;
        // newShape.startPoint.x += 10;
        // newShape.startPoint.y += 10;
        newShape.endPoint = old.endPoint;
        // newShape.endPoint.x += 10;
        // newShape.endPoint.y += 10;
        newShape.centerPoint = old.centerPoint;
        // newShape.centerPoint.x += 10;
        // newShape.centerPoint.y += 10;

        newShape.staticStartPoint = old.staticStartPoint;
        newShape.staticEndPoint = old.staticEndPoint;
        newShape.staticCenterPoint = old.staticCenterPoint;

        newShape.boundingBox = old.boundingBox;
        newShape.boundingHeight = old.boundingHeight;
        newShape.boundingWidth = old.boundingWidth;
        newShape.boundingX = old.boundingX; 
        newShape.boundingY = old.boundingY;

        newShape.handleSize = old.handleSize;
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

        shape.setBoundingBox();
    }

    public void addShape(ShapeModel shape) {
        UndoableEdit undoableEdit = new AbstractUndoableEdit() {
            final ShapeModel newShape = shape;

            // Method that is called when we must redo the undone action
            public void redo() throws CannotRedoException {
                super.redo();
                shapes.add(newShape);
                newShape.isSelected = true;
                updateViews();
            }

            public void undo() throws CannotUndoException {
                super.undo();
                shapes.remove(newShape);
                for(ShapeModel shape : getShapes()) {
                    shape.isSelected = false;
                }
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
