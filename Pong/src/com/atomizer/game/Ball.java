package com.atomizer.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Ball {

	public static boolean col = false;

	private double x;
	private double y;

	private double velX = -8;
	private double velY = -7;

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
		x += velX;
		y += velY;
		if (x < 0) {
			reverseDirX();
			System.out.println("p2!");
		}
		if (x > Main.WIDTH - 32) {
			reverseDirX();
			System.out.println("p1!");
		}

		if (y < 0) {
			reverseDirY();
		}

		if (y > Main.HEIGHT - 32 - 24) {
			reverseDirY();
		}

		if (this.getBounds().intersects(Player1.getBounds())) {
			double p1y = Player1.getY();
			if (y >= p1y - Player1.getH() / 3 && y <= p1y + Player1.getH() / 3) {
				reverseDirX();
				System.out.println("1");
			} else if (y >= p1y - Player1.getH() / 2 && y <= p1y + Player1.getH() / 2) {
				velY += (y > p1y ? 1 : -1);
				reverseDirX();
				System.out.println("2");
			} else if (y >= p1y - Player1.getH() && y <= p1y + Player1.getH()) {
				velY += (y > p1y ? 2 : -2);
				reverseDirX();
				System.out.println("3");
			}

			reverseDirYplus();
		}

		if (this.getBounds().intersects(Player2.getBounds())) {
			double p2y = Player2.getY();
			if (this.y >= p2y - Player2.getH() / 3 && y <= p2y + Player2.getH() / 3) {
				reverseDirX();
				System.out.println("1");
			} else if (y >= p2y - Player2.getH() / 2 && y <= p2y + Player2.getH() / 2) {
				velY += (y > p2y ? 1 : -1);
				reverseDirX();
				System.out.println("2");
			} else if (y >= p2y - Player2.getH() && y <= p2y + Player2.getH()) {
				velY += (y > p2y ? 2 : -2);
				reverseDirX();
				System.out.println("3");
			}
			reverseDirYplus();
		}

	}

	public void render(Graphics g) {
		g.drawImage(ball, (int) x, (int) y, null);

	}

	public void reverseDirX() {

		velX = -velX;

	}

	public void reverseDirXplus() {

		this.velX = +velX;

	}

	public void reverseDirY() {
		velY = -velY;

	}

	public void reverseDirYplus() {
		this.velY = +velY;

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
