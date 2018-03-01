
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
		this.selectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				model.setMode(0);
			}
		});

		this.drawBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				model.setMode(1);
			}
		});


		this.widthList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
            	String width = event.getItem().toString();
            	if (width == "1px"){
            		model.setStrokeWidth(1);
            	} else if (width == "2px"){
            		model.setStrokeWidth(2);
            	} else if (width == "3px"){
            		model.setStrokeWidth(3);
            	} else if (width == "4px"){
            		model.setStrokeWidth(4);
            	} else if (width == "5px"){
            		model.setStrokeWidth(5);
            	} else if (width == "6px"){
            		model.setStrokeWidth(6);
            	} else if (width == "7px"){
            		model.setStrokeWidth(7);
            	} else if (width == "8px"){
            		model.setStrokeWidth(8);
            	} else if (width == "9px"){
            		model.setStrokeWidth(9);
            	} else if (width == "10px"){
            		model.setStrokeWidth(10);
            	} else {
            		return;
            	}  	
            }
        });

        this.shapeList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
            	String shape = event.getItem().toString();
            	if (shape == "Freeform"){
            		model.setDrawingShape(0);
            	} else if (shape == "Straight line"){
            		model.setDrawingShape(1);
            	} else if (shape == "Retangle"){
            		model.setDrawingShape(2);
            	} else if (shape == "Ellipse"){
            		model.setDrawingShape(3);
            	} else {
            		return;
            	}  	
            }
        });

    	this.fillColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        Color fillColor = JColorChooser.showDialog(null, "Choose Fill Colour", model.fillColor);
		        model.fillColor = fillColor;
			}
		});

		this.strokeColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        Color strokeColor = JColorChooser.showDialog(null, "Choose Fill Colour", model.strokeColor);
		        model.strokeColor = strokeColor;
			}
		});
	}
}