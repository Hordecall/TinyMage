package com.hero.tinymage;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * Petite classe de test pour une fonction recursive.
 * @author hero
 *
 */
public class AbstractImageTest {
	
	Pixel[] pixels = new Pixel[100];

	@Test
	public void test() {
		
		for (int i = 0; i < pixels.length; i ++)
		{
			pixels[i] = new Pixel(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
		}
		
		String s = AbstractImage.recursiveWriteToString(0, "", pixels);		
		
		System.out.println("Result : " + s);
		
		
	}

}
