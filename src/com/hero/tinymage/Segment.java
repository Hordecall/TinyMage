package com.hero.tinymage;

import java.awt.Color;

/**
 * La classe/liste chainée sur laquelle sont bâties les Images.
 * @author hero
 *
 */
public class Segment {
	
	private static int ID = 0;
	private int pid;
	private int red, green, blue;
	private int occurences;
	private Segment nextSegment;
	
	Segment (int red, int green, int blue, int occurences, Segment nextSegment)
	{
		ID++;
		this.pid = ID;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.occurences = occurences;
		this.nextSegment = nextSegment;
	}
	
	Segment (int red, int green, int blue, int occurences)
	{
		ID++;
		this.pid = ID;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.occurences = occurences;
		this.nextSegment = null;
	}
	
	Segment (int red, int green, int blue)
	{
		ID++;
		this.pid = ID;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.occurences = 1;
		this.nextSegment = null;
	}
	
	/**
	 * Définit le prochain Segment.
	 * @param segment
	 */
	public void setNext(Segment segment)
	{
		this.nextSegment = segment;
	}
	
	/**
	 * Renvoie le prochain Segment.
	 * @return Segment le Segment suivant.
	 */
	public Segment getNext()
	{
		return this.nextSegment;
	}
	
	/**
	 * Renvoie les trois valeurs contenues dans le Segment sous la forme d'une Color.
	 * @return Color, la couleur définie par les valeurs du Segment.
	 */
	public Color getColor()
	{
		return new Color(red,green,blue,255);
	}
	
	/**
	 * Modifie les valeurs des entiers composant la couleur. Appellé par la Toolbox.
	 * @see Toolbox
	 * @param value
	 */
	public void hueModification(int value)
	{
		if (red+value < 255 && red+value > 0)
			red+=value; 
		if (green+value < 255 && green+value > 0)
			green+=value; 
		if (blue+value < 255 && blue+value > 0)
			blue+=value;	
	}
	
	/**
	 * Définit le nombre de répétitions du Segment.
	 * @param int nombre d'occurences.
	 */
	public void setOccurences(int nbr)
	{
		this.occurences = nbr;
	}
	
	/**
	 * Retourne le nombre de répétitions du Segment.
	 * @return int
	 */
	public int getOccurences()
	{
		return this.occurences;
	}
	
	/**
	 * Est-ce que ce segment a une suite ?
	 * @return true si le Segment a une suite.
	 */
	public boolean hasNext()
	{
		return this.nextSegment != null;
	}

	/**
	 * Permet de comparer les couleurs entre deux Segment.
	 * @param Segment seg
	 * @return true si les couleurs ont les mêmes valeurs.
	 */
	public boolean isEqualTo(Segment seg)
	{
		return (this.red == seg.getRed() && this.green == seg.getGreen() && this.blue == seg.getBlue());
	}
	
	public int getRed()
	{
		return this.red;
	}
	
	public int getGreen()
	{
		return this.green;
	}
	
	public int getBlue()
	{
		return this.blue;
	}
	
	public int getID()
	{
		return this.pid;
	}

	/**
	 * Change toutes les valeurs d'un segment à la même valeur.
	 * @param value
	 */
	public void setAllValues(int value) 
	{
		this.red = value;
		this.green = value;
		this.blue = value;
	}
	
	/**
	 * Change toutes les valeurs d'un segment, chacun sa valeur.
	 * @param redValue
	 * @param greenValue
	 * @param blueValue
	 */
	public void setAllValues(int redValue, int greenValue, int blueValue) 
	{
		this.red = redValue;
		this.green = greenValue;
		this.blue = blueValue;
	}
	
	/**
	 * Ecrit le Segment en String, pour la sauvegarde.
	 * @return String 
	 */
	public String writeToString()
	{
		return this.red + " " + this.green + " " + this.blue + "\n";
	}
	
	/**
	 * Ecrit le Segment en Pixel.
	 * @return Pixel[]
	 */
	public Pixel[] writeToPixels()
	{
		Pixel[] pixs = new Pixel[this.getOccurences()];
		for (int i = 0; i < pixs.length; i++)
		{
			pixs[i] = new Pixel (new Color(this.red, this.green, this.blue, 255));
		}	
		return pixs;
	}

}
