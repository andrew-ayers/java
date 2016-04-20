//
// Asteroids - A simple implementation in Java
//
// Copyright (C) 2016 by Andrew L. Ayers
//
// This program is free software; you can redistribute it and/or modify it under
// the terms of the GNU General Public License as published by the Free Software
// Foundation; either version 3 of the License, or (at your option) any later 
// version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT 
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
// FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with 
// this program; if not, write to the Free Software Foundation, Inc., 51 Franklin 
// Street, Fifth Floor, Boston, MA 02110-1301  USA
//

package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.RenderingHints
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

// Implements the actual game animation thread
public class Board extends JPanel implements ActionListener, Commons {
    private Timer timer; // for animation thread
    
    private Craft craft; // player

    private ArrayList<Asteroid> asteroids; // asteroids on screen
    
    private ArrayList<Asteroid> splits; // array to hold "split" of an asteroid

    private ArrayList<Explosion> explosions; // explosions on screen
    
    // radius of "emptiness" around player when ship is to be re-displayed after hit
    private final int EXCLUSION_RADIUS = 100; 
    
    private int score; // current score
    private int level; // current level

    private String message = "!!! Game Over !!!"; // game-over message

	// Instantiate the game; add listener for key-press events, set 
	// double-buffereing of panel, start animation timer, and reset
	// the game
    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        timer = new Timer(5, this); // 5 ms between frames (200 FPS)
        timer.start();
        
