package com.hero.tinymage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JToolBar.Separator;
import javax.swing.plaf.synth.SynthSeparatorUI;

/**
 * La boite à outils contenant l'entièreté des fonctions demandées.
 * Elle est construite avec une Image dont elle reçoit les propriétés.
 * @author hero
 *
 */
public class Toolbox implements MouseListener, FocusListener {

	private JFrame mainWindow, cutWindow, saveWindow, colorWindow, shapeWindow;
	private JButton darkenRed, lightenRed, darkenGreen, lightenGreen, darkenBlue, lightenBlue, desaturate, invert, sizeX2, sizeY2, size2, displaySize, save, crop, remove, add;
	private JTextArea sizeDisplay, cutLine1, cutLine2, cutRow1, cutRow2, colorR, colorG, colorB, shapeStartPosX, shapeStartPosY, shapeWidth, shapeHeight;
	private ArrayList<JButton> buttons; //je me suis permis ce petit écart pour ajouter rapidement les MouseListener
	private GridLayout mainLayout, cutLayout, colorLayout;
	private AbstractImage parent;
	private Pixel[] imagePixels;
	private int sizeX;
	
	Toolbox(AbstractImage parent)
	{
		System.out.println("Init Toolbox ...");
		this.parent = parent;
		this.sizeX = 200;
		this.mainLayout = new GridLayout(0, 1);
		this.cutLayout = new GridLayout(2, 2);
		this.colorLayout = new GridLayout(1,3);
		this.mainWindow = new JFrame("Boite à outs' #"+parent.getID());
		this.cutWindow = new JFrame("Cropper #" +parent.getID());
		this.saveWindow = new JFrame("Sauvegarde #"+parent.getID());
		this.colorWindow = new JFrame("Couleur #" + parent.getID());
		this.shapeWindow = new JFrame("Formes #" + parent.getID());
		
		this.buttons = new ArrayList<JButton>();

		darkenRed = new JButton("Foncer Rouges");
		lightenRed = new JButton("Eclaircir Rouges");
		darkenGreen = new JButton("Foncer Verts");
		lightenGreen = new JButton("Eclaircir Verts");
		darkenBlue = new JButton("Foncer Bleus");
		lightenBlue = new JButton("Eclaircir Bleus");
		desaturate = new JButton("Noir & Blanc");
		invert = new JButton("Inverser");
		sizeX2 = new JButton("Taille X x2");
		sizeY2 = new JButton("Taille Y x2");
		displaySize = new JButton("Afficher Taille");
		save = new JButton("Sauvegarder ...");
		crop = new JButton("Rogner");
		remove = new JButton("Retirer");
		add = new JButton("Ajouter");
		
		sizeDisplay = new JTextArea();
		sizeDisplay.setEditable(false);
		
		cutLine1 = new JTextArea();
		cutLine1.setText("y1");
		cutLine1.addFocusListener(this);
		cutLine2 = new JTextArea();
		cutLine2.setText("y2");
		cutLine2.addFocusListener(this);
		cutRow1 = new JTextArea();
		cutRow1.setText("x1");
		cutRow1.addFocusListener(this);
		cutRow2 = new JTextArea();
		cutRow2.setText("x2");
		cutRow2.addFocusListener(this);
				
		colorR = new JTextArea(1,3); colorR.setBorder(BorderFactory.createLineBorder(Color.red));
		colorG = new JTextArea(1,3); colorG.setBorder(BorderFactory.createLineBorder(Color.green));
		colorB = new JTextArea(1,3); colorB.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		buttons.add(darkenRed);
		buttons.add(lightenRed);
		buttons.add(darkenGreen);
		buttons.add(lightenGreen);
		buttons.add(darkenBlue);
		buttons.add(lightenBlue);
		buttons.add(desaturate);
		buttons.add(invert);
		buttons.add(displaySize);
		buttons.add(sizeX2);
		buttons.add(sizeY2);
				
		mainWindow.setSize(sizeX, 300);
		mainWindow.setLocation(this.parent.getWindow().getLocation().x+this.parent.getWindow().getWidth(), this.parent.getWindow().getLocation().y);
		mainWindow.setVisible(true);
				
		mainWindow.getLayeredPane().setLayout(mainLayout);
		
		for (int i = 0; i < buttons.size(); i++)
		{
			mainWindow.getLayeredPane().add(buttons.get(i), mainLayout);
			buttons.get(i).addMouseListener(this);
		}
		
		mainWindow.getLayeredPane().add(sizeDisplay);
		
		cutWindow.setSize(sizeX, 300);
		cutWindow.setLocation(mainWindow.getLocation().x, mainWindow.getLocation().y+mainWindow.getHeight());
		cutWindow.getLayeredPane().setLayout(mainLayout);
		cutWindow.getLayeredPane().add(cutRow1); cutWindow.getLayeredPane().add(new Separator());
		cutWindow.getLayeredPane().add(cutRow2); cutWindow.getLayeredPane().add(new Separator());
		cutWindow.getLayeredPane().add(cutLine1); cutWindow.getLayeredPane().add(new Separator());
		cutWindow.getLayeredPane().add(cutLine2); cutWindow.getLayeredPane().add(new Separator());
		cutWindow.getLayeredPane().add(crop);
		crop.addMouseListener(this);
		cutWindow.setVisible(true);
		
		saveWindow.setSize(sizeX, sizeX);
		save.addMouseListener(this);
		saveWindow.add(save, BorderLayout.CENTER);
		saveWindow.setLocation(cutWindow.getLocation().x, cutWindow.getLocation().y+cutWindow.getHeight());
		saveWindow.setVisible(true);
		
		colorWindow.setSize(300,50);
		colorWindow.setLocation(mainWindow.getLocation().x + mainWindow.getWidth(), mainWindow.getLocation().y );
		colorWindow.setLayout(colorLayout);
		colorWindow.add(colorR); colorWindow.add(colorG); colorWindow.add(colorB);
		colorWindow.add(remove); remove.addMouseListener(this);
		colorWindow.setVisible(true);
		
		shapeWindow.setSize(300, 300);
		shapeWindow.setLocation(colorWindow.getLocation().x, colorWindow.getLocation().y + colorWindow.getHeight());
		shapeWindow.add(new JLabel("Position X :"));
		shapeStartPosX = new JTextArea(1,3);
		shapeStartPosY = new JTextArea(1,3);
		shapeHeight = new JTextArea(1,3);
		shapeWidth = new JTextArea(1,3);
		shapeWindow.add(shapeStartPosX);
		shapeWindow.add(new JLabel("Position Y :"));
		shapeWindow.add(shapeStartPosY);
		shapeWindow.add(new JLabel("Hauteur :"));
		shapeWindow.add(shapeHeight);
		shapeWindow.add(new JLabel("Largeur :"));
		shapeWindow.add(shapeWidth);
		shapeWindow.add(add); 
		add.addMouseListener(this);
		shapeWindow.setLayout(new GridLayout(0,1));
		shapeWindow.setVisible(true);
		
		this.imagePixels = this.parent.getPixels();
					
	}
	
