package com.hero.tinymage;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JFrame;

/**
 * Classe fille d'AbstractImage, pour les images PPM importées ou crées par la Toolbox.
 * @author hero
 *
 */
public class PPMImage extends AbstractImage {

	PPMImage(int sizeX, int sizeY, int maxColorVal, Segment first)
	{
		super(sizeX, sizeY, maxColorVal, first);		
	}
	
	PPMImage (int sizeX, int sizeY, Segment first, int maxColorVal, int startLocX, int startLocY)
	{
		super(sizeX, sizeY, first, maxColorVal, startLocX, startLocY);
	}
	
	PPMImage (int sizeX, int sizeY)
	{
		super(sizeX, sizeY);
	}

	/**
	 * Pour les PPMImage, si elles sont importées, elles n'ont pas encore de tableau de Pixels.
	 * Celui ci est créé à la fin du rendu.
	 */
	public void render()
	{
		System.out.println("Rendering image ...");
		Segment starter = super.getFirstSegment();
		int printerX = 0;
		int printerY = 0;
		Pixel[] pixels = new Pixel[this.getSizeX()*this.getSizeY()];
		int counter = 0;
		
		while (starter != null)
		{	
			int red = starter.getRed();
			int green = starter.getGreen();
			int blue = starter.getBlue();
			int occ = starter.getOccurences();
			
			for (int i = 0; i < occ; i++)
			{
				Pixel pix = new Pixel(new Color (red, green, blue, 255));
				super.getWindow().getLayeredPane().add(pix);
//				this.window.getLayeredPane().add(pix);
				pix.setLocation(printerX, printerY);
				pix.validate();
				pix.setVisible(true);
				pixels[counter] = pix;
				counter++;
				printerX ++;
				if (printerX == super.getSizeX())
				{
						printerY++;
						printerX = 0;
				}
			}
			
			starter = starter.getNext();
		}
		
		this.setPixels(pixels);
		
		JFrame frame = super.getWindow();
		frame.getLayeredPane().validate();
		frame.getLayeredPane().repaint();
		frame.getLayeredPane().setVisible(true);
		
		System.out.println("Done !");
		
		if (!this.hasBeenRendered())
		{
			addToCollection(this);
			hasBeenRendered(true);
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
	
}
