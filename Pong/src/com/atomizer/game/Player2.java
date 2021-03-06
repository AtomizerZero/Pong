package com.atomizer.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player2 {

	private static double x;
	private static double y;

	private static double velX = 0;
	private static double velY = 0;
	
	private static int w = 32;
	private static int h = 96;
	
	private BufferedImage player;
	

	public Player2(double x, double y, int w, int h, Main game) {
		Player2.x = x;
		Player2.y = y;
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());

		player = ss.grabImage(1, 4, w, h);

	}

	public void update() {
		x += velX;
		y += velY;
		
	}
	
	public static Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, w, h);
	}

	public void render(Graphics g) {
		g.drawImage(player, (int) x, (int) y, null);

	}

	public static double getX() {
		return x;
	}

	public static double getY() {
		return y;
	}

	public void setX(double x) {
		Player2.x = x;
	}

	public void setY(double y) {
		Player2.y = y;
	}

	public void setVelX(double velX) {
		Player2.velX = velX;
	}
	
	public static double getVelX() {
		return velX;
	}

	public void setVelY(double velY) {
		Player2.velY = velY;

	}
	
	public static double getVelY() {
		return velY;
	}

	public static int getH() {
		return h;
	}

	public void setH(int h) {
		Player2.h = h;
	}

	public static int getW() {
		return w;
	}
	
	public void setW(int w) {
		Player2.w = w;
	}

}
