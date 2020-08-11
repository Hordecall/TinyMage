package com.hero.tinymage;


import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Classe abstraite des images, qui contiennent toutes une identification, une fenêtre JFrame, une boite à outils Toolbox, et un tableau de Pixel.
 * @author hero
 *
 */
public abstract class AbstractImage extends Collector implements Image, WindowListener, ComponentListener {

	private static int ID = 0;
	private int pid;
	private int sizeX;
	private int sizeY;
	private int maxColorValue;
	private JFrame window;
	private Segment firstSegment;
	private Toolbox toolbox;
	private Pixel[] pixels;
	private boolean hasBeenRendered;
	
	AbstractImage (int sizeX, int sizeY, Segment firstSegment)
	{
		ID++;
		this.pid = ID;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.firstSegment = firstSegment;
		this.maxColorValue = 255;
		
		this.window = new JFrame("#"+pid);		
		this.window.setDefaultCloseOperation(2);
		this.window.setSize(this.sizeX*2, this.sizeY*2);
		this.window.validate();
		this.window.setVisible(true);
		
		render();
		
		this.hasBeenRendered = true;
		this.toolbox = new Toolbox(this);
		this.window.addWindowListener(this);
		this.window.addComponentListener(this);
		
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i].addMouseListener(toolbox);
		}


	}
	
	AbstractImage (int sizeX, int sizeY, int maxColorValue, Segment firstSegment)
	{
		ID++;
		this.pid = ID;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.firstSegment = firstSegment;
		this.maxColorValue = maxColorValue;
		
		this.window = new JFrame("#"+pid);	
		this.window.setDefaultCloseOperation(2);

		this.window.setSize(this.sizeX*2, this.sizeY*2);
		this.window.validate();
		this.window.setVisible(true);
		
		render();
		
		this.hasBeenRendered = true;
		this.toolbox = new Toolbox(this);
		this.window.addWindowListener(this);
		this.window.addComponentListener(this);
		
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i].addMouseListener(toolbox);
		}


	}

	/**
	 * Ce constructeur sert principalement à l'ergonomie de l'interface utilisateur, pour éviter de placer la fenêtre sur une autre à sa création.
	 * Avec plus ou moins de succès.
	 * @param sizeX2
	 * @param sizeY2
	 * @param first
	 * @param maxColorVal
	 * @param startLocX
	 * @param startLocY
	 */
	AbstractImage(int sizeX2, int sizeY2, Segment first, int maxColorVal, int startLocX, int startLocY) {
		
		ID++;
		this.pid = ID;
		this.sizeX = sizeX2;
		this.sizeY = sizeY2;
		this.firstSegment = first;
		this.maxColorValue = 255;
		
		this.window = new JFrame("#"+pid);		
		this.window.setDefaultCloseOperation(2);

		this.window.setSize(this.sizeX*2, this.sizeY*2);
		this.window.validate();
		this.window.setVisible(true);
		
		render();
		
		this.hasBeenRendered = true;
		this.toolbox = new Toolbox(this);
		this.window.addWindowListener(this);
		this.window.addComponentListener(this);
		
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i].addMouseListener(toolbox);
		}


	}
	
	/**
	 * Ce constructeur sert pour les CompositeImage, qui n'ont pas encore de Segment ou de tableau de Pixel.
	 * @param sizeX
	 * @param sizeY
	 */
	AbstractImage(int sizeX, int sizeY)
	{
		ID++;
		this.pid = ID;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.maxColorValue = 255;
		
		this.window = new JFrame("#"+pid);		
		this.window.setDefaultCloseOperation(2);

		this.window.setSize(this.sizeX*2, this.sizeY*2);
		this.window.validate();
		this.window.setVisible(true);
		this.window.addWindowListener(this);
		this.window.addComponentListener(this);
		
		this.toolbox = new Toolbox(this);
		
	}
	
	public Segment getFirstSegment()
	{
		return this.firstSegment;
	}
	
	public void setFirstSegment(Segment newFirst)
	{
		this.firstSegment = newFirst;
	}
	
	public JFrame getWindow()
	{
		return this.window;
	}
	
	public int getSizeX()
	{
		return this.sizeX;
	}
	
	public int getSizeY()
	{
		return this.sizeY;
	}
	
	public int getID()
	{
		return this.pid;
	}
	
	public int getMaxColorValue() {
		return this.maxColorValue;
	}
	
	public Pixel[] getPixels()
	{
		return this.pixels;
	}

	public void setPixels(Pixel[] pixels2) 
	{
		this.pixels = pixels2;
	}
	
	public void hasBeenRendered(boolean state)
	{
		this.hasBeenRendered = state;
	}
	
	public boolean hasBeenRendered()
	{
		return this.hasBeenRendered;
	}
	
	public void addToCollection(Image i)
	{
		try {
			super.addToCollection(i);
		} catch (TerribleMistake e) {
			e.printStackTrace();
		}
	}
	
	public static String recursiveWriteToString(int counter, String str, Pixel[] pix){
		
		if (counter != pix.length)
		{
			System.out.println("c:"+counter+";");
			str+=pix[counter].writeToString();
			return recursiveWriteToString(counter+1, str, pix);
		}
		else
			return str;
		
	}
	
	/**
	 * Lorsqu'on ferme l'image, ferme toutes les fenêtres associées.
	 */
	@Override
	public void windowClosed(WindowEvent e)
	{
		JFrame[] allRelatedWindows = toolbox.getAllWindows();
		
		for (int i = 0; i < allRelatedWindows.length; i++)
		{
			allRelatedWindows[i].dispose();
		}
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {

		JFrame[] allFrames = toolbox.getAllWindows();
		Point p = window.getLocation();
		int width = window.getWidth();
		
		for (int i = 0; i < allFrames.length; i++)
		{
			JFrame frame = allFrames[i];
			String title = frame.getTitle();
			
			if (title.contains("Boite"))
			{
				frame.setLocation(p.x+width, p.y);
			}
			else if (title.contains("Cropper"))
			{
				frame.setLocation(p.x+width, p.y+300);
			}
			else if (title.contains("Sauvegarde"))
			{
				frame.setLocation(p.x+width, p.y+600);
			}
			else if (title.contains("Couleur"))
			{
				frame.setLocation(p.x+width+200, p.y);
			}
			else if (title.contains("Formes"))
			{
				frame.setLocation(p.x+width+200, p.y+50);
			}
				
					
		}
		
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	
	


}
