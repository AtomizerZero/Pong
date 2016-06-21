package com.atomizer.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Ball {

	public static boolean col = false;

	private double x;
	private double y;

	private double velX = -5;
	private double velY = -4;

	private int w = 32;
	private int h = 32;

	private BufferedImage ball;

	public Ball(double x, double y, int w, int h, Main game) {
		this.x = x;
		this.y = y;

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());

		ball = ss.grabImage(2, 1, w, h);

	}

	public void update() {
		x = x + velX;
		y = y + velY;
		if (x < 0) {
			velX = 5;

			System.out.println("p2!");
		} else if (x > Main.WIDTH - 32) {
			velX = -5;
			System.out.println("p1!");
		}

		if (y < 0) {
			velY = 4;

		} else if (y > Main.HEIGHT - 32 - 24) {
			velY = -4;
		}

		if (this.getBounds().intersects(Player1.getX(), Player1.getY(), Player1.getW(), Player1.getH())) {
			col = true;
			reverseDir();
			System.out.println("col: " + col);

		}
		if (this.getBounds().intersects(Player2.getX(), Player2.getY(), Player2.getW(), Player2.getH())) {
			col = true;
			reverseDir();
			System.out.println("col: " + col);
			
		}

	}

	public void render(Graphics g) {
		g.drawImage(ball, (int) x, (int) y, null);

	}
	
	public void reverseDir() {
		velX = -velX;
		velY = -velY;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, w, h);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public void setVelY(double velY) {
		this.velY = velY;

	}

	public double getVelX(double velX) {
		return velX;
	}

	public double getVelY(double velY) {
		return velY;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

}
