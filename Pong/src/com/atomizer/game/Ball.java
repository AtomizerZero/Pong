package com.atomizer.game;

import java.awt.Rectangle;

public class Ball {

	public static boolean col = false;
	public static boolean p1Col = false;
	public static boolean p2Col = false;

	private static double x;
	private static double y;

	private static double velX = -8;
	private static double velY = -7;

	private static int w = 32;
	private static int h = 32;
	private int i = 0;

	public Ball(double x, double y, int w, int h, Main game) {
		Ball.x = x;
		Ball.y = y;

	}

	public void update() {
		x += velX;
		y += velY;
		{

			if (x < 0) {
				Main.ballMoving = false;
				Main.player2score++;
				System.out.println("p2!");
			}
			if (x > Main.WIDTH - 32) {
				Main.ballMoving = false;
				Main.player2score++;
				System.out.println("p1!");
			}

			if (y < 0) {
				reverseDirY();
				velY += (y > y ? 1 : +1);
				p1Col = false;
				p2Col = false;
				
			}

			if (y > Main.HEIGHT - 24) {
				p1Col = false;
				p2Col = false;
				
				reverseDirY();
				i++;
				if (i == 5) {
					i = 0;
					velY += (y > y ? 1 : -1);
				}
			}
			
		}
		if (this.getBounds().intersects(Player1.getBounds())) {
			p1Col = true;p2Col = false;
			double p1x = Player1.getX();
			double p1y = Player1.getY();

			if (y >= p1y - Player1.getH() / 3 && y <= p1y + Player1.getH() / 3) {
				reverseDirX();
				velX += (x > p1x ? 1 : -1);
			} else if (y >= p1y - Player1.getH() / 2 && y <= p1y + Player1.getH() / 2) {
				velY += (y > p1y ? 1 : -1);
				reverseDirX();
			} else if (y >= p1y - Player1.getH() && y <= p1y + Player1.getH()) {
				velY += (y > p1y ? 2 : -2);
				reverseDirX();
				velX += (x > p1x ? 1 : -1);
			}
		}

		if (this.getBounds().intersects(Player2.getBounds())) {
			p2Col = true;p1Col = false;
			double p2x = Player2.getX();
			double p2y = Player2.getY();
			if (Ball.y >= p2y - Player2.getH() / 3 && y <= p2y + Player2.getH() / 3) {
				reverseDirXop();
				velX += (x > p2x ? 1 : +1);
			} else if (y >= p2y - Player2.getH() / 2 && y <= p2y + Player2.getH() / 2) {
				velY += (y > p2y ? 1 : +1);
				reverseDirXop();
			} else if (y >= p2y - Player2.getH() && y <= p2y + Player2.getH()) {
				velY += (y > p2y ? 2 : +2);
				reverseDirXop();
				velX += (x > p2x ? 1 : +1);

			}
		}

	}

	public void reverseDirX() {

		if (velX < 0) {
			velX = -velX;
		} else if (velX > 0) {
			velX = +velX;
		}

	}

	public void reverseDirXop() {

		if (velX > 0) {
			velX = -velX;
		} else if (velX < 0) {
			velX = +velX;
		}

	}

	public void reverseDirY() {
		velY = -velY;

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, w, h);
	}

	public static double getX() {
		return x;
	}

	public static double getY() {
		return y;
	}

	public void setX(double x) {
		Ball.x = x;
	}

	public void setY(double y) {
		Ball.y = y;
	}

	public void setVelX(double velX) {
		Ball.velX = velX;
	}

	public void setVelY(double velY) {
		Ball.velY = velY;

	}

	public static double getVelX() {
		return velX;
	}

	public static double getVelY() {
		return velY;
	}

	public static int getH() {
		return h;
	}

	public void setH(int h) {
		Ball.h = h;
	}

	public static int getW() {
		return w;
	}

	public void setW(int w) {
		Ball.w = w;
	}

}