	/**
	 * Basée sur la liste chainée, cette fonction permet d'éclaircir ou de foncer les dominantes, mais aussi de rendre l'image en noir et blanc, ou de l'inverser.
	 * @param color String "red", "green", ou "blue" : la dominante choisie
	 * @param action String "lighten", "darken", "desaturate", "invert" : l'action choisie
	 */
	public void colorOperation(String color, String action)
	{
		String colorToEdit = color;
		String actionToExec = action;
		
		System.out.println(action + " "+ colorToEdit);
		
		Segment start = this.parent.getFirstSegment();
		

			while (start != null)
			{
					if (actionToExec == "lighten")
					{
						System.out.print("Lightening ...");
						if (colorToEdit == "red")
						{
							System.out.println(" reds");
							if (start.getRed() > start.getGreen() && start.getRed() > start.getBlue())
							{
								start.hueModification(20);							
							}							
						}
						else if (colorToEdit == "green")
						{
							System.out.println(" greens");
							if (start.getRed() < start.getGreen() && start.getGreen() > start.getBlue())
							{
								start.hueModification(20);							
							}	
						}
						
						else if (colorToEdit == "blue")
						{
							System.out.println(" blues");
							if (start.getBlue() > start.getRed() && start.getBlue() > start.getGreen())
							{
								start.hueModification(20);
							}
						}
					}
					
					else if (actionToExec == "darken")
					{
						System.out.println("Darkening ...");
						if (colorToEdit == "red")
						{
							System.out.println(" reds");
							if (start.getRed() > start.getGreen() && start.getRed() > start.getBlue())
							{
								start.hueModification(-20);							
							}							
						}
						else if (colorToEdit == "green")
						{
							System.out.println(" greens");
							if (start.getRed() < start.getGreen() && start.getGreen() > start.getBlue())
							{
								start.hueModification(-20);							
							}	
						}
						
						else if (colorToEdit == "blue")
						{
							System.out.println(" blues");
							if (start.getBlue() > start.getRed() && start.getBlue() > start.getGreen())
							{
								start.hueModification(-20);
							}
						}
					}
					
					else if (actionToExec == "desaturate")
					{
						int red = start.getRed();
						int green = start.getGreen();
						int blue = start.getBlue();
						int value = (red+green+blue)/3;
						start.setAllValues(value);
					}
					
					else if (actionToExec == "invert")
					{
						int red = 255 - start.getRed();
						int green = 255 - start.getGreen();
						int blue = 255 - start.getBlue();
						start.setAllValues(red, green, blue);
					}
				
				start = start.getNext();			
			}
			System.out.println("Done !");
			
			this.parent.getWindow().getLayeredPane().removeAll();
			this.parent.render();					

	}
	
