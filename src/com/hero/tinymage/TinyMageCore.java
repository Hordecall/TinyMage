package com.hero.tinymage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * La classe principale du programme. Elle permet majoritairement d'importer des fichiers.
 * Elle étend également le Collector auquel elle ajoute les fichiers importés.
 * @see Collector
 * @see parseFile();
 * @see parseImage();
 * @author hero
 *
 */
public class TinyMageCore extends Collector {

	private JFrame window;
	private JButton importer;
	private JButton create;
	
	TinyMageCore(){
			
		window = new JFrame("TinyMage !");
		window.setSize(200, 80);
		window.setDefaultCloseOperation(3);
		
		importer = new JButton("Importer");
		importer.addMouseListener(this);
		create = new JButton("Créer");
		create.addMouseListener(this);
				
		window.add(importer, BorderLayout.NORTH);
		window.add(create, BorderLayout.SOUTH);
		window.setVisible(true);
				
	}
	

	@Override
	public void importFile() throws IOException {

		 JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Portable PixMap File", "ppm");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this.window);
		    
		    if(returnVal == JFileChooser.APPROVE_OPTION) 
		    {
		       System.out.println("You chose to open this file: " +
		            chooser.getSelectedFile().getName());
		       File importedFile = chooser.getSelectedFile();
		       this.parseFile(importedFile);
		    }
	}
	
	/**
	 * Cette méthode s'occupe de découper la première partie du fichier, à savoir les quatre premières lignes des fichiers PPM.
	 * Elle récupère de ça la taille de l'image et la valeur maximale des pixels; qu'elle envoie à parseImage().
	 * @see parseImage()
	 * @param file
	 */
	private void parseFile(File file)
	{	
		System.out.println("Parsing file. This may take some time (~ 2 minutes).");
		
		String fileContent = "";
		FileInputStream fileInputStream;
		int reader;
		try {
			fileInputStream = new FileInputStream(file);
			reader = fileInputStream.read();
			
			int lineCount = 0;
			
			String imageSizeX = "";
			String imageSizeY = "";
			String maxColorValue = "";
			boolean xy = false;
				
				while (reader != -1)
				{
					reader = fileInputStream.read();
					
					if ((char) reader == '\n')
					{
						lineCount++;
					}
					
					else if (lineCount == 2)
					{
						if ((char) reader == ' ')
						{
							xy = true;
						}
						else if (!xy)
							imageSizeX += (char) reader;
						else
							imageSizeY += (char) reader;
					}
					
					else if (lineCount == 3)
					{
						maxColorValue +=(char) reader;
					}
					
					if (lineCount >= 4)
					{
						fileContent += (char) reader;
					}
					
				}
				
			System.out.println("Done !");
			System.out.println("Image size : " + imageSizeX + " x " +imageSizeY);
			System.out.println("Image max color value : " + maxColorValue);
			fileInputStream.close();
			
			if (fileContent.startsWith(" ")|| fileContent.startsWith("\n"))
			{
				fileContent = fileContent.substring(1);
			}
			
			parseImage(Integer.valueOf(imageSizeX), Integer.valueOf(imageSizeY), fileContent, Integer.valueOf(maxColorValue));
					
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier n'a pas été trouvé.");
		} catch (IOException e) {
			System.out.println("Une erreur est survenue dans l'importation du fichier.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Le fichier est mal composé.");
		}
		
		
	}
	
	/**
	 * Une des méthodes les plus complexes de ce projet, le parseur ! Lorsque j'ai reçu l'information qu'on avait pas le droit d'utiliser les collections, j'ai du refaire le projet.
	 * Assoiffé de vengeance, j'ai décidé de respecter l'énoncé au plus strict, qui recommande de ne pas utiliser de tableau ! J'ai donc composé cette méthode sans .split().
	 * Le reader lit le contenu de l'image déjà édité par parseFile(), caractère par caractère. Il remplit au fur et à mesure des Value (valeurs), et change de Value lorsqu'il détecte un espace ou un retour ligne.
	 * Une fois que trois Value ont été remplies, il en fait un Segment, qu'il enchaine avec un autre si ce n'est pas le premier.
	 * @see Value
	 * @see Segment
	 * @param sizeX
	 * @param sizeY
	 * @param content
	 * @param maxColor
	 */
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
			
			Image newImage = new PPMImage(sizeX, sizeY, firstSegment, maxColor, (int)this.window.getLocation().getX() + this.window.getWidth() ,(int) this.window.getLocation().getY());
					
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


	/**
	 * Envoie l'image au Collecteur.
	 * @see Collector
	 */
	public void addToCollection(Image newImage) throws TerribleMistake {

			super.addToCollection(newImage);
		
	}





}
