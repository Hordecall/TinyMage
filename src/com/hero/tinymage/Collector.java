package com.hero.tinymage;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Le Collector s'occupe de collecter les images créées et de les stocker.
 * Il sert de ressource au Mixer qui permet de fusionner deux images.
 * @author hero
 *
 */
public class Collector implements MouseListener{

	private static boolean exists = false;
	private static Image[] collection = new Image[20];
	private static int imageCount = 0;
	private static JFrame mixer;
	private static GridLayout grid;
	private static JLabel background, foreground;
	private static JPanel paneA, paneB, paneC;
	private static JComboBox<Integer> selectorA, selectorB;
	private static JButton generer;
	
	
	Collector()
	{
		if (!exists) // le collecteur n'est créé qu'une fois par ses classes filles
		{
			System.out.println("Creating Collector ...");
			imageCount = 0;
			
			mixer = new JFrame("Mixer");
			mixer.setSize(200, 120);
			grid = new GridLayout(3,3);
			mixer.setLayout(grid);
			
			background = new JLabel("Fond : ");
			foreground = new JLabel("Premier plan : ");
			selectorA = new JComboBox<Integer>(); selectorA.setSize(100,20);
			selectorB = new JComboBox<Integer>(); selectorB.setSize(100,20);
			
			paneA = new JPanel(); paneA.setLayout(new GridLayout(1,2));
			paneA.add(background); paneA.add(selectorA);
			paneA.setSize(200,30); paneA.setVisible(true);
			mixer.add(paneA);
			
			paneB = new JPanel(); paneB.setLayout(new GridLayout(1,2));
			paneB.add(foreground); paneB.add(selectorB);
			paneB.setSize(200,30); paneB.setVisible(true);
			mixer.add(paneB);
			
			generer = new JButton("Générer"); generer.setSize(200,30);
			paneC = new JPanel(); paneC.setLayout(new FlowLayout());
			paneC.add(generer); generer.addMouseListener(this);
			paneC.setSize(200,30); paneC.setVisible(true); paneC.addMouseListener(this);
			mixer.add(paneC);
			
			mixer.setLocation(0, 200);
			mixer.setVisible(true);
			exists = true;
			System.out.println("Done.");
		}
	}
	
	/**
	 * Génère une image "composite" à partir de deux images de la même taille.
	 * @param selectedIndex
	 * @param selectedIndex2
	 * @throws TerribleMistake si les images ne sont pas de la même taille.
	 */
	private void imageGenerator(int selectedIndex, int selectedIndex2) throws TerribleMistake {
		
		System.out.println("select index 1 : " + selectedIndex + " // select index 2 : "+ selectedIndex2);
		
		if (selectedIndex == selectedIndex2)
		{
			throw new TerribleMistake("Les index doivent être différents !");
		}
		else
		{
			Image i = collection[selectedIndex];
			Image j = collection[selectedIndex2];
			
			System.out.println("Image 1 =" +i.getID());
			System.out.println("Image 2 =" +j.getID());
			
			if (i.getSizeX() != j.getSizeX() || i.getSizeY() != j.getSizeY())
			{
				throw new TerribleMistake("Les images doivent être de la même taille.");
			}
			
			else
			{
				new CompositeImage(i, j);
			}
		}
	}
	
	public void addToCollection(Image i) throws TerribleMistake
	{
		if (imageCount < 20)
		{
			collection[imageCount] = i;
			selectorA.addItem(i.getID());
			selectorB.addItem(i.getID());
			System.out.println("Image " +i.getID() + " has been added to Collection.");
			System.out.println("Collection size : " + collection.length);
			System.out.println("Image Count = "+ imageCount);
			imageCount++;
		}
		else throw new TerribleMistake("Vous ne pouvez pas avoir plus de 20 fichiers en mémoire.");	
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		System.out.println(e.getSource());
		
		if (e.getSource() instanceof JButton)
		{
			JButton source = (JButton) e.getSource();
			if (source.getText() == "Générer")
			{
				System.out.println("generate");
				try 
				{
					imageGenerator((int) selectorA.getSelectedIndex(),(int) selectorB.getSelectedIndex());
				} 
				catch (TerribleMistake e1) 
				{
					e1.printStackTrace();
				}
			}
			else if (source.getText() == "Importer")
			{
				try {
					importFile();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("Quelque chose de terrible est survenu dans l'importation du fichier.");
				}
			}
			
			else if (source.getText() == "Créer")
			{
				new RandomImage(64,64);
			}
		}		
	}
	
	public void importFile() throws IOException {
	}

	public Image[] getCollection()
	{
		return collection;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
