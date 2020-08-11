package com.hero.tinymage;

import javax.swing.JFrame;

/**
 * Interface Image, implémentée par AbstractImage, demandée pour des raisons de sécurité.
 * @author hero
 *
 */
public interface Image {
			
	public void render();
	public int getID();
	
	/**
	 * Renvoie le premier segment de l'Image.
	 * @return Segment premier Segment.
	 */
	public Segment getFirstSegment();
	
	/**
	 * Définit le premier Segment de l'Image.
	 * @param newFirst
	 */
	public void setFirstSegment(Segment newFirst);
	
	/**
	 * Renvoie la fenêtre de l'Image.
	 * @return JFrame
	 */
	public JFrame getWindow();
	
	/**
	 * Renvoie la largeur en pixels de l'Image.
	 * @return int sizeX
	 */
	public int getSizeX();
	
	/**
	 * Renvoie la hauteur en pixels de l'Image.
	 * @return int sizeY;
	 */
	public int getSizeY();
	
	/**
	 * Renvoie la valeur maximale des pixels de l'Image.
	 * @return int maxColorValue
	 */
	public int getMaxColorValue();
	
	/**
	 * Renvoie un tableau contenant tous les Pixel de l'image.
	 * @return
	 */
	public Pixel[] getPixels();
	
	
}
