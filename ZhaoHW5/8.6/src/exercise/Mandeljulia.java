package exercise;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Mandeljulia extends Frame{
	//boolean isMandelbrot = true;
	public static void main(String[] args) {
		new Mandeljulia();
	}
	
	public Mandeljulia() {
		// TODO Auto-generated constructor stub
		super("Drag left mouse button to crop and zoom. " + "Click right mouse button to restore.");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(800, 600);
		add("Center", new CvMandeljulia());
		show();
	}
}

class CvMandeljulia extends Canvas{
	final double minRe0 = -2.0, maxRe0 = 1.0,
			minIm0 = -1.0, maxIm0 = 1.0;
	double minRe = minRe0, maxRe = maxRe0,
			minIm = minIm0, maxIm = maxIm0, factor, r;
	int n, xs, ys, xe, ye, w, h;

	void drawWhiteRectabgle(Graphics g){
		g.drawRect(Math.min(xs, xe), Math.min(ys, ye), Math.abs(xe - xs), Math.abs(ye - ys));
	}
	
	boolean isLeftMouseButton(MouseEvent e){
		return (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0;
	}
	
	public CvMandeljulia() {
		// TODO Auto-generated constructor stub
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (isLeftMouseButton(e)) {
					xs = xe = e.getX();
					ys = ye = e.getY();
				}
				else {
					minRe = minRe0;
					maxRe = maxRe0;
					minIm = minIm0;
					maxIm = maxIm0;
					repaint();
				}
			}
			
			public void mouseReleased(MouseEvent e){
				if (isLeftMouseButton(e)) {
					xe = e.getX();
					ye = e.getY();
					if (xe != xs && ye != ys) {
						int xS = Math.min(xs, xe), xE = Math.max(xs, xe),
								yS = Math.min(ys, ye), yE = Math.max(ys, ye),
								w1 = xE - xS, h1 = yE - yS, a = w1 * h1,
								h2 = (int)Math.sqrt(a/r), w2 = (int)(r * h2),
								dx = (w2 - w1)/2, dy = (h2 - h1)/2;
						xS -= dx; xE += dx;
						yS -= dy; yE += dy;
						maxRe = minRe + factor * xE;
						maxIm = minIm + factor * yE;
						minRe += factor * xS;
						minIm += factor * yS;
						repaint();
					}
				}
			}
			
			public void mouseClicked(MouseEvent e) {
				if (isLeftMouseButton(e)) {
					new Julia((double)e.getX()/getSize().width, (double)e.getY()/getSize().height).setLocation(800, 0);
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e){
				if (isLeftMouseButton(e)) {
					Graphics g = getGraphics();
					g.setXORMode(Color.black);
					g.setColor(Color.white);
					if (xe != xs || ye != ys) {
						drawWhiteRectabgle(g);
					}
					xe = e.getX();
					ye = e.getY();
					drawWhiteRectabgle(g);
				}
			}
		});
	}
	
	public void paint(Graphics g){
		w = getSize().width;
		h = getSize().height;
		r = w/h;
		factor = Math.max((maxRe - minRe)/w, (maxIm - minIm)/h);
		for (int yPix = 0; yPix < h; ++yPix) {
			double cIm = minIm + yPix * factor;
			for (int xPix = 0; xPix < w; ++xPix) {
				double cRe = minRe + xPix * factor, x = cRe, y = cIm;
				int nMax = 100, n;
				for (n = 0; n < nMax; ++n) {
					double x2 = x * x, y2 = y * y;
					if (x2 + y2 > 4) {
						break;
					}
					y = 2 * x * y + cIm;
					x = x2 - y2 + cRe;
				}
				g.setColor(n == nMax ? Color.black : new Color(100 + 155 * n / nMax, 0, 0));
				g.drawLine(xPix, yPix, xPix, yPix);
			}
		}
	}
}

