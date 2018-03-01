
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
    JMenuItem strokeWidthItem = new JMenuItem ("Stroke Width");
    JMenuItem fillColorItem = new JMenuItem ("Fill Color");
    JMenuItem strokeColorItem = new JMenuItem ("Stroke Colour");
   
	//private JTextField baseTF = new JTextField(10);
	//private JTextField heightTF = new JTextField(10);
	//private JTextField hypoTF = new JTextField(10);

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
		this.fileMenu.add(this.exitItem);

		this.editMenu.add(this.selectionModeItem);
        this.editMenu.add(this.drawingModeItem);
        this.editMenu.add(this.deleteShapeItem);
        this.editMenu.add(this.transformShapeItem);

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
		this.newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    
            }
        });
        
        this.exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             
            }
        });
        
        this.selectionModeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    
            }
        });
        

        this.drawingModeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    
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

        this.strokeWidthItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// strokeWidthItem.get
             //    model.setStrokeWidth();
            }
        });

        this.fillColorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        this.strokeColorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
	}
}