	/**
	 * Opération de rognage de l'image avec les valeurs entrées par l'utilisateur. Celle-ci est basée sur les pixels et leur localisation dans la fenêtre.
	 * Une fois localisés, la fonction créé une nouvelle image et la parse.
	 * @param line1
	 * @param line2
	 * @param row1
	 * @param row2
	 * @throws TerribleMistake 
	 */
	public void crop(int line1, int line2, int row1, int row2) throws TerribleMistake
	{
		
		System.out.println("Cropping ...");
		
		Component[] components = this.parent.getWindow().getLayeredPane().getComponents();
		
		int x1 = row1; 
		int x2 = row2;
		int y1 = line1; 
		int y2 = line2;
		
		if (x1 > x2)
		{
			throw new TerribleMistake("La valeur X2 doit être supérieure à la valeur X1");
		}
		
		if (y1 > y2)
		{
			throw new TerribleMistake("La valeur Y2 doit être supérieure à la valeur Y1");
		}
		
		int newSizeX = x2-x1;
		int newSizeY = y2-y1;
		
		int nbrOfPixels = newSizeX*newSizeY;
		
		Pixel[] pixels = new Pixel[nbrOfPixels+newSizeX+newSizeY+1];
		
		int counter = 0;
		
		for (int i = 0; i < components.length; i++)
		{
			if (components[i] instanceof Pixel)
			{
				Pixel p = (Pixel) components[i];
				if (p.getLocation().getX()>= x1 && p.getLocation().getX() <= x2 && p.getLocation().getY() >= y1 && p.getLocation().getY() <= y2)
				{
					System.out.println("Added pixel #" + p.getID() + " at " + p.getLocation() + " i : " + counter);
					pixels[counter] = p;
					counter++;
				}
			}
		}
				
		String newImage = "";
		for (int i = 0; i < pixels.length; i++)
		{
			newImage += pixels[i].writeToString();
		}
		
		parse(newSizeX+1, newSizeY+1, newImage);
			
	}
	
