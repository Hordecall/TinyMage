package com.hero.tinymage;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * Petite classe de test pour les pixels.
 * @author hero
 *
 */
public class PixelTest {

	@Test
	public void test() {
		Color firstCol = new Color (12, 25, 32, 0);
		Color secCol = new Color (12, 25, 32, 8);
		Pixel p = new Pixel(firstCol);
		assertEquals(true, p.isEqualTo(secCol));
	}

}