class Julia extends Frame{
	double eX, eY;
	public Julia(double eX, double eY) {
		// TODO Auto-generated constructor stub
		super("Drag left mouse button to crop and zoom. " + "Click right mouse button to restore.");
		this.eX = eX;
		this.eY = eY;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(900, 500);
		add("Center", new CvJulia(eX, eY));
		show();
	}
}

class CvJulia extends Canvas{
	final double minRe0 = -2.0, maxRe0 = 1.0,
			minIm0 = -1.0, maxIm0 = 1.0;
	double minRe = minRe0, maxRe = maxRe0,
			minIm = minIm0, maxIm = maxIm0, factor, r;
	int n, xs, ys, xe, ye, w, h;
	boolean isMandelbrot;
	double eX, eY;
	
	void drawWhiteRectabgle(Graphics g){
		g.drawRect(Math.min(xs, xe), Math.min(ys, ye), Math.abs(xe - xs), Math.abs(ye - ys));
	}
	
	boolean isLeftMouseButton(MouseEvent e){
		return (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0;
	}
	
	public double geteX() {
		return eX;
	}

	public void seteX(double eX) {
		this.eX = eX;
	}

	public double geteY() {
		return eY;
	}

	public void seteY(double eY) {
		this.eY = eY;
	}

	public CvJulia(double eX, double eY) {
		this.eX = eX;
		this.eY = eY;
		// TODO Auto-generated constructor stub
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (isLeftMouseButton(e)) {
					xs = xe = e.getX();
					ys = ye = e.getY();
				}
				else {
					minRe = minRe0;
					maxRe = maxRe0;
					minIm = minIm0;
					maxIm = maxIm0;
					repaint();
				}
			}
			
			public void mouseReleased(MouseEvent e){
				if (isLeftMouseButton(e)) {
					xe = e.getX();
					ye = e.getY();
					if (xe != xs && ye != ys) {
						int xS = Math.min(xs, xe), xE = Math.max(xs, xe),
								yS = Math.min(ys, ye), yE = Math.max(ys, ye),
								w1 = xE - xS, h1 = yE - yS, a = w1 * h1,
								h2 = (int)Math.sqrt(a/r), w2 = (int)(r * h2),
								dx = (w2 - w1)/2, dy = (h2 - h1)/2;
						xS -= dx; xE += dx;
						yS -= dy; yE += dy;
						maxRe = minRe + factor * xE;
						maxIm = minIm + factor * yE;
						minRe += factor * xS;
						minIm += factor * yS;
						repaint();
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e){
				if (isLeftMouseButton(e)) {
					Graphics g = getGraphics();
					g.setXORMode(Color.black);
					g.setColor(Color.white);
					if (xe != xs || ye != ys) {
						drawWhiteRectabgle(g);
					}
					xe = e.getX();
					ye = e.getY();
					drawWhiteRectabgle(g);
				}
			}
		});
	}
	
	
	public void paint(Graphics g){
		Dimension d = getSize();
		w = d.width;
		h = d.height;
		r = w/h;
		double cRe = -2 + this.geteX() * 3, cIm = 1 - this.geteY() * 2;
		System.out.println(cRe);
		System.out.println(cIm);
		factor = Math.max((maxRe - minRe)/w, (maxIm - minIm)/h);
		for (int yPix = 0; yPix < h; ++yPix) {
			for (int xPix = 0; xPix < w; ++xPix) {
				double x = minRe + xPix * factor, 
						y = minIm + yPix * factor;
				int nMax = 100, n;
				for (n = 0; n < nMax; ++n) {
					double x2 = x * x, y2 = y * y;
					if (x2 + y2 > 4) {
						break;
					}
					y = 2 * x * y + cIm;
					x = x2 - y2 + cRe;
				}
				g.setColor(n == nMax ? Color.black : new Color(100 + 155 * n / nMax, 0, 0));
				g.drawLine(xPix, yPix, xPix, yPix);
			}
			
		}
		g.setColor(Color.yellow);
		g.drawString("z =" + cRe + "x + " + cIm + "yi", 20, 20);
	}
}