	/**
	 * Opération de changement de taille, basée sur le tableau de Pixels de l'image.
	 * @param action
	 */
	private void sizeOperation(String action)
	{
		System.out.println("Editing size ..." + action);
		int sizeX = this.parent.getSizeX();
		int sizeY = this.parent.getSizeY();
		
		if (action.contains("sizeX2"))
		{
			sizeX *=2;
		}
		if (action.contains("sizeY2"))
		{
			sizeY*=2;
		}
		
		Pixel[] newImgPixs = new Pixel[sizeX*sizeY];
		Pixel[] oldImgPixs = this.parent.getPixels();
		
		if (action.contains("sizeX2"))
		{
			int counter = 0;
			for (int i = 0; i < this.parent.getPixels().length; i++)
			{
				newImgPixs[counter] = oldImgPixs[i];
				newImgPixs[counter+1] = oldImgPixs[i];
				counter+=2;
			}
		}
		
		else if (action.contains("sizeY2"))
		{
			int counter3 = 0;
			int lineCount = 0;
					
			for (int i = 0; i < oldImgPixs.length; i ++)
			{
				newImgPixs[i+lineCount] = oldImgPixs[i];
				newImgPixs[i+lineCount+sizeX] = oldImgPixs[i];
				
				counter3++;
				if (counter3 == sizeX)
				{
					lineCount+=sizeX;
					counter3 = 0;
				}
			}
		}
		
		String finalImageStr = "";
		for (int i = 0; i < newImgPixs.length; i++)
		{
			if (newImgPixs[i] != null)
				finalImageStr += newImgPixs[i].writeToString();
		}
		
		System.out.println("Done ! Image size x :" + sizeX + "Image size Y : " + sizeY);
		
		parse(sizeX, sizeY, finalImageStr);

	}

	private void addShape(int startX, int startY, int height, int width, Color col) {
	
		Color shapeCol = col;
		String newFile = "";
		if (shapeCol == null)
		{
			shapeCol = new Color (255,0,0,255);
		}
		
		imagePixels = this.parent.getPixels();
		
		for (int i = 0; i < imagePixels.length; i++)
		{
			int pLocX = (int) imagePixels[i].getLocation().getX();
			int pLocY = (int) imagePixels[i].getLocation().getY();
			
			if (pLocX  >= startX && pLocY >= startY && pLocX <= startX+width && pLocY <= startY+height)
			{
				imagePixels[i].setColor(shapeCol);
			}
			newFile+= imagePixels[i].writeToString();
		}
		
		parse(this.parent.getSizeX(), this.parent.getSizeY(), newFile);
		
	}

	/**
	 * Copie du parser pour faire le lien entre les opérations Segment et les opérations Pixel.
	 * @param sizeX
	 * @param sizeY
	 * @param content String, le contenu de l'image à transformer en Segment.
	 */
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

