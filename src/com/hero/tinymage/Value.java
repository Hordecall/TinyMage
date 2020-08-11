package com.hero.tinymage;

public abstract class Value {

	private int value;
	private boolean isFilled;
	private Value next;
	
	Value(int value)
	{
		this.value = value;
		isFilled = true;
	}
	
	Value()
	{
		isFilled = false;
	}
	
	public boolean isFilled()
	{
		return this.isFilled;
	}
	
	public void setValue(int value)
	{
		this.value = value;
		this.isFilled = true;
	}
	
	public int getValue() throws TerribleMistake
	{
		if (isFilled)
		{
			return this.value;
		}
		else
			throw new TerribleMistake("L'élément n'a pas de valeur.");
	}
	
	public Value getNext()
	{
		return this.next;
	}
	
	public void setNext(Value color)
	{
		this.next = color;
	}
	
}
