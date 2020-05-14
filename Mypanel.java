import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;

/**
 * The MyPanel Class extends the JPanel and implements the Moustelistener interface. This class provides the
 * user with a panel to draw,color,move and delete the shapes. 
 * @author suyashlohia
 * @version 1.0
 */
public class Mypanel extends JPanel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private ArrayList<Shape> Shapes;
	private ArrayList<Color> ShapeColors;
	private ArrayList<Point> Dot_Points = new ArrayList<Point>();
	private String Command;
	private int Index = -2;
	private JButton VariableButton = null;
	private JButton DeleteBtn = null;
	private JButton CopyBtn =null;
	private JButton RColorBtn = null;
	private JButton SelectBtn = null;
	private int count =0;
	private float x1,y1,x2,y2;
	private int a1,b1,a2,b2;
	private int tx[],ty[],qx[],qy[];

	/**
	 * A Constructor of the MyPanel class initialising the private variables with some values 
	 * as well as adding MouseListener to the canvas. 
	 * @param f, an object of JFrame ,representing the entire frame 
	 * @param s, an arraylist of type Shape containing all Shapes. 
	 * @param c, an arraylist of type Color containing the color of all shapes. 
	 */
	public Mypanel(JFrame f, ArrayList<Shape> s, ArrayList<Color> c) {
//		super();
		this.frame = f;
		this.Shapes = s;
		this.ShapeColors = c;
		Command= new String("");
		tx= new int[3];
		ty=new int[3];
		qx=new int[4];
		qy= new int[4];
		addMouseListener(this);
	}
	
	public void removedata() {
		this.Shapes.clear();
		this.ShapeColors.clear();
	}
	public void adddata(ArrayList<Shape> s, ArrayList<Color> c) {
		this.Shapes= s;
		this.ShapeColors=c;
	}
	/**
	 * A method responsible for drawing all shapes with the specific colors after taking data from 
	 * the shape and colors arraylist. 
	 */
	public void paintComponent (Graphics g) {

		if(!Dot_Points.isEmpty()) {
			for (Point point : Dot_Points) {
	            g.fillOval(point.x-1, point.y-1,2,2);
	        }	
		}
		
		for (Shape s : Shapes) {
				if (Index != Shapes.indexOf(s)) {
					g.setColor(Color.black);
				}
				else {
					g.setColor(Color.green);
				}
				if (s instanceof Polygon) {
					int x [] = ((Polygon) s).xpoints;
					int y [] = ((Polygon) s).ypoints;
					for (int k = 0; k < y.length; k ++) {
						g.drawLine(x[k], y[k], x[(k+1)%y.length], y[(k+1)%y.length]);
					}
					if (null != ShapeColors.get(Shapes.indexOf(s))) {
						g.setColor(ShapeColors.get(Shapes.indexOf(s)));
						g.fillPolygon(x,y,x.length);
					}
				}
				
				if (s instanceof Ellipse2D.Float) {
					a1=(int)((Ellipse2D.Float) s).getX();
					b1=(int)((Ellipse2D.Float) s).getY();
					a2=(int)((Ellipse2D.Float) s).getWidth();
					b2=(int)((Ellipse2D.Float) s).getHeight();
					g.drawOval(a1,b1,a2,b2);
					if (null != ShapeColors.get(Shapes.indexOf(s))) {
						g.setColor(ShapeColors.get(Shapes.indexOf(s)));
						g.fillOval(a1,b1,a2,b2);
					}
				}
				if (s instanceof Line2D.Float) {
					a1= (int) ((Line2D.Float) s).getX1();
					b1= (int) ((Line2D.Float) s).getY1();
					a2= (int) ((Line2D.Float) s).getX2();
					b2= (int) ((Line2D.Float) s).getY2();
					if (Shapes.indexOf(s)!=Index &&  null!= ShapeColors.get(Shapes.indexOf(s))) {
						g.setColor(ShapeColors.get(Shapes.indexOf(s)));
					}
					g.drawLine(a1,b1,a2,b2);				
				}
				
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
	 * A method function responsible for drawing the shapes and adding them to the shapes arraylist. 
	 * @param cmd, a string containing the command representing the task to be performed. 
	 * @param btn, a button which is to be enabled or disabled accordingly. 
	 */
	public void AddShape (String cmd, JButton btn) {
		Command=cmd;
		VariableButton = btn;

//		System.out.println(VariableButton);
	}
	
	/**
	 * A method functioning used to select shapes which are to be then modified. 
	 * @param cmd, a string containing the command representing the task to be performed. 
	 * @param b1, a button which is to be enabled or disabled accordingly. 
	 * @param b2,a button which is to be enabled or disabled accordingly. 
	 * @param b3, a button which is to be enabled or disabled accordingly. 
	 * @param b4, a button which is to be enabled or disabled accordingly. 
	 * @param b5, a button which is to be enabled or disabled accordingly. 
	 */
	public void SelectShape (String cmd, JButton b1, JButton b2, JButton b3, JButton b4, JButton b5) {
		Command = cmd;
		VariableButton = b1;
		DeleteBtn = b2;
		CopyBtn = b3;
		RColorBtn = b4;
		SelectBtn = b5;
	}
	
	/**
	 * A method function for generating a random color and filling the selected shape with it. 
	 */
	public void RColorShape () {
		if (Index >=0) {
			
			int g = (int) (Math.random()*255);
			int b = (int) (Math.random()*255);
			int r = (int) (Math.random()*255);
			Color RColor = new Color(r,g,b);
			ShapeColors.set(Index, RColor);
			Index = -2;
		}
		frame.repaint();
	}
	
	/**
	 * A method function deleting the selected object by removing it from both the 
	 * colors arraylist and shape arraylist. 
	 */
	public void DeleteShape () {
		if (Index >=0) {
			ShapeColors.remove(Index);
			Shapes.remove(Index);
			
		}
		Index = -2;
		frame.repaint();
	}
		 
	/**
	 * A method responsible for moving the selected object as the user instructs. 
	 * @param deltaX, an integer variable containing the distance object is to moved in horizontal direction.
	 * @param deltaY, an integer variable containing the distance object is to moved in vertical direction.
	 */
	public void MoveShape (int deltaX, int deltaY) {
		
		if (Index >=0) {
			Shape OgShape = Shapes.get(Index);
			if (OgShape instanceof Polygon) {
				((Polygon) OgShape).translate(-deltaX,-deltaY);
				Index = -2;
			}			
			if (OgShape instanceof Ellipse2D.Float) {
				x1 = (float) ((Ellipse2D.Float) OgShape).getX();
				y1 = (float) ((Ellipse2D.Float) OgShape).getY();
				x2 = (float) ((Ellipse2D.Float) OgShape).getWidth();
				y2 = (float) ((Ellipse2D.Float) OgShape).getHeight();
				((Ellipse2D.Float) OgShape).setFrame(x1-deltaX, y1-deltaY, x2, y2);
				Index = -2;
			}
			if (OgShape instanceof Line2D.Float) {
				x1= (float) ((Line2D.Float) OgShape).getX1();
				y1= (float) ((Line2D.Float) OgShape).getY1();
				x2= (float) ((Line2D.Float) OgShape).getX2();
				y2= (float) ((Line2D.Float) OgShape).getY2();
				((Line2D.Float) OgShape).setLine(x1-deltaX,y1-deltaY,x2-deltaX,y2-deltaY);
				Index = -2;
			}			
		}
		frame.repaint();
		ActivateButton(true,VariableButton);
		VariableButton = null;		
		
	}
	
	/**
	 * A method responsilble for copying the selecting object and creating a clone of it. 
	 */
	public void CopyShape() {
		if (Index >=0) {
			Color OgColor = ShapeColors.get(Index);
			Shape OgShape = Shapes.get(Index);

			
			if (OgShape instanceof Polygon) {				
				int x[] = ((Polygon) OgShape).xpoints;
				int y[] = ((Polygon) OgShape).ypoints;     
			    int[] tempx = new int[x.length];
			    System.arraycopy(x, 0, tempx, 0, tempx.length);
				int[] tempy =new int[y.length];
				System.arraycopy(y, 0, tempy, 0, tempy.length);
				((Polygon) OgShape).translate(+25,+25);
				Shapes.add(new Polygon(tempx, tempy, tempx.length));
				Index = -2;
			}
			if (OgShape instanceof Ellipse2D.Float) {
				Ellipse2D.Float NewC = new Ellipse2D.Float();
				x1 = (float) ((Ellipse2D.Float) OgShape).getX();
				y1 = (float) ((Ellipse2D.Float) OgShape).getY();
				x2 = (float) ((Ellipse2D.Float) OgShape).getWidth();
				y2 = (float) ((Ellipse2D.Float) OgShape).getHeight();
				NewC.setFrame(x1+25, y1+25, x2, y2);
				Shapes.add(NewC);
				Index = -2;
			}
			if (OgShape instanceof Line2D.Float) {
				Line2D.Float NewL = (Line2D.Float)((Line2D.Float) OgShape).clone();
				x1= (float) NewL.getX1();
				x2= (float) NewL.getX2();
				y1= (float) NewL.getY1();
				y2= (float) NewL.getY2();
				NewL.setLine(x1+10,y1+10,x2+10,y2+10);
				Shapes.add(NewL);
				Index = -2;
			}	
			ShapeColors.add(OgColor);
			frame.repaint();
		}
	}
		
	/**
	 *MouseClicked Listener which runs whenever a mouse is clicked/pressed once. 
	 */
	public void mouseClicked(MouseEvent e) {
		
		if (Command.equals("line")) {
			
			if (count == 0) {
				x2 = e.getX();
				y2 = e.getY();
				Dot_Points.add(new Point(e.getX(),e.getY()));
				frame.repaint();
				count++;
			}
			else if (count==1){
				x1 = x2;
				y1 = y2;
				x2 = e.getX();
				y2 = e.getY();
				count=0;
				Shapes.add(new Line2D.Float(x1,y1,x2,y2));
				ShapeColors.add(null);
				ActivateButton(true,VariableButton);
				Dot_Points.clear();
				frame.repaint();
			}
		}
		
		if (Command.equals("circle")) {
			if (count == 0) {
				x2 = e.getX();
				y2 = e.getY();
				Dot_Points.add(new Point(e.getX(),e.getY()));
				frame.repaint();
				count++;
			}
			else if (count==1){
				x1 = x2;
				y1 = y2;
				x2 = e.getX();
				y2 = e.getY();
				count=0;
				int xsq = (int)Math.pow(x2 - x1, 2);
				int ysq = (int)Math.pow(y2 - y1, 2);
				int radius = (int)Math.pow(xsq + ysq,0.5);
				Shapes.add(new Ellipse2D.Float(x1 - radius, y1-radius, radius*2, radius*2));
				ShapeColors.add(null);
				ActivateButton(true,VariableButton);
				Dot_Points.clear();
				frame.repaint();
			}
		}
		
		if (Command.equals("triangle")) {
			
			if (count == 0) {
				tx[0] = e.getX();
				ty[0] = e.getY();
				Dot_Points.add(new Point(e.getX(),e.getY()));
				frame.repaint();
				count++;

			}
			else if (count==1){

				tx[1] = e.getX();
				ty[1] = e.getY();
				Dot_Points.add(new Point(e.getX(),e.getY()));
				frame.repaint();
				count++;
			}
			else if (count==2){
				tx[2] = e.getX();
				ty[2] = e.getY();
				count=0;
				Shapes.add(new Polygon(tx,ty,3)); 
				ShapeColors.add(null);
				ActivateButton(true,VariableButton);
				Dot_Points.clear();
				frame.repaint();
			}			
		}	
		if (Command.equals("quad")) {
			
			if (count == 0) {
				qx[0] = e.getX();
				qy[0] = e.getY();
				Dot_Points.add(new Point(e.getX(),e.getY()));
				frame.repaint();
				count++;
			}
			else if (count==1){

				qx[1] = e.getX();
				qy[1] = e.getY();
				Dot_Points.add(new Point(e.getX(),e.getY()));
				frame.repaint();
				count++;
			}
			else if (count==2){

				qx[2] = e.getX();
				qy[2] = e.getY();
				Dot_Points.add(new Point(e.getX(),e.getY()));
				frame.repaint();
				count++;
			}
			else if (count==3){
				qx[3] = e.getX();
				qy[3] = e.getY();
				count=0;
				Shapes.add(new Polygon(qx,qy,4)); 
				ShapeColors.add(null);
				ActivateButton(true,VariableButton);
				Dot_Points.clear();
				frame.repaint();
			}			
		}
		
		if (Command.equals("select")) {
			int x = e.getX();
			int y = e.getY();

			for (Shape s : Shapes) {
				if (s.getBounds2D().contains(x,y)) {
					Index = Shapes.indexOf(s);
					frame.repaint();
					break;
				}
			}
			if (Index <0) {
				ActivateButton(true,VariableButton);					
			}
			else {
				ActivateButton(true,VariableButton);
				ActivateButton(true,DeleteBtn);
				ActivateButton(true,CopyBtn);
				ActivateButton(true,RColorBtn);
			}

		}
		
	}

	/**
	 * MousePressed Listener which runs whenever the mouse is pressed and moved. It's function is 
	 * to track position for moving the objects. 
	 */
	public void mousePressed(MouseEvent e) {
		if (Command.equals("move")) {
			x1 = e.getX();
			y1 = e.getY();
		}	
	}

	/**
	 * MouseRelease Listener which runs whenever mouse is released. It's function is 
	 * to track position for moving the objects. 
	 */
	public void mouseReleased(MouseEvent e) {
		if (Command.equals("move")) {
			x2 = e.getX();
			y2 = e.getY();
			Command="";			
			int deltaX= (int)(x1-x2);
			int deltaY= (int)(y1-y2);
			MoveShape(deltaX, deltaY);
		}
	}
	
	/**
	 * An empty mouselistener added because of package import and implemention
	 * @param e, dummy parametre. 
	 */
	public void mouseDragged(MouseEvent e) {}

	/**
	 * An empty mouselistener added because of package import and implemention
	 * @param e, dummy parametre. 
	 */
	public void mouseMoved(MouseEvent e) {}
	/**
	 * An empty mouselistener added because of package import and implemention
	 * @param e, dummy parametre. 
	 */
	public void mouseEntered(MouseEvent arg0) {}

	/**
	 * An empty mouselistener added because of package import and implemention
	 * @param e, dummy parametre. 
	 */
	public void mouseExited(MouseEvent arg0) {}
}