        resetGame();
    }

	// Reset the game (score, level, new player, initialize explosions
	// and asteroids
	private void resetGame() {
		score = 0;
		level = 1;
		
        craft = new Craft();
        
    	explosions = new ArrayList<Explosion>();

		initAsteroids();
	}
	
	// Repaint the panel for animation
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;
        
        // The following anti-aliases things, but really slows stuff down
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
        drawCraft(g2d);
        drawMissiles(g2d);
        drawAsteroids(g2d);
        drawExplosions(g2d);
        drawScores(g2d);
        
        // If the player has no lives, show "game over" message
        if (craft.getLives() <= 0) {
			gameOver(g2d);
		}
        
        // This is done to keep things in sync on some platforms
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

	// Called by animation timer event; move the various pieces of the 
	// game, and perform checks, then re-paint
    public void actionPerformed(ActionEvent e) {
		moveCraft();
		moveMissiles();
		moveAsteroids();
		checkHits();
		checkCrash();
		
        repaint(); // repaint the panel (calls paint() method)
    }

	// Initialize a field of (large) asteroids based on level
	private void initAsteroids() {
		asteroids = new ArrayList<Asteroid>();

		for (int i = 0; i < (level * 4); i++) {
			
			int ax = (int) Math.floor(Math.random() * PANEL_WIDTH);
			int ay = (int) Math.floor(Math.random() * PANEL_HEIGHT);
			int rot = (int) Math.floor(Math.random() * 360);
			int dir = (int) Math.floor(Math.random() * 360);
			
			Asteroid asteroid = new Asteroid(ax, ay, 0, rot, dir);
		
			asteroids.add(asteroid);
		}
		
		splits = new ArrayList<Asteroid>();

		this.level++;
	}

	// Move the player's craft around on the screen
	private void moveCraft() {
        craft.move();
        craft.bounds();
	}
	
	// Move visible missiles around on the screen
	private void moveMissiles() {
        ArrayList ms = craft.getMissiles();

        for (int i = 0; i < ms.size(); i++) {
            Missile m = (Missile) ms.get(i);
            if (m.isVisible()) {
                m.move();
                m.bounds();
			}
            else {
				ms.remove(i);
			}
        }
	}
	
	// Move visible asteroids around on the screen
	private void moveAsteroids() {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid as = (Asteroid) asteroids.get(i);
            if (as.isVisible()) {
                as.move();
                as.bounds();
			}
            else {
				asteroids.remove(i);
			}
        }
	}
	
	// Draw the player's craft
	private void drawCraft(Graphics2D g2d) {
        craft.draw(g2d, Color.green);
	}

	// Draw any missiles fired by the player
	private void drawMissiles(Graphics2D g2d) {
		ArrayList ms = craft.getMissiles();

        for (int i = 0; i < ms.size(); i++ ) {
            Missile m = (Missile) ms.get(i);
            m.drawMissile(g2d, Color.white);
        }
	}
	
	// Check to see if any missiles have hit an asteroid; if so, split
	// the asteroid and increase the player's score
	private void checkHits() {
		ArrayList ms = craft.getMissiles();
		
        for (int i = 0; i < ms.size(); i++ ) {
            Missile m = (Missile) ms.get(i);
            
            if (m.isVisible()) {
				int mx = (int) m.getX();
				int my = (int) m.getY();
				
				for (int j = 0; j < asteroids.size(); j++) {
					Asteroid a = (Asteroid) asteroids.get(j);
					
					if (a.isVisible()) {
						Rectangle brect = a.getBBounds();
						
						if (mx >= brect.getX() && mx <= brect.getX() + brect.width) {
							if (my >= brect.getY() && my <= brect.getY() + brect.height) {
								m.inVisible();
								
								a.inVisible();
															
								splitAsteroid(a);
								
								score = score + ((a.getSize() + 1) * 10);
							}
						}
					}
				}				
			}
		}
		
		if (!splits.isEmpty())
			asteroids.addAll(splits);
			splits.clear();
		
	}
	
	// Check to see if an asteroid has hit the player; if so, split the
	// asteroid apart and re-initialize the player
	private void checkCrash() {
		if (craft.getLives() > 0) {
			for (int j = 0; j < asteroids.size(); j++) {
				Asteroid a = (Asteroid) asteroids.get(j);
				
				if (a.isVisible()) {
					Rectangle brect = a.getBBounds();
					
					if (craft.getX() >= brect.getX() && craft.getX() <= brect.getX() + brect.width) {
						if (craft.getY() >= brect.getY() && craft.getY() <= brect.getY() + brect.height) {
							a.inVisible();
							
							splitAsteroid(a);
							
							craft.init();
						}
					}
				}
			}
		
			if (!splits.isEmpty())
				asteroids.addAll(splits);
				splits.clear();			
		}
	}

	// Draw the asteroids; if the player has been killed, and needs to
	// be re-shown, wait for the asteroids "on-screen" to clear enough
	// space before the player is re-instanced. 
	//
	// NOTE: We need to do something similar on the init of asteroids 
	// as well, to keep the player from dying at the start of the game 
	// or on level changes
	private void drawAsteroids(Graphics2D g2d) {
		if (asteroids.size() <= 0)
			this.initAsteroids();
			
		boolean hits = false;
		
        for (int i = 0; i < asteroids.size(); i++ ) {
            Asteroid as = (Asteroid) asteroids.get(i);
            as.draw(g2d, Color.red);
            
            if (!craft.isVisible() && !hits) {
				double ax = as.getX();
				double ay = as.getY();
				double cx = craft.getX();
				double cy = craft.getY();
				
				double dist = Math.sqrt(Math.pow((ax - cx), 2) + Math.pow((ay - cy), 2));
				
				if (dist < EXCLUSION_RADIUS)
					hits = true;
			}
        }
        
        if (!hits)
			craft.setVisible();
	}

	// Draw the explosions as needed
	private void drawExplosions(Graphics2D g2d) {
        for (int i = 0; i < explosions.size(); i++ ) {
            Explosion e = (Explosion) explosions.get(i);
            if (e.isVisible()) {
				e.draw(g2d);
			}
            else {
				explosions.remove(i);
			}
        }
	}
	
	// Display the scorebar
	private void drawScores(Graphics2D g2d) {
        Font small = new Font("Helvetica", Font.PLAIN, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString("SCORE: " + score, 5, 15);

        String level = "LEVEL - " + (this.level - 1);
        
        g2d.drawString(level, (PANEL_WIDTH / 2) - (metr.stringWidth(level) / 2), 15);	

        String lives = "LIVES: " + craft.getLives();
        
        g2d.drawString(lives, PANEL_WIDTH - metr.stringWidth(lives) - 15, 15);	
	}
	
	// Display the "game over" message to the player
    private void gameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, (PANEL_HEIGHT / 2) - 35, PANEL_WIDTH - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, (PANEL_HEIGHT / 2) - 35, PANEL_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.PLAIN, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(message, (PANEL_WIDTH - metr.stringWidth(message)) / 2, (PANEL_HEIGHT / 2) - 5);
    }
	
	/**
	 * Splits an asteroid into three parts
	 * 
	 * @var Asteroid a
	 */	
	private void splitAsteroid(Asteroid a) {
		int size = a.getSize();

		// Split asteroid into three parts (if bigger than smallest 'roid)
		if (size < 2) {
			splits = new ArrayList<Asteroid>();

			for (int i = 0; i < 3; i++) {
				// Build asteroid
				int ax = (int) a.getX();
				int ay = (int) a.getY();
				int rot = (int) Math.floor(Math.random() * 360);
				int dir = (int) Math.floor(Math.random() * 360);
				
				Asteroid asteroid = new Asteroid(ax, ay, size + 1, rot, dir);

				splits.add(asteroid);

				// Add an explosion effect
				Explosion explosion = new Explosion(ax, ay, false);

				explosions.add(explosion);			
			}		
		}
		else {
			// Single explosion when smallest asteroid is "hit"
			int ax = (int) a.getX();
			int ay = (int) a.getY();

			Explosion explosion = new Explosion(ax, ay, true);

			explosions.add(explosion);			
		}
	}

	// Class to pass-thru key-press events to the player (craft)
    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            craft.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
			// only pass a key-press to the player's craft (movement
			// and fire) if the player is "alive", otherwise if the
			// player is "dead" (and the game-over message is being
			// shown), reset the game. NOTE: This needs to be re-worked,
			// as it is possible for the game to be reset "in the heat"
			// of playing
            if (craft.getLives() > 0) {
				craft.keyPressed(e);
			}
			else {
				resetGame();
			}
        }
    }
}
