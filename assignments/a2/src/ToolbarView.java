
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ToolbarView extends JPanel implements Observer {
	private Model model;

	JButton selectBtn = new JButton("Select"); 
	JButton drawBtn = new JButton("Draw"); 

	String[] drawingShapes = { "Freeform", "Straight line", "Retangle", "Ellipse" };
	JComboBox shapeList = new JComboBox(drawingShapes);
	
	String[] strokeWidths = { "1px", "2px", "3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px" };
	JComboBox widthList = new JComboBox(strokeWidths);

	JButton fillColorBtn = new JButton("Fill Color"); 
	JButton strokeColorBtn = new JButton("Stroke Color"); 

	public ToolbarView(Model theModel) {
		super();
		this.model = theModel;
		this.layoutView();
		this.registerControllers();

		// Add a this view as a listener to the model
		this.model.addObserver(this);
	}

	public void update(Object observable) {

	}

	private void layoutView() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// add components
		this.add(selectBtn);
		this.add(drawBtn);

		shapeList.setPreferredSize(new Dimension(100, 15));
		this.add(shapeList);
		widthList.setPreferredSize(new Dimension(100, 10));
		this.add(widthList);

		this.add(fillColorBtn);
		this.add(strokeColorBtn);

	}

	private void registerControllers() {


	}
}