				if (((char) reader == ' ' || (char) reader == '\n') && (!currentValue.isEmpty()))
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
					if ((char) reader != '\n' || (char) reader !=' ' )
					currentValue+=reader;
				}
				
			}
			
			sr.close();
			System.out.println("Done !");
			
			Image i = new PPMImage(sizeX, sizeY, this.parent.getMaxColorValue(), firstSegment);
			
					
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
	 * Fonction de sauvegarde, basée sur les Segments.
	 */
	private void saveFile() {

		System.out.println("Saving file ...");
		
		String br = System.getProperty("line.separator");
		String fileContent = "P3"+br;
		fileContent += "# CREATOR : TinyMage (a Java student project) by Thomas Bedeau (hello !)"+br;
		fileContent += parent.getSizeX()+" "+parent.getSizeY()+br;
		fileContent += parent.getMaxColorValue()+br;
		

		Segment first = this.parent.getFirstSegment();
		
		while (first != null)
		{
			int occ = first.getOccurences();
			
			for (int i = 0; i < occ; i++)
			{
				fileContent += first.writeToString();
			}
			
			first = first.getNext();
		}
		
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showSaveDialog(saveWindow);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			try {
	            FileWriter fw = new FileWriter(chooser.getSelectedFile()+".ppm");
	            fw.write(fileContent.toString());
	            fw.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		}
		
		System.out.println("Done !");
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	
		System.out.println("e get source = " + e);
		if (e.getSource() instanceof JButton)
			{
			JButton source = (JButton) e.getSource();
			if (source.getText() == "Foncer Rouges")
				colorOperation("red", "darken");
			else if (source.getText() == "Eclaircir Rouges")
				colorOperation("red", "lighten");
			else if (source.getText() == "Foncer Verts")
				colorOperation("green", "darken");
			else if (source.getText() == "Eclaircir Verts")
				colorOperation("green", "lighten");
			else if (source.getText() == "Foncer Bleus")
				colorOperation("blue", "darken");
			else if (source.getText() == "Eclaircir Bleus")
				colorOperation("blue", "lighten");
			else if (source.getText() == "Noir & Blanc")
				colorOperation("all", "desaturate");
			else if (source.getText() == "Inverser")
				colorOperation("all", "invert");
			else if (source.getText() == "Taille X x2")
				sizeOperation("sizeX2");
			else if (source.getText() == "Taille Y x2")
				sizeOperation("sizeY2");
			else if (source.getText() == "Taille x2")
				sizeOperation("sizeX2sizeY2");
			else if (source.getText() == "Afficher Taille")
			{
				String sizeX = String.valueOf(this.parent.getSizeX());
				String sizeY =String.valueOf(this.parent.getSizeY());
				sizeDisplay.setText(sizeX + "x"+sizeY+" px");
			}
			else if (source.getText() == "Rogner")
			{
				try 
				{
					crop(Integer.valueOf(cutLine1.getText().trim()), Integer.valueOf(cutLine2.getText().trim()), Integer.valueOf(cutRow1.getText().trim()), Integer.valueOf(cutRow2.getText().trim()));
				} catch (NumberFormatException e1) {
					new TerribleMistake("Les valeurs indiquées doivent être uniquement des entiers.");
				} catch (TerribleMistake e1) {
					e1.printStackTrace();
				}
			}
			else if (source.getText() == "Sauvegarder ...")
			{
				saveFile();
			}
			
			else if (source.getText() == "Retirer") // operation Retirer, gérée sans appel à une fonction. Le principe est qu'elle marque les pixels à retirer pour la future fusion.
			{
				System.out.println("Removing !");
				int red = Integer.valueOf(colorR.getText());
				int green = Integer.valueOf(colorG.getText());
				int blue = Integer.valueOf(colorB.getText());
				
				if (red > 255 || red < 0 || green > 255 || green < 0 || blue > 255 || blue < 0)
				{
					try {
						throw new TerribleMistake("Les valeurs des couleurs doivent être comprises entre 0 et 255 !");
					} catch (TerribleMistake e1) {
						e1.printStackTrace();
					}
				}
				
				Color col = new Color(red, green, blue, 255);
												
				for (int i = 0; i < imagePixels.length; i++)
				{
					Pixel p = imagePixels[i];
					if (p.isEqualTo(col))
					{
						System.out.println("Found same color !");
						p.setColor(Color.white);
						p.isRemoved(true);
					}
				}
				System.out.println("Done.");
				parent.getWindow().getLayeredPane().repaint();
			}
			else if (source.getText() == "Ajouter")
			{
				if (!shapeStartPosX.getText().isEmpty() && !shapeStartPosY.getText().isEmpty() && !shapeHeight.getText().isEmpty() && !shapeWidth.getText().isEmpty())
				{
					System.out.println("Adding shape ...");
					int startX = Integer.valueOf(shapeStartPosX.getText());
					int startY = Integer.valueOf(shapeStartPosY.getText());
					int height = Integer.valueOf(shapeHeight.getText());
					int width = Integer.valueOf(shapeWidth.getText());
					boolean hasColor = !colorR.getText().isEmpty() && !colorG.getText().isEmpty() && !colorB.getText().isEmpty();
					if (hasColor)
					{
						Color shapeCol = new Color(Integer.valueOf(colorR.getText()), Integer.valueOf(colorG.getText()), Integer.valueOf(colorB.getText()));
						addShape(startX, startY, height, width, shapeCol);
					}
					else
						addShape(startX, startY, height, width, null);
					
				} else
					try {
						throw new TerribleMistake("Vous devez remplir toutes les valeurs et indiquer une couleur avant d'ajouter une forme !");
					} catch (TerribleMistake e1) {
						e1.printStackTrace();
					}
			}
			
		}
		else if (e.getSource() instanceof Pixel)
		{
			Pixel p = (Pixel) e.getSource();
			colorR.setText(String.valueOf(p.getColor().getRed()));
			colorG.setText(String.valueOf(p.getColor().getGreen()));
			colorB.setText(String.valueOf(p.getColor().getBlue()));
		}
		
	
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		
		JTextArea source = (JTextArea) e.getSource();
		source.setText("");
		System.out.println("Focus gained on : " + source);
	}

	@Override
	public void focusLost(FocusEvent e) {
		
	}
	
	public JFrame[] getAllWindows()
	{
		JFrame[] allWinds = {this.mainWindow, this.cutWindow, this.saveWindow, this.colorWindow, this.shapeWindow};
		return allWinds;
	}


	
}
