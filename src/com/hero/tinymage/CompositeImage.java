package com.hero.tinymage;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JFrame;

/**
 * Classe fille d'AbstractImage, créée pour les fonctions d'incrustration.
 * @author hero
 *
 */
public class CompositeImage extends AbstractImage {
	
	Image background;
	Image foreground;
	
	CompositeImage(Image i, Image j)
	{
		super(i.getSizeX(), i.getSizeY());
		this.background = i;
		this.foreground = j;
		render();
		
	}

	/**
	 * Le rendu d'une image Composite se fait sur les tableaux des pixels des deux images : si certains sont marqués isRemoved, ils ne sont pas affichés et remplacés par ceux de l'autre image.
	 */
	@Override
	public void render() {
		
		System.out.println("Rendering Composite Image ...");
		System.out.println("Background : " + background.getID() + " " + background.getSizeX());
		System.out.println("Foreground : " + foreground.getID() + " " + foreground.getSizeX());

		Pixel[] backPixels = background.getPixels();
		Pixel[] frontPixels = foreground.getPixels();
		
		int printerX = 0;
		int printerY = 0;
		
		for (int i = 0; i < backPixels.length; i++)
		{
			Pixel p = backPixels[i];
			Pixel pf = frontPixels[i];
			
			if (pf.isRemoved)
			{
				this.getWindow().getLayeredPane().add(p);
			}
			else
			{
				this.getWindow().getLayeredPane().add(pf);
			}
		}
		
		this.getWindow().getLayeredPane().setVisible(true);
		this.getWindow().repaint();
		this.getWindow().validate();
		
		Component[] comps = this.getWindow().getLayeredPane().getComponents();
		Pixel[] pixelArray = new Pixel[this.getSizeX()*this.getSizeY()];
		int counter = 0;
		for (int i = 0; i < comps.length; i++)
		{
			if (comps[i] instanceof Pixel)
			{
				pixelArray[counter] = (Pixel) comps[i];
				counter ++;
			}
		}
		
		this.setPixels(pixelArray);
		writeToSegments();
		System.out.println("Done !");
	}
	
	private void writeToSegments()
	{
		System.out.println("Writing to Segments ...");
		Pixel[] pixs = this.getPixels();
		String fileContent = "";
		
		for (int i = 0; i < pixs.length; i++)
		{
			fileContent += pixs[i].writeToString();
		}
		
		parse(this.getSizeX(), this.getSizeY(), fileContent);
	}
	
	public void parse(int sizeX, int sizeY, String content)
	{
		Value red = new Red();
		Value green = new Green();
		Value blue = new Blue();
		Segment firstSegment = null;
		Segment currentSegment = null;
		
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

				if (((char) reader == ' ' || (char) reader == '\n' || (char) reader == '\r') && (!currentValue.isEmpty()))
				{
					
					System.out.println("Space reached.");
					System.out.println("Current value : "+ currentValue);

					if (!red.isFilled() && !green.isFilled() && !blue.isFilled())
					{
						System.out.println("Setting red value with : "+ currentValue);
						red.setValue(Integer.valueOf(currentValue));
					}
					else if (red.isFilled() && !green.isFilled() && !blue.isFilled())
					{
						System.out.println("Setting green value with : "+ currentValue);
						green.setValue(Integer.valueOf(currentValue));
					}
					else if (red.isFilled() && green.isFilled() && !blue.isFilled())
					{
						System.out.println("Setting blue value with : "+ currentValue);
						blue.setValue(Integer.valueOf(currentValue));
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
					if ((char) reader != '\n' || (char) reader !=' ' || (char) reader != '\r' )
					currentValue+=reader;
				}
				
			}
			
			sr.close();
			this.setFirstSegment(firstSegment);
			System.out.println("Done !");
							
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


}
