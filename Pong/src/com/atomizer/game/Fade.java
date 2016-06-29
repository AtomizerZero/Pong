package com.atomizer.game;

import java.awt.Color;
import java.awt.Graphics;

public class Fade {

	private double x;
	private double y;
	private static double area = 24;

	private Color color;
	private int alpha;
	private int r;
	private int g;
	private int b;

	public Fade(double x, double y, int r, int g, int b) {
		this.x = x;
		this.y = y;
		alpha = 255;
		this.r = r;
		this.g = g;
		this.b = b;
		color = new Color(r, g, b, alpha);
	}

	public void fade() {
		alpha -= 23;
		setColor();
	}

	private void setColor() {
		color = new Color(r, g, b, alpha);
	}

	public int getAlpha() {
		return alpha;
	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillOval((int) x, (int) y, (int) area, (int) area);
	}

}
