package com.atomizer.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static int screenWidth = (int) screenSize.getWidth();
	private static int screenHeight = (int) screenSize.getHeight();

	public static boolean running = false;

	private boolean p1UpPressed = false; // key W
	private boolean p1DownPressed = false; // key S
	private boolean p2UpPressed = false; // key numpad 8
	private boolean p2DownPressed = false; // key numpad 5

	public static boolean ballMoving = false;

	private Thread thread;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;

	private Player1 p1;
	private Player2 p2;
	private Ball ball;
	private BallFade fBall;

	public static Rectangle recP1;
	public static Rectangle recP2;
	public static Rectangle recBall;

	public static int player1score;
	public static int player2score;

	public static void main(String[] args) {
		Main game = new Main();

		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		game.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		JFrame frame = new JFrame("Pong");
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Pong");
		frame.setVisible(true);
		frame.requestFocusInWindow();

		System.out.println("ScreenWidth: " + screenWidth);
		System.out.println("ScreenHeight: " + screenHeight);

		game.start();

	}

	public synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();

		System.out.println("Start");

	}

	private void init() {

		requestFocus(); // required for instant focus on window
		System.out.println("init");

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			spriteSheet = loader.loadImage("/sprite_sheet.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		addKeyListener(new KeyInput(this));

		p1 = new Player1(WIDTH / 16, HEIGHT / 2 - 52, 32, 96, this);
		p2 = new Player2(WIDTH - Player1.getX() - 32, HEIGHT / 2 - 52, 32, 96, this);
		ball = new Ball(WIDTH / 2 - 32, HEIGHT / 2 - 32, 32, 32, this);

		recP1 = new Rectangle((int) Player1.getX(), (int) Player1.getY(), (int) Player1.getW(), (int) Player1.getH());
		recP2 = new Rectangle((int) Player2.getX(), (int) Player2.getY(), (int) Player2.getW(), (int) Player2.getH());
		recBall = new Rectangle((int) Ball.getX() + 4, (int) Ball.getY() + 4, (int) Ball.getW(), (int) Ball.getH());

		fBall = new BallFade(Ball.getX() + 4, Ball.getY() + 4);
	}

	public void run() {
		init();
		System.out.println("Running: " + running);

		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}

		}

	}

	private void update() {
		p1.update();
		p2.update();

		if (ballMoving) {
			ball.update();
			fBall.update();
		}
		if ((p1UpPressed) && (!p1DownPressed)) {
			p1.setVelY(-10);
		} else if ((p1DownPressed) && (!p1UpPressed)) {
			p1.setVelY(10);
		} else if ((!p1DownPressed) && (!p1UpPressed)) {
			p1.setVelY(0);
		}

		if (Player1.getY() - Player1.getH() / 96 < 0) {
			p1.setY(Player1.getH() / 96);
		} else if (Player1.getY() + Player1.getH() > this.getHeight()) {
			p1.setY(this.getHeight() - Player1.getH());
		}

		if ((p2UpPressed) && (!p2DownPressed)) {
			p2.setVelY(-10);
		} else if ((p2DownPressed) && (!p2UpPressed)) {
			p2.setVelY(10);
		} else if ((!p2DownPressed) && (!p2UpPressed)) {
			p2.setVelY(0);
		}

		if (Player2.getY() - Player2.getH() / 96 < 0) {
			p2.setY(Player2.getH() / 96);
		} else if (Player2.getY() + Player2.getH() > this.getHeight()) {
			p2.setY(this.getHeight() - Player2.getH());
		}

	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.setFont(new Font("Ariel", Font.BOLD, 56));
		g.setColor(Color.WHITE);
		if (!ballMoving) {
			g.drawString("PRESS SPACE TO PLAY", WIDTH / 2 - (WIDTH / 4), HEIGHT / 8);
		}
		g.setFont(new Font("Ariel", Font.BOLD, 36));

		g.drawString("Player1: " + player1score, WIDTH / 2 - (WIDTH / 2), HEIGHT);
		g.drawString("Player2: " + player2score, WIDTH / 2 + (WIDTH / 4 + 96), HEIGHT);
		fBall.render(g);
		p1.render(g);
		p2.render(g);

		g.dispose();
		bs.show();

	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			p1UpPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			p1DownPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			p2UpPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			p2DownPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {

			ballMoving = true;

		}
	}

	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_W) {
			p1UpPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			p1DownPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			p2UpPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			p2DownPressed = false;
		}
	}

}