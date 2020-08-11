package com.hero.tinymage;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JFrame;

/**
 * Créée pour tester, parce que j'aime bien l'aléatoirité.
 * Crée une image aléatoire de 64x64 pixels. Qui sait ?
 * @author hero
 *
 */
public class RandomImage extends AbstractImage{
	
	private String content;
	

	RandomImage(int sizeX, int sizeY)
	{
		super(sizeX, sizeY);
		
		content = generateRandomContent();
		
		parseImage(sizeX, sizeY, content, 255);
		render();
	}


	private String generateRandomContent() {

		String s = "";
		
		for (int i = 0; i < this.getSizeX()*this.getSizeY(); i++)
		{
			s+= (int)(Math.random()*255) + " " + (int)(Math.random()*255) + " " + (int)(Math.random()*255)+ " ";
		}
		
		return s;
		
	}


	private void parseImage(int sizeX, int sizeY, String content, int maxColor)
	{
		Value red = new Red();
		Value green = new Green();
		Value blue = new Blue();
		Segment firstSegment = null;
		Segment currentSegment = null;
		int maxColorValue = maxColor;
		int factor = 255/maxColorValue;		
		
		System.out.println("Parsing image ...");
		
		try {		
		
			StringReader sr = new StringReader(content);
			char reader = 0;
			String currentValue = "";
						
			while (reader != (char) -1)
			{
				reader = (char) sr.read();
				
				System.out.println("Reader : " + reader);
				currentValue = currentValue.replace(" ", "");
				currentValue = currentValue.trim();

				if (((char) reader == ' ' || (char) reader == '\n' || (char) reader == '\r' ) && (!currentValue.isEmpty()))
				{
					
					System.out.println("Space reached.");
					System.out.println("Current value : "+ currentValue);

					if (!red.isFilled() && !green.isFilled() && !blue.isFilled())
					{
						System.out.println("Setting red value with : "+ currentValue);
						red.setValue(Integer.valueOf(currentValue)*factor);
					}
					else if (red.isFilled() && !green.isFilled() && !blue.isFilled())
					{
						System.out.println("Setting green value with : "+ currentValue);
						green.setValue(Integer.valueOf(currentValue)*factor);
					}
					else if (red.isFilled() && green.isFilled() && !blue.isFilled())
					{
						System.out.println("Setting blue value with : "+ currentValue);
						blue.setValue(Integer.valueOf(currentValue)*factor);
					}					
					
					if (red.isFilled() && green.isFilled() && blue.isFilled())
					{
						System.out.println("All values filled, segment is ready.");
						
						try {
							if (firstSegment == null)
							{
								System.out.println("First segment is null.");
								firstSegment = new Segment(red.getValue(), green.getValue(), blue.getValue());
								System.out.println("First segment created. Segment ID : " + firstSegment.getID());
							}
							else if (firstSegment != null && currentSegment == null)
							{			
								System.out.println("First segment is filled. Creating new segment ...");
								currentSegment = new Segment(red.getValue(), green.getValue(), blue.getValue());
								if (firstSegment.isEqualTo(currentSegment))
								{
									System.out.println("New segment is equal to first one. Updating occurences ...");
									firstSegment.setOccurences(firstSegment.getOccurences()+1);
									currentSegment = null;
								}
								else
								{
									firstSegment.setNext(currentSegment);
									System.out.println("New segment linked. Segment id : " + currentSegment.getID());
								}
							}
							else
							{
								System.out.println("Chaining new segment ...");
								Segment nextSegment = new Segment(red.getValue(), green.getValue(), blue.getValue());
								if (currentSegment.isEqualTo(nextSegment))
								{
									System.out.println("New segment is equal to previous one. Updating occurences ...");
									currentSegment.setOccurences(currentSegment.getOccurences()+1);								
								}
								else								
								{
									System.out.println("New segment created with ID "+ nextSegment.getID());
									currentSegment.setNext(nextSegment);
									currentSegment = nextSegment;
								}
							}
						} catch (TerribleMistake e) {
							System.out.println("Erreur de parsing.");
							e.printStackTrace();
						}
						
						red = new Red();
						green = new Green();
						blue = new Blue();
					}
					
					currentValue = "";

				}
				else
				{
					if ((char) reader != '\n' || (char) reader !=' ' || (char) reader != '\r')
					currentValue+=reader;
				}
				
			}
			
			sr.close();
			System.out.println("Done !");
			
			this.setFirstSegment(firstSegment);
								
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier n'a pas été trouvé.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Une erreur d'entrée et de sortie est survenue.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Le fichier est mal composé.");
		} 
	}

	@Override
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
}
