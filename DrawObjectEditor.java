import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

/**
 * The Class DrawObjectEditor sets up the GUI Application display and controls the flow of the program.
 * This application alllows users to draw a line, circle, triangle and quadrilateral allwowing them to select
 * their objects and move, copy, colour and deleteit. It also allows the user to save the objects created via 
 * two ways including an object file and a text file. Further allowing the user to load/import his drawn objects
 * via the two methods respectively. 
 * It extends the Class JFrame.
 * @author Suyash Lohia 
 * @version 1.0
 *
 */
public class DrawObjectEditor extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel jpbase = new JPanel();
	private JFrame jf = new JFrame();
	private ArrayList<Color> ShapeColors = new ArrayList<Color>();
	private ArrayList<Shape> Shapes = new ArrayList<Shape>();
	private JButton LineBtn = new JButton("Line");
	private JButton CircleBtn = new JButton("Circle");
	private JButton TriangleBtn = new JButton("Triangle");
	private JButton QuadrilateralBtn = new JButton("Quadrilateral");
	private JButton SelectBtn = new JButton("Select");
	private JButton DeleteBtn = new JButton("Delete");
	private JButton RColorBtn = new JButton("Random Color");
	private JButton MoveBtn = new JButton("Move");
	private JButton CopyBtn = new JButton("Copy");
	private JButton SaveBtn= new JButton("Save");
	private JButton LoadBtn= new JButton("Load");
	private JButton ExportBtn= new JButton("Export");
	private JButton ImportBtn= new JButton("Import");
	private int no_of_shapes=0;

	private Mypanel MyCanvas = new Mypanel(jf, Shapes, ShapeColors);
	

	/**
	 * Main functionin initialising the DrawObjectEditor class by declring an instance and calling the go method 
	 * @param arg, compulsory string array argument for main function. 
	 */
	public static void main(String arg[]) {
		DrawObjectEditor GuiApplication = new DrawObjectEditor();
		GuiApplication.Go();
	}
	/**
	 * Method which sets up the Graphical User Interface of the application by adding 
	 * frames,buttons and panels. 
	 */
	public void Go() {

		LineBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ActivateButton(false,LineBtn);
				MyCanvas.AddShape("line", LineBtn);
		}});
		
		CircleBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ActivateButton(false,CircleBtn);
				MyCanvas.AddShape("circle", CircleBtn);
		}});
		
		TriangleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ActivateButton(false,TriangleBtn);
				MyCanvas.AddShape("triangle", TriangleBtn);
		}});
		
		QuadrilateralBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActivateButton(false,QuadrilateralBtn);
				MyCanvas.AddShape("quad", QuadrilateralBtn);	
		}});
		
		SelectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActivateButton(false,SelectBtn);
				MyCanvas.SelectShape("select", MoveBtn, DeleteBtn, CopyBtn, RColorBtn, SelectBtn);
		}});
		
		DeleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShapeEditButtons(false);
				MyCanvas.DeleteShape();
				ActivateButton(true,SelectBtn);
		}});
		
		RColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShapeEditButtons(false);
				MyCanvas.RColorShape();
				ActivateButton(true,SelectBtn);	
		}});
		
		MoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShapeEditButtons(false);
				MyCanvas.AddShape("move", SelectBtn);
		}});
		
		CopyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShapeEditButtons(false);
				MyCanvas.CopyShape();
				ActivateButton(true,SelectBtn);
		}});
		
		SaveBtn.addActionListener(new SaveBtnListn());
		LoadBtn.addActionListener(new LoadBtnListn());
		ExportBtn.addActionListener(new ExportBtnListn());
		ImportBtn.addActionListener(new ImportBtnListn());
		
		jpbase.setLayout(new GridLayout(4,3));
		jpbase.add(LineBtn);
		jpbase.add(CircleBtn);
		jpbase.add(TriangleBtn);
		jpbase.add(QuadrilateralBtn);
		jpbase.add(SelectBtn);
		jpbase.add(MoveBtn);
		jpbase.add(DeleteBtn);
		jpbase.add(CopyBtn);
		jpbase.add(RColorBtn);
		jpbase.add(SaveBtn);
		jpbase.add(LoadBtn);
		jpbase.add(ExportBtn);
		jpbase.add(ImportBtn);
		

		ShapeEditButtons(false);
		jf.add(BorderLayout.SOUTH,jpbase);
		jf.add(MyCanvas);
		jf.setSize(400, 450);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		jf.setVisible(true);
	}
	
	
	/**
	 * An Inner Class handling the event when Save Button is clicked to save the drawn shapes in 
	 * an .object file
	 * @author Suyash Lohia 
	 * @version 1.0
	 */
	class SaveBtnListn implements ActionListener{

		/**
		 * Action performed function implementing the saving of drawn shapes
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser ChooseFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int check = ChooseFile.showSaveDialog(null);
			String filepath ="";
			if (JFileChooser.CANCEL_OPTION== check) {
				System.exit(1);
			}
			else if (JFileChooser.APPROVE_OPTION==check) {
				File ActualFile = ChooseFile.getSelectedFile();
				filepath= ActualFile.getAbsolutePath().toString();
			}
							
			try {
				FileOutputStream FOut = new FileOutputStream(new File(filepath));
				ObjectOutputStream OOut = new ObjectOutputStream(FOut);
				
				no_of_shapes=Shapes.size();
				
				OOut.writeObject(no_of_shapes);

			    for (Shape s : Shapes) {
			    	OOut.writeObject(s);
			    }

				for (Color c: ShapeColors) {
					OOut.writeObject(c);
				}
					
			    OOut.close();
				FOut.close();

			}  catch (Exception err) {
				System.out.println("Save Game Error!");
			}
			
		}
		
	}
	/**
	 * An Inner Class handling the event when Load Button is clicked to load already drawn shapes 
	 * from an .object file
	 * @author Suyash Lohia 
	 * @version 1.0
	 */
	class LoadBtnListn implements ActionListener{

		/**
		 * Action performed function implementing the loading the drawn shapes
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser ChooseFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int check = ChooseFile.showOpenDialog(null);
			String filepath ="";
			if (JFileChooser.CANCEL_OPTION== check) {
				System.exit(1);
			}
			else if (JFileChooser.APPROVE_OPTION==check) {
				File ActualFile = ChooseFile.getSelectedFile();
				filepath= ActualFile.getAbsolutePath().toString();
			}
			if (false ==filepath.equals("")) {
				FileInputStream FInput = null;
				ObjectInputStream OInput = null;
				
				try {
					
					FInput = new FileInputStream(new File(filepath));
					OInput = new ObjectInputStream(FInput);
					
					MyCanvas.removedata();
					Shapes.clear();
					ShapeColors.clear();
					
					no_of_shapes= (int) OInput.readObject();
					
					for (int i = 0; i < no_of_shapes; ++i ) {
						Shapes.add((Shape) OInput.readObject());
					}
					for (int j = 0; j < no_of_shapes; ++j) {
						ShapeColors.add((Color) OInput.readObject());
					}

				}  catch (Exception err) {
					JOptionPane.showMessageDialog(jf, "Incorrect File");
				} 
				
				try {
					OInput.close();
					FInput.close();
				} catch (IOException err) {
					System.out.println("Load Game Error!");
				}
			}
			MyCanvas.adddata(Shapes, ShapeColors);
			jf.repaint();
			
		}
		
	}
	/**
	 * An Inner Class handling the event when Export Button is clicked to save the drawn shapes in 
	 * a text file with a .txt extension.
	 * @author Suyash Lohia 
	 * @version 1.0
	 */
	class ExportBtnListn implements ActionListener{

		/**
		 * Action performed function implementing the exporting of drawn shapes
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser ChooseFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int check = ChooseFile.showSaveDialog(null);
			String filepath ="";
			if (JFileChooser.CANCEL_OPTION== check) {
				System.exit(1);
			}
			else if (JFileChooser.APPROVE_OPTION==check) {
				File ActualFile = ChooseFile.getSelectedFile();
				filepath= ActualFile.getAbsolutePath().toString();
			}
			
			try {
				FileOutputStream FOut = new FileOutputStream(new File(filepath));
				DataOutputStream DOut = new DataOutputStream(FOut);
				
				no_of_shapes=Shapes.size();
				int shape_index=-1;
				
				for(Shape s: Shapes) {
					shape_index+=1;
					if (s instanceof Line2D.Float) {
						Line2D.Float temp = (Line2D.Float) s;
						DOut.writeBytes("line"+";");
						DOut.writeBytes((temp.getX1())+";");
						DOut.writeBytes((temp.getY1())+";");
						DOut.writeBytes((temp.getX2())+";");
						DOut.writeBytes((temp.getY2())+";");
						DOut.writeBytes(retRed(shape_index));
						DOut.writeBytes(retGreen(shape_index));
						DOut.writeBytes(retBlue(shape_index));
					}
					else if(s instanceof Ellipse2D.Float) {
						Ellipse2D.Float temp= (Ellipse2D.Float) s;
						DOut.writeBytes("circle;");
						DOut.writeBytes((temp.getX())+";");
						DOut.writeBytes((temp.getY())+";");
						DOut.writeBytes(((temp.getHeight())/2)+";");
						DOut.writeBytes(retRed(shape_index));
						DOut.writeBytes(retGreen(shape_index));
						DOut.writeBytes(retBlue(shape_index));
					
					}
					else {
						int x[] = ((Polygon) s).xpoints;
						int y[] = ((Polygon) s).ypoints; 
						if(x.length==3) {
							DOut.writeBytes("triangle;");						
						}
						else {
							DOut.writeBytes("quadrilateral;");
						}
						for (int i=0; i<x.length;i++) {
							DOut.writeBytes((x[i])+";");
							DOut.writeBytes((y[i])+";");
						}
						DOut.writeBytes(retRed(shape_index));
						DOut.writeBytes(retGreen(shape_index));
						DOut.writeBytes(retBlue(shape_index));
					
					}
				}
				
					
			    DOut.close();
				FOut.close();

			}  catch (Exception err) {
				System.out.println("Export Game Error!");
			}
			
		}
		
	}
	/**
	 * A function to return the red component of the colour of the shape with index as paramtere 
	 * @param index, an integer representing the index of the shape in the Shapes Arraylist
	 * @return string, to be printed in txt file containing red component
	 */
	public String retRed(int index) {
		if (ShapeColors.get(index)==null) {
			return "0;";
		}
		else {
			return (String.valueOf(ShapeColors.get(index).getRed()) + ";");
		}
	}
	/**
	 * A function to return the green component of the colour of the shape with index as paramtere 
	 * @param index, an integer representing the index of the shape in the Shapes Arraylist
	 * @return string, to be printed in txt file containing green component
	 */
	public String retGreen(int index) {
		if (ShapeColors.get(index)==null) {
			return "0;";
		}
		else {
			return (String.valueOf(ShapeColors.get(index).getGreen()) + ";");
		}
	}
	/**
	 * A function to return the blue component of the colour of the shape with index as paramtere 
	 * @param index, an integer representing the index of the shape in the Shapes Arraylist
	 * @return string, to be printed in txt file containing blue component
	 */
	public String retBlue(int index) {
		if (ShapeColors.get(index)==null) {
			return "0\n";
		}
		else {
			return (String.valueOf(ShapeColors.get(index).getBlue()) + "\n");
		}
	}
	
	/**
	 * An Inner Class handling the event when Import Button is clicked to import the drawn shapes from 
	 * a text file with a .txt extension.
	 * @author Suyash Lohia 
	 * @version 1.0
	 */
	class ImportBtnListn implements ActionListener{

		/**
		 * Action performed function implementing the importing of drawn shapes
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser ChooseFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int check = ChooseFile.showOpenDialog(null);
			String filepath ="";
			if (JFileChooser.CANCEL_OPTION== check) {
				System.exit(1);
			}
			else if (JFileChooser.APPROVE_OPTION==check) {
				File ActualFile = ChooseFile.getSelectedFile();
				filepath= ActualFile.getAbsolutePath().toString();
			}
			try {
				
				FileInputStream FInput = new FileInputStream(new File(filepath));
				DataInputStream DIn = new DataInputStream(FInput);
				String line;
				String[] obj;
				MyCanvas.removedata();
				Shapes.clear();
				ShapeColors.clear();
				while ((line =DIn.readLine())!= null) {
					obj= line.split(";");					
					boolean flag= true;
					if (obj[obj.length - 1].equals("0") && obj[obj.length - 2].equals("0") && obj[obj.length - 3].equals("0")) {
						flag=false; 
					}
					
					if(obj[0].equals("line")) {
						Shapes.add(new Line2D.Float(Float.parseFloat(obj[1]),Float.parseFloat(obj[2]),Float.parseFloat(obj[3]),Float.parseFloat(obj[4])));
						if(flag)
							ShapeColors.add(new Color(Integer.parseInt(obj[5]),Integer.parseInt(obj[6]),Integer.parseInt(obj[7])));
					}
					else if (obj[0].equals("circle")) {
						Shapes.add(new Ellipse2D.Float(Float.parseFloat(obj[1]),Float.parseFloat(obj[2]) , Float.parseFloat(obj[3])*2, Float.parseFloat(obj[3])*2));
						if(flag)
							ShapeColors.add(new Color(Integer.parseInt(obj[4]),Integer.parseInt(obj[5]),Integer.parseInt(obj[6])));
					}
					else if (obj[0].equals("triangle")) {
						int[] x= {Integer.parseInt(obj[1]),Integer.parseInt(obj[3]),Integer.parseInt(obj[5])};
						int[] y= {Integer.parseInt(obj[2]),Integer.parseInt(obj[4]),Integer.parseInt(obj[6])};
						Shapes.add(new Polygon(x,y,3)); 
						if(flag)
							ShapeColors.add(new Color(Integer.parseInt(obj[7]),Integer.parseInt(obj[8]),Integer.parseInt(obj[9])));
					}
					else if (obj[0].equals("quadrilateral")) {
						int[] x= {Integer.parseInt(obj[1]),Integer.parseInt(obj[3]),Integer.parseInt(obj[5]),Integer.parseInt(obj[7])};
						int[] y= {Integer.parseInt(obj[2]),Integer.parseInt(obj[4]),Integer.parseInt(obj[6]),Integer.parseInt(obj[8])};
						Shapes.add(new Polygon(x,y,4)); 
						if(flag)
							ShapeColors.add(new Color(Integer.parseInt(obj[9]),Integer.parseInt(obj[10]),Integer.parseInt(obj[11])));
					}
					if (flag==false)
						ShapeColors.add(null);
				}
				DIn.close();
				FInput.close();
					

			}  catch (Exception err) {
				System.out.println("Import Error!");
			}
			MyCanvas.adddata(Shapes, ShapeColors);
			jf.repaint();
		}
	}
			
	/**
	 * This method function is responsible for enabling and disabling the buttons on the panel and modifying 
	 * the color accordingly. 
	 * @param flag, a boolean variable stating if the button is to enables or enabled. 
	 * @param button, a button oject which is to be enabled or disabled.
	 */
	public void ActivateButton(boolean flag,JButton button) {
		if(flag) {
			button.setEnabled(true);
			button.setBackground(null); //to set it back to the default color

		}
		else {
			button.setEnabled(false);
			button.setBackground(Color.gray);
		}
	}

	/**
	 *This method function is to enable/disable the object modification buttons using one call. 
	 * @param flag, a boolean variable stating if the buttons are to enables or enabled.
	 */
	public void ShapeEditButtons(boolean flag) {
		if(flag) {
			ActivateButton(true,DeleteBtn);
			ActivateButton(true,RColorBtn);
			ActivateButton(true,CopyBtn);
			ActivateButton(true,MoveBtn);
			
		}
		else {
			
			ActivateButton(false,DeleteBtn);
			ActivateButton(false,RColorBtn);
			ActivateButton(false,CopyBtn);
			ActivateButton(false,MoveBtn);

		}
	}
}