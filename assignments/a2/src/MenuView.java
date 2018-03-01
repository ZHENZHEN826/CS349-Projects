
import java.io.*;
import java.util.*;
import java.util.EventObject;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MenuView extends JPanel implements Observer {

	private Model model;

	JMenuBar menuBar = new JMenuBar();

	JMenu fileMenu = new JMenu("File");      
    // 2 choices in "File" menu
    JMenuItem newItem = new JMenuItem ("New");

    JMenuItem exitItem = new JMenuItem ("Exit");

    JMenu editMenu = new JMenu("Edit");
    // 4 choices in "Edit" menu
    JMenuItem selectionModeItem = new JMenuItem ("Selection Mode");
    JMenuItem drawingModeItem = new JMenuItem ("Drawing Mode");
    JMenuItem deleteShapeItem = new JMenuItem ("Delete Shape");
    JMenuItem transformShapeItem = new JMenuItem ("Transform Shape"); 

    JMenu formatMenu = new JMenu("Format");
    // 3 choices in "Format" menu      
    JMenu strokeWidthItem = new JMenu ("Stroke Width");
    JMenuItem px1 = new JMenuItem ("1px");
    JMenuItem px2 = new JMenuItem ("2px");
    JMenuItem px3 = new JMenuItem ("3px");
    JMenuItem px4 = new JMenuItem ("4px");
    JMenuItem px5 = new JMenuItem ("5px");
    JMenuItem px6 = new JMenuItem ("6px");
    JMenuItem px7 = new JMenuItem ("7px");
    JMenuItem px8 = new JMenuItem ("8px");
    JMenuItem px9 = new JMenuItem ("9px");
    JMenuItem px10 = new JMenuItem ("10px");
    JMenuItem fillColorItem = new JMenuItem ("Fill Color");
    JMenuItem strokeColorItem = new JMenuItem ("Stroke Colour");
   
	//private JTextField baseTF = new JTextField(10);
	//private JTextField heightTF = new JTextField(10);
	//private JTextField hypoTF = new JTextField(10);
	JMenuBar toolBar = new JMenuBar();

	JButton selectBtn = new JButton("Select"); 
	JButton drawBtn = new JButton("Draw"); 

	String[] drawingShapes = { "Freeform", "Straight line", "Retangle", "Ellipse" };
	JComboBox shapeList = new JComboBox(drawingShapes);
	
	String[] strokeWidths = { "1px", "2px", "3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px" };
	JComboBox widthList = new JComboBox(strokeWidths);

	JButton fillColorBtn = new JButton("Fill Color"); 
	JButton strokeColorBtn = new JButton("Stroke Color"); 

	public MenuView(Model theModel) {
		super();
		this.model = theModel;
		this.layoutView();
		this.registerControllers();

		// Add a this view as a listener to the model
		this.model.addObserver(this);
	}
	
	/**
	 * What to do when the model changes.
	 */
	 public void update(Object observable) {
		//baseTF.setText("" + model.getBase());
		//heightTF.setText("" + model.getHeight());
		//hypoTF.setText("" + model.getHypotenuse());
	}

	private void layoutView() {
		this.fileMenu.add(this.newItem);
		newItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		this.fileMenu.add(this.exitItem);
		exitItem.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

		this.editMenu.add(this.selectionModeItem);
		selectionModeItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        this.editMenu.add(this.drawingModeItem);
        drawingModeItem.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        this.editMenu.add(this.deleteShapeItem);
        transformShapeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        this.editMenu.add(this.transformShapeItem);
        transformShapeItem.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

        this.strokeWidthItem.add(this.px1);
        this.strokeWidthItem.add(this.px2);
        this.strokeWidthItem.add(this.px3);
        this.strokeWidthItem.add(this.px4);
        this.strokeWidthItem.add(this.px5);
        this.strokeWidthItem.add(this.px6);
        this.strokeWidthItem.add(this.px7);
        this.strokeWidthItem.add(this.px8);
        this.strokeWidthItem.add(this.px9);
        this.strokeWidthItem.add(this.px10);
        this.formatMenu.add(this.strokeWidthItem);
        this.formatMenu.add(this.fillColorItem);
        this.formatMenu.add(this.strokeColorItem);

        this.menuBar.add(this.fileMenu);
        this.menuBar.add(this.editMenu);
        this.menuBar.add(this.formatMenu);

        //button.setMaximumSize(new Dimension(100, 50));
        //button.setPreferredSize(new Dimension(600, 50));

        this.setLayout(new BorderLayout());

        this.add(this.menuBar, BorderLayout.NORTH);

        this.toolBar.add(this.selectBtn);
		this.toolBar.add(this.drawBtn);

		//shapeList.setPreferredSize(new Dimension(100, 15));
		this.toolBar.add(this.shapeList);
		//widthList.setPreferredSize(new Dimension(100, 10));
		this.toolBar.add(this.widthList);

		this.toolBar.add(this.fillColorBtn);

		this.toolBar.add(this.strokeColorBtn);

		this.add(this.toolBar, BorderLayout.SOUTH);
	}

	private void registerControllers() {
		// Add a controller to interpret user actions in the base text field
		// this.baseTF.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent evt) {
		// 		double base = Double.parseDouble(baseTF.getText());
		// 		model.setBase(base);
		// 	}
		// });

		// // Add a controller to interpret user actions in the height text field
		// this.heightTF.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent evt) {
		// 		double height = Double.parseDouble(heightTF.getText());
		// 		model.setHeight(height);
		// 	}
		// });
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
		        Color strokeColor = JColorChooser.showDialog(null, "Choose Stroke Colour", model.strokeColor);
		        model.strokeColor = strokeColor;
			}
		});

		this.newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	      
            }
        });
        
        this.exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.exit(0); 
            }
        });
        
        this.selectionModeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setMode(0);
            }
        });
        

        this.drawingModeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setMode(1);
            }
        });


        this.deleteShapeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    
            }
        });


        this.transformShapeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        this.px1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(1);
            	widthList.setSelectedIndex(0);
            }
        });

        this.px2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(2);
            	widthList.setSelectedIndex(1);
            }
        });
        this.px3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(3);
            	widthList.setSelectedIndex(2);
            }
        });
        this.px4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(4);
            	widthList.setSelectedIndex(3);
            }
        });
        this.px5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(5);
            	widthList.setSelectedIndex(4);
            }
        });
        this.px6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(6);
            	widthList.setSelectedIndex(5);
            }
        });
        this.px7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(7);
            	widthList.setSelectedIndex(6);
            }
        });
        this.px8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(8);
            	widthList.setSelectedIndex(8);
            }
        });
        this.px9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(9);
            	widthList.setSelectedIndex(8);
            }
        });
        this.px10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setStrokeWidth(10);
            	widthList.setSelectedIndex(9);
            }
        });

        this.strokeWidthItem.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
            	
		    }
		    public void menuDeselected(MenuEvent e) {
		        
		    }
	
		    public void menuCanceled(MenuEvent e) {
		       
		    }
        });

        this.fillColorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
		        Color fillColor = JColorChooser.showDialog(null, "Choose Fill Colour", model.fillColor);
		        model.fillColor = fillColor;
			}
        });

        this.strokeColorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color strokeColor = JColorChooser.showDialog(null, "Choose Stroke Colour", model.strokeColor);
		        model.strokeColor = strokeColor;
            }
        });
	}
}