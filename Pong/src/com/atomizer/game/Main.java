package com.atomizer.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;

	public static double p1StartX = WIDTH / 16;
	public static double p1StartY = HEIGHT / 2 - 52;
	public static double p2StartX = WIDTH - 112;
	public static double p2StartY = HEIGHT / 2 - 52;
	public static double StartX = WIDTH / 2 - 32;
	public static double StartY = HEIGHT / 2 - 32;

	public static boolean running = false;
	public static boolean ballMoving = false;
	public static boolean debug = false;
	public static BufferStrategy bs;

	public static int gameStatus = 0;
	public static int updates = 0;
	public static int frames = 0;
	public static long timer = System.currentTimeMillis();
	public static int FPS = 0;

	private boolean p1UpPressed = false; // key W
	private boolean p1DownPressed = false; // key S
	private boolean p2UpPressed = false; // key numpad 8
	private boolean p2DownPressed = false; // key numpad 5

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;

	public static HashMap<String, Sound> sfx;

	private Thread thread;
	private Player1 p1;
	private Player2 p2;
	private Ball ball;
	private BallFade fBall;

	public static Rectangle recP1;
	public static Rectangle recP2;
	public static Rectangle recBall;

	public static int player1score;
	public static int player2score;
	public static int winScore = 3;

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

		game.start();

	}

	public synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();

	}

	private void init() {

		requestFocus();
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			spriteSheet = loader.loadImage("/sprite_sheet.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		sfx = new HashMap<String, Sound>();
		sfx.put("playerBounce", new Sound("/bounce1.wav"));
		sfx.put("screenBounce", new Sound("/bounce2.wav"));
		sfx.put("screenDead", new Sound("/bouncedead.wav"));

		addKeyListener(new KeyInput(this));
		initStuff();

	}

	public void run() {
		init();

		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;

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
				FPS = updates;
				updates = 0;
				frames = 0;
			}

		}

	}

	private void update() {
		if (getGameStatus() == 2) {

			p1.update();
			p2.update();

			ball.update();
			fBall.update();

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

		checkWin();
	}

	private void checkWin() {
		if (player1score == winScore) {
			setGameStatus(5);
		}
		if (player2score == winScore) {
			setGameStatus(6);
		}
	}

	private void render() {
		bs = this.getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (getGameStatus() == 0) {
			g.setColor(Color.WHITE);

			g.setFont(new Font("Ariel", Font.BOLD, 34));
			g.drawString("Press Space to Play", WIDTH / 2 - (WIDTH / 8), HEIGHT - 32);
			g.drawString("Press Escape to Quit", WIDTH / 2 - (WIDTH / 8), HEIGHT);
			g.drawString("Player1 Controls : W = up, S = down", WIDTH - WIDTH, HEIGHT - 128);
			g.drawString("Player2 Controls : Num8 = up, Num5 = down", WIDTH - WIDTH, HEIGHT - 128 + 32);

			Random rand = new Random();
			float red = rand.nextFloat();
			float grn = rand.nextFloat();
			float blu = rand.nextFloat();

			Color randomColor = new Color(red, grn, blu);

			g.setColor(randomColor);

			g.setFont(new Font("Ariel", Font.BOLD, 196));
			g.drawString("PONG!", WIDTH / 4, HEIGHT / 2);

		}

		if (getGameStatus() == 2 || getGameStatus() == 1 || getGameStatus() == 3 || getGameStatus() == 4
				|| getGameStatus() == 7) {

			p1.render(g);
			p2.render(g);
			Color bcolor = new Color(0,150,255);
			g.setColor(bcolor);
			fBall.render(g);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Ariel", Font.BOLD, 36));
			g.drawString("Player1: " + player1score, WIDTH / 2 - (WIDTH / 2), HEIGHT);
			g.drawString("Player2: " + player2score, WIDTH / 2 + (WIDTH / 4 + 96), HEIGHT);
		}

		if (getGameStatus() == 1) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Ariel", Font.BOLD, 64));
			g.drawString("PAUSED", WIDTH / 2 - 128, HEIGHT / 4);
		}

		if (getGameStatus() == 3) {
			g.setColor(Color.RED);
			g.setFont(new Font("Ariel", Font.BOLD, 128));
			g.drawString("Player1 Scored!", WIDTH - WIDTH + 128 + 48, HEIGHT / 2);

		}
		if (getGameStatus() == 4) {
			g.setColor(Color.GREEN);
			g.setFont(new Font("Ariel", Font.BOLD, 128));
			g.drawString("Player2 Scored!", WIDTH - WIDTH + 128 + 48, HEIGHT / 2);

		}
		if (getGameStatus() == 5) {
			g.setColor(Color.RED);
			g.setFont(new Font("Ariel", Font.BOLD, 128));
			g.drawString("Player1 WINS!", WIDTH - WIDTH + 128 + 48, HEIGHT / 2);
		}
		if (getGameStatus() == 6) {
			g.setColor(Color.GREEN);
			g.setFont(new Font("Ariel", Font.BOLD, 128));
			g.drawString("Player2 WINS!", WIDTH - WIDTH + 128 + 48, HEIGHT / 2);
		}

		if (debug) {
			
			String Build = ("1.3.290616.13.18");
			g.setColor(Color.GREEN);
			g.setFont(new Font("Ariel", Font.BOLD, 12));

			g.drawString(FPS + " fps", 0, 12);
			g.drawString("XVel: " + Ball.velX, 0, 24);
			g.drawString("YVel: " + Ball.velY, 0, 36);
			g.drawString("X: " + Ball.getX(), 0, 48);
			g.drawString("Y: " + Ball.getY(), 0, 60);
			g.drawString("P1Col: " + Ball.p1Col, 64, 12);
			g.drawString("P2Col: " + Ball.p2Col, 64, 24);
			g.drawString("Col: " + Ball.col, 64, 36);
			g.drawString("Build: " + Build, WIDTH - 128, 12);

		}
		g.dispose();
		bs.show();

	}

	public void initStuff() {
		p1 = new Player1(p1StartX, p1StartY, 32, 96, this);
		p2 = new Player2(p2StartX, p2StartY, 32, 96, this);
		ball = new Ball(StartX, StartY, 32, 32, this);

		recP1 = new Rectangle((int) Player1.getX(), (int) Player1.getY(), (int) Player1.getW(), (int) Player1.getH());
		recP2 = new Rectangle((int) Player2.getX(), (int) Player2.getY(), (int) Player2.getW(), (int) Player2.getH());
		recBall = new Rectangle((int) Ball.getX() + 4, (int) Ball.getY() + 4, (int) Ball.getW(), (int) Ball.getH());

		fBall = new BallFade(Ball.getX() + 4, Ball.getY() + 4);

	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public HashMap<String, Sound> getSFX() {
		return sfx;
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
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			if (!debug) {
				debug = true;

			} else if (debug) {
				debug = false;
			}
		}
		// - GameStatus
		// - 0 = title
		// - 1 = paused
		// - 2 = game in play
		// - 3 = player 1 score
		// - 4 = player 2 score
		// - 5 = player 1 win
		// - 6 = player 2 win
		// - 7 = continue after score

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (getGameStatus() == 1) {
				setGameStatus(2);
			} else if (getGameStatus() == 2) {
				setGameStatus(1);
			} else if (getGameStatus() == 0) {
				setGameStatus(7);
			} else if (getGameStatus() == 3) {
				initStuff();
				setGameStatus(7);
			} else if (getGameStatus() == 4) {
				initStuff();
				setGameStatus(7);
			} else if (getGameStatus() == 5) {
				Ball.resetBallVel();
				initStuff();
				player1score = 0;
				player2score = 0;

				setGameStatus(0);
			} else if (getGameStatus() == 6) {
				Ball.resetBallVel();
				initStuff();
				player1score = 0;
				player2score = 0;

				setGameStatus(0);
			} else if (getGameStatus() == 7) {
				Ball.resetBallVel();
				initStuff();
				setGameStatus(2);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
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

	public int getGameStatus() {
		return gameStatus;
	}

	public static void setGameStatus(int gameStatus) {
		Main.gameStatus = gameStatus;
	}

}