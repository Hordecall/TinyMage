package com.hero.tinymage;

import javax.swing.JOptionPane;

public class TerribleMistake extends Exception {

	TerribleMistake(String message)
	{
		JOptionPane.showMessageDialog(null, message, "Uh oh !", JOptionPane.ERROR_MESSAGE);
		System.out.println(message);
	}
}
