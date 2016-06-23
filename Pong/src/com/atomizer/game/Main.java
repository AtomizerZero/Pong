package com.atomizer.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
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

	private boolean ballMoving = false;

	private Thread thread;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;

	private Player1 p1;
	private Player2 p2;
	private Ball ball;

	public static Rectangle recP1;
	public static Rectangle recP2;
	public static Rectangle recBall;
	public static Rectangle recP1Top;
	public static Rectangle recP1Bot;

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
		frame.setBounds(screenWidth / 6, screenHeight / 5, WIDTH, HEIGHT);
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

		p1 = new Player1(WIDTH / 16, this.getHeight() / 2 - 96, 32, 96, this);
		p2 = new Player2(WIDTH - Player1.getX() - 32, this.getHeight() / 2 - 96, 32, 96, this);
		ball = new Ball(WIDTH / 2 - 32, HEIGHT / 2 - 32, 32, 32, this);

		recP1 = new Rectangle((int) Player1.getX(), (int) Player1.getY(), (int) Player1.getW(), (int) Player1.getH());
		recP2 = new Rectangle((int) Player2.getX(), (int) Player2.getY(), (int) Player2.getW(), (int) Player2.getH());
		recBall = new Rectangle((int) ball.getX()+4, (int) ball.getY()+4, (int) ball.getW(), (int) ball.getH());
		recP1Top = new Rectangle((int) Player1.getX(), (int) Player1.getY(), (int) Player1.getW(), 8);
		recP1Bot = new Rectangle((int) Player1.getX(), (int) (Player1.getY() + Player1.getH() - 8), (int) Player1.getW(), 8);

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

		if (ballMoving) {
			ball.update();
		}

		p1.update();
		p2.update();

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

		p1.render(g);
		p2.render(g);
		ball.render(g);
		g.setColor(Color.ORANGE);
		g.drawRect((int) Player1.getX(), (int) (Player1.getY() + Player1.getH() - 8), (int) Player1.getW() -1, 8);
//		g.drawRect((int) Player1.getX(), (int) Player1.getY(), (int) Player1.getW(), (int) Player1.getH());
//		g.drawRect((int) Player2.getX(), (int) Player2.getY(), (int) Player2.getW(), (int) Player2.getH());
//		g.drawRect((int) ball.getX() + 4, (int) ball.getY() + 4, (int) ball.getW() - 8, (int) ball.getH() - 8);

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