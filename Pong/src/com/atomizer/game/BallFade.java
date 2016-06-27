package com.atomizer.game;

import java.awt.Graphics;
import java.util.LinkedList;

public class BallFade {

	private LinkedList<Fade> fBall;
	private double x;
	private double y;

	public BallFade(double x, double y) {

		fBall = new LinkedList<Fade>();
	}

	public void update() {

		x = Ball.getX();
		y = Ball.getY();

		if (fBall.size() < 12 && Ball.p1Col) {
			Fade fadeball = new Fade((int) x + 4, (int) y + 4, 200, 0, 0);
			fBall.add(fadeball);
		} else if (fBall.size() < 12 && Ball.p2Col) {
			Fade fadeball = new Fade((int) x + 4, (int) y + 4, 0, 200, 0);
			fBall.add(fadeball);
		} else if (fBall.size() < 12) {
			Fade fadeball = new Fade((int) x + 4, (int) y + 4, 0, 150, 255);
			fBall.add(fadeball);
		}
		LinkedList<Fade> garbageballs = new LinkedList<Fade>();
		for (Fade fb : fBall) {
			fb.fade();

			if (fb.getAlpha() <= 10) {
				garbageballs.add(fb);
			}
		}
		fBall.removeAll(garbageballs);
	}

	public void render(Graphics g) {
		g.fillOval((int) Ball.getX() + 4, (int) Ball.getY() + 4, 24, 24);
		for (Fade fb : fBall)
			fb.paint(g);

	}
}
