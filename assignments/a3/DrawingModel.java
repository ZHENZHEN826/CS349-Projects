import java.util.*;
import java.util.List;

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
                System.out.println("Model: redo value to "+ newShape);
                updateViews();
            }

            public void undo() throws CannotUndoException {
                super.undo();
                shapes.remove(newShape);
                //shapes.remove(shapes.size() - 1);
                //value = oldValue;
                System.out.println("Model: undo value to "+ newShape);
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
