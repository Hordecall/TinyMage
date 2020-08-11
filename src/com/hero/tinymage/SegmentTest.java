package com.hero.tinymage;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Petite classe de test pour les segments.
 * @author hero
 *
 */
public class SegmentTest {

	@Test
	public void test() {
		
		Segment seg = new Segment(255, 255, 255);
		Segment seg2 = new Segment(255, 255, 255);
		
		assertEquals(true, seg.isEqualTo(seg2));
		
	}
	
	@Test
	public void testHasNext(){
		
		Segment seg = new Segment(255,255,255);
		
		assertEquals(false, seg.hasNext());
	}

}
