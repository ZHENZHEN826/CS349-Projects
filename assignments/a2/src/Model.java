
import java.util.*;
import java.awt.Color;

public class Model {
    // the data in the model
    int mode = 1;              // 0 for selection mode, 1 for drawing mode
    int drawingShape= 0;       // 0 for freeform line, 1 for straight line,
                               // 2 for rectangle, 3 for ellipse 
    int strokeWidth = 1;       // 1 -10 px
    Color fillColor= Color.white;   // Color used to fill a shape
    Color strokeColor = Color.black; // Color used in stroke

    /** The observers that are watching this model for changes. */
    private List<Observer> observers;

    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList<Observer>();
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    /**
     * Set 5 properties here.
     */
    public void setMode (int theMode){
        this.mode = theMode;
        this.notifyObservers();
    }

    public void setDrawingShape (int theShape){
        this.drawingShape = theShape;
        this.notifyObservers();
    }

    public void setStrokeWidth (int theWidth){
        this.strokeWidth = theWidth;
        this.notifyObservers();
    }

    public void setFillColor (Color theColor){
        this.fillColor = theColor;
        this.notifyObservers();
    }

    public void setStokeColor (Color theColor){
        this.strokeColor = theColor;
        this.notifyObservers();
    }
    
    /**
     * Get 5 properties here.
     */
    public int getMode (){
        return this.mode;
    }

    public int getDrawingShape (){
        return this.drawingShape;
    }

    public int getStrokeWidth (){
        return this.strokeWidth;
    }

    public Color getFillColor (){
        return this.fillColor;
    }

    public Color getStrokeColor (){
        return this.strokeColor;
    }
}
