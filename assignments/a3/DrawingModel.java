import java.util.*;
import java.util.List;
import java.awt.*;
// import java.awt.Point;
import java.awt.geom.Point2D;
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

    public void updateShape(ShapeModel shape, Point endPoint) {
        // ShapeModel oldShape = shape;

        // int shapeIndex = shapes.indexOf(oldShape);

        Shape tmp = shape.updateShape(endPoint);
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

                int newEndX= shape.endPoint.x + 10;
                int newEndY= shape.endPoint.y + 10;
                Point newEndPoint = new Point(newEndX, newEndY);
                ShapeModel newShape = new ShapeModel.ShapeFactory().getShape(shape.myShapeType, newStartPoint, newEndPoint);
                this.addShapeField(newShape, newStartPoint, newEndPoint);
                this.shapes.add(newShape);
                shape.isSelected = false;
                newShape.isSelected = true;
                updateViews();
                return;
            }

        } 
        
    }

    public void addShapeField(ShapeModel shape, Point startPoint, Point endPoint){
        shape.myShapeType = this.getShape();
        shape.startPoint = startPoint;
        shape.endPoint = endPoint;
    }

    public void addShape(ShapeModel shape) {
        // recUndoable = new RectUndoable(pX, pY, absX, absY);
        // undoManager.addEdit(recUndoable);
        

        UndoableEdit undoableEdit = new AbstractUndoableEdit() {

            // capture variables for closure
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


    public class RectUndoable extends AbstractUndoableEdit{

        // position for undo
        public int p_translateX = 0;
        public int p_translateY = 0;

        // position for redo
        public int n_translateX = 0;
        public int n_translateY = 0;


        public RectUndoable(int px, int py, int x, int y){
            // position for undo
            p_translateX = px;
            p_translateY = py;
            // position for redo
            n_translateX = x;
            n_translateY = y;
        }


        public void undo() throws CannotRedoException {
            super.undo();
            // absX = p_translateX;
            // absY = p_translateY;
            // System.out.println("Model: undo location to " + absX + "," + absY);
            updateViews();
        }

        public void redo() throws CannotRedoException {
            super.redo();
            // absX = n_translateX;
            // absY = n_translateY;
            // System.out.println("Model: redo location to " + absX + "," + absY);
            updateViews();
        }

    }

    // public void undo() {
    //     if (canUndo())
    //         undoManager.undo();
    // }

    // public void redo() {
    //     if (canRedo())
    //         undoManager.redo();
    // }

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
