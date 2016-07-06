package com.atomizer.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Item {

	private static double x = -50;
	private static double y = -50;

	private static int w = 32;
	private static int h = 32;
	public static int a = 0;
	public static int itemNumber;
	public static boolean shield = false;
	public static boolean p1Shield = false;
	public static boolean p2Shield = false;

	private BufferedImage item1;
	private BufferedImage item2;
	private BufferedImage item3;

	public Item(double x, double y, int w, int h, Main game) {
		Item.x = x;
		Item.y = y;

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());

		item1 = ss.grabImage(2, 1, w, h);
		item2 = ss.grabImage(3, 1, w, h);
		item3 = ss.grabImage(4, 1, w, h);

	}

	public void update() {

		spawnPosition();
		if (Ball.itemCol) {
			resetBallItemEffect();
		}
		shield();

	}

	public void render(Graphics g) {
		if (itemNumber == 1) {
			g.drawImage(item1, (int) x, (int) y, null);
		}
		if (itemNumber == 2) {
			g.drawImage(item2, (int) x, (int) y, null);
		}
		if (itemNumber == 3) {
			g.drawImage(item3, (int) x, (int) y, null);
		}
		if (itemNumber == 4) {
			Item.resetItemPos();
		}

		if (p1Shield) {
			g.setColor(Color.RED);
			g.fillRect(0, 0, 32, Main.HEIGHT + 9);
		}
		if (p2Shield) {
			g.setColor(Color.GREEN);
			g.fillRect(Main.WIDTH - 24, 0, 32, Main.HEIGHT + 9);
		}
	}

	public void spawnPosition() {

		if (Main.updates == 59) {
			Random r = new Random();
			int b = r.nextInt((700 - 100) + 1) + 100;
			int c = r.nextInt((300 - 1) + 1) + 1;
			a++;
			if (a == 10) {
				itemNumber = r.nextInt((4 - 1) + 1) + 1;

				Item.x = Main.WIDTH - b * 2;
				if (Item.x <= 128 || Item.x >= 1200) {
					Item.x = 400;
				}
				if (Item.y <= 0 || Item.y >= Main.HEIGHT - 32) {
					Item.y = 300;
				}
				Item.y = Main.HEIGHT - c * 2;

				a = 0;
			}
		}

	}

	public static void resetBallItemEffect() {
		if (a == 4) {
			Ball.itemCol = false;
			Ball.resetBallVelDir();
		}
	}

	public static void shield() {

		if (p1Shield) {
			if ((Ball.getX() < 32)) {
				Ball.reverseDirX();
				p1Shield = false;
			}

		}
		if (p2Shield) {
			if (Ball.getX() > Main.WIDTH - 64) {
				Ball.reverseDirXop();
				p2Shield = false;
			}
		}

	}

	public static void bouncey() {
		if (a == 4) {
			Ball.resetBallVelDir();
		}
	}

	public static void resetItemPos() {
		Item.x = -100;
		Item.y = -100;
	}

	public static double getX() {
		return x;
	}

	public static double getY() {
		return y;
	}

	public void setX(double x) {
		Item.x = x;
	}

	public void setY(double y) {
		Item.y = y;
	}

	public static int getW() {
		return w;
	}

	public static int getH() {
		return h;
	}

	public static Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, w, h);
	}
}