package exercise;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FractalGrammars extends Frame{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "txt/Tree.txt";
		new FractalGrammars(fileName);
	}

	public FractalGrammars(String fileName) {
		// TODO Auto-generated constructor stub
		super("Click left or right mouse button to change the level");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setSize(800, 600);
		add("Center", new CvFractalGrammars(fileName));
		show();
	}
}

class CvFractalGrammars extends Canvas{
	String fileName, axiom, strF, strf, strX, strY;
	int maxX, maxY, level = 1;
	double xLast, yLast, dir, rotation, dirStart, fxStart, fyStart,
		lengthFract, reductFact;
	
	void error(String str){
		System.out.println(str);
		System.exit(1);
	}
	
	CvFractalGrammars(String fileName) {
		// TODO Auto-generated constructor stub
		Input inp = new Input(fileName);
		if (inp.fails()) {
			error("Cannot open input file.");
		}
		axiom = inp.readString(); inp.skipRest();
		strF = inp.readString(); inp.skipRest();
		strf = inp.readString(); inp.skipRest();
		strX = inp.readString(); inp.skipRest();
		strY = inp.readString(); inp.skipRest();
		rotation = inp.readFloat(); inp.skipRest();
		dirStart = inp.readFloat(); inp.skipRest();
		fxStart = inp.readFloat(); inp.skipRest();
		fyStart = inp.readFloat(); inp.skipRest();
		lengthFract = inp.readFloat(); inp.skipRest();
		reductFact = inp.readFloat();
		if (inp.fails()) {
			error("Input file incorrect.");
		}
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					level--;
					if (level < 1) {
						level = 1;
					}
				}
				else {
					level++;
				}
				repaint();
			}
		});
	}
	
	Graphics g;
	
	int iX(double x){
		return (int)Math.round(x);
	}
	
	int iY(double y){
		return (int)Math.round(maxY - y);
	}
	
	void drawTo(Graphics g, double x, double y, double blod){
		double slope = (y - yLast) / (x - xLast), phi = Math.atan(slope), deduction;
		phi += (Math.random() - 0.5) / 6;
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke((float)(2)));
		/*
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke((float)(getBlodLast())));
		g2.drawLine(iX(xLast), iY(yLast), iX(x), iY(y));
		if(blodLast-0.1<0){
			setBlodLast(0);
		}else {
			setBlodLast(blodLast-0.1);
		}
		*/
		if (slope >= -1 && slope <= 1) {
			//deduction = 0.125 / Math.cos(phi);
			for (double x0 = xLast; x0 <= x; x0 += 2.2) {
				double y0 = yLast + (x0 - xLast) * Math.tan(phi),
						x1 = x0 - blod * Math.sin(phi) / 3,
						y1 = y0 + blod * Math.cos(phi) / 3,
						x2 = x0 + blod * Math.sin(phi) / 3,
						y2 = y0 - blod * Math.cos(phi) / 3;
				g2.drawLine(iX(x1), iY(y1), iX(x2), iY(y2));
				//blod -= 0.125 * deduction;
			}
		}else {
			//deduction = 0.125 / Math.sin(phi);
			for (double y0 = yLast; y0 <= y; y0 += 2.2) {
				double x0 = xLast + (y0 - yLast) / Math.tan(phi),
						x1 = x0 - blod * Math.sin(phi) / 3,
						y1 = y0 + blod * Math.cos(phi) / 3,
						x2 = x0 + blod * Math.sin(phi) / 3,
						y2 = y0 - blod * Math.cos(phi) / 3;
				g2.drawLine(iX(x1), iY(y1), iX(x2), iY(y2));
				//blod -= 0.125 * deduction;
			}
		}
		xLast = x;
		yLast = y;
	}
	
	void drawLeaf(Graphics g, double x, double y){
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke((float)(3)));
		g2.setColor(Color.green);
		g2.drawLine(iX(xLast), iY(yLast), iX(x), iY(y));
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke((float)(2)));
		//g.drawOval(iX(xLast), iY(yLast), 2, 2);
		//g.fillOval(iX(x), iY(y), 2, 2);
	}
	
	void moveTo(Graphics g, double x, double y){
		xLast = x;
		yLast = y;
	}
	
	public void paint(Graphics g) {
		Dimension d = getSize();
		maxX = d.width - 1;
		maxY = d.height - 1;
		xLast = fxStart * maxX;
		yLast = fyStart * maxY;
		dir = dirStart;
		turtleGraphics(g, axiom, level, lengthFract * maxY, 30);
	}

	public void turtleGraphics(Graphics g, String instruction, int depth, double len, double blod) {
		// TODO Auto-generated method stub
		double xMark = 0, yMark = 0, dirMark = 0, blodMark = blod, blodMarkP = 0;
		for (int i = 0; i < instruction.length(); i++) {
			char ch = instruction.charAt(i);
			//System.out.println(i);
			//System.out.println(ch);
			switch (ch) {
			case 'F':
				if (depth == 0) {
					double rad = Math.PI/180 * dir,
							dx = len * Math.cos(rad), dy = len * Math.sin(rad);
					drawTo(g, xLast + dx, yLast + dy, blod);
					//drawLeaf(g, xLast + dx, yLast + dy);
					if (i == 6) {
						//System.out.println(ch);
						drawLeaf(g, xLast + 5 * Math.cos(rad), yLast + 5 * Math.sin(rad));
						//blodMark = blod;
					}
					//blod -= Math.sqrt(dx*dx+dy*dy) * 0.15;
					if (blod <= 0 && blodMark <= 0) {
						blod = 1;
						blodMark = 1;
					}else {
						blod -= reductFact * len * 0.15;
						blodMark -= 2 * reductFact * len * 0.15;
					}
				}
				else {
					turtleGraphics(g, strF, depth - 1, reductFact * len, blod);
					if (blod <= 0 && blodMark <= 0) {
						blod = 1;
						blodMark = 1;
					}else {
						blod -= reductFact * len * 0.15;
						blodMark -= 2 * reductFact * len * 0.15;
					}
				}
				break;
			case 'f':
				if (depth == 0) {
					double rad = Math.PI/180 * dir,
							dx = len * Math.cos(rad), dy = len * Math.sin(rad);
					moveTo(g, xLast + dx, yLast + dy);
				}
				else {
					turtleGraphics(g, strf, depth - 1, reductFact * len, blod);
				}
				break;
			case 'X':
				if (depth > 0) {
					turtleGraphics(g, strX, depth - 1, reductFact * len, blod);
				}
				break;
			case 'Y':
				if (depth > 0) {
					turtleGraphics(g, strY, depth - 1, reductFact * len, blod);
				}
				break;
			case '+':
				dir -= rotation; blod = blodMark;
				break;
			case '-':
				dir += rotation; blodMark = blod;
				break;
			case '[':
				xMark = xLast; yMark = yLast; dirMark = dir; blodMarkP = blod;
				break;
			case ']':
				xLast = xMark; yLast = yMark; dir = dirMark; blod = blodMarkP;
				break;
			default:
				break;
			}
		}
	}
	
}
