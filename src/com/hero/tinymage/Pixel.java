package com.hero.tinymage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * Classe permettant l'affichage des Images dans le logiciel. Etendent JComponent.
 * Le principe est qu'après avoir lu un Segment, on crée un Rectangle de la taille d'un pixel, de la couleur du Segment.
 * J'ai pu faire ça après m'être frotté à AWT et Swing pour mon projet pour la matière NFA019.
 * @author hero
 *
 */
public class Pixel extends JComponent {

	private static int ID = 0;
	private int pid;
	private Rectangle pix;
	private Color color;
	boolean isRemoved;
	
	Pixel(Color color)
	{
		ID++;
		this.color = color;
		this.pix = new Rectangle(1, 1);
		this.pid = ID;
		this.setBounds(0, 0, pix.width, pix.height);
		isRemoved = false;
	}
	
	public void paint (Graphics l)
	{
		Graphics2D k = (Graphics2D) l;
		k.setColor(color);
		k.fill(pix);
	}
	
	/**
	 * Permet la liaison entre le parseur et l'affichage. Ecrit les Pixel en String pour qu'ils puissent être parsés.
	 * @return String
	 */
	public String writeToString()
	{
		String red = String.valueOf(color.getRed());
		String green = String.valueOf(color.getGreen());
		String blue = String.valueOf(color.getBlue());
		
		return red + " " + green + " " + blue + "\n";
	}
	
	public int getID()
	{
		return this.pid;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	public void setColor(Color newCol)
	{
		this.color = newCol;
		this.repaint();
	}
	
	/**
	 * Utilisé par la Toolbox et la fonction remove(), cette fonction permet d'enlever des pixels aux couleurs très proches.
	 * @param otherCol
	 * @return true si la couleur comparée n'a qu'une différence inférieure à 4 avec la couleur d'origine.
	 */
	public boolean isEqualTo(Color otherCol)
	{	
		Color col2 = otherCol;
		Color col1 = this.color;
		
		int col1r = color.getRed();
		int col1g = color.getGreen();
		int col1b = color.getBlue();
		
		int col2r = col2.getRed();
		int col2g = col2.getGreen();
		int col2b = col2.getBlue();
		
		boolean reds = col1r == col2r || (col1r <= col2r+4 && col1r >= col2r-4);
		boolean greens = col1g == col2g ||( col1g <= col2g+4 && col1g >= col2g-4);
		boolean blues = col1b == col2b || (col1b <= col2b+4 && col1b >= col2b-4);
			
		return (reds && greens && blues);
	}
	
	/**
	 * Permet de savoir si le Pixel doit être compté ou pas dans le calcul d'une CompositeImage.
	 * @see CompositeImage
	 * @return true si le Pixel a été marqué comme à enlever.
	 */
	public boolean isRemoved()
	{
		return this.isRemoved;
	}
	
	/**
	 * Permet de définir si le Pixel doit être compté ou pas dans le calcul d'une CompositeImage.
	 * @see CompositeImage
	 * @param true si on ne veut pas afficher ce Pixel.
	 */
	public void isRemoved(boolean state)
	{
		this.isRemoved = state;
	}

	
}
