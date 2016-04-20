//
// Asteroids - A simple implementation of the Asteroids game in Java
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
import java.awt.Graphics2D;
import java.awt.Toolkit;

import java.util.ArrayList;

import java.awt.event.KeyEvent;

public class Craft extends Polyshape implements Commons {
	// Position, deltas, angle of travel direction
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double rot;
    
    private boolean isVisible;
    
    private int lives; // number of remaining lives

	// List of missiles fired
    private ArrayList<Missile> missiles;

	// Constants for craft
	private static final int CRAFT_SCALE = 8;
	private static final int CRAFT_ROTSPEED = 5;
	private static final double CRAFT_ACCEL = .02;

	// X-coordinates for polyshape of craft
    private int polyx[] = {
		-1, 0, 1
    };

	// Y-coordinates for polyshape of craft
    private int polyy[] = {
		2, -2, 2
    };

	// Instantiate a Craft object (player); initialize the craft, reset
	// the number of lives (only happens at beginning of game), reset
	// the missile array
    public Craft() {
		init();

        this.lives = 3;

		this.missiles = new ArrayList<Missile>();

		// Build a new Polyshape for the craft
        initPolygon(this.polyx, this.polyy);
    }

	// Initialize the player's craft; position it at the center of the
	// play area, reset delta vectors and rotation amount, make it
	// invisible (until field is clear - see Board.java for details)
	public void init() {
        this.x = (PANEL_WIDTH / 2);
        this.y = (PANEL_HEIGHT / 2);
        this.dx = 0;
        this.dy = 0;
        this.rot = 0;
        
        this.isVisible = false;
        
        this.lives--;
        
        if (this.lives < 0)
			this.lives = 0;
	}

	// Called during the animation process (Board.java); draw the 
	// player's craft (if alive and visible)
	public void draw(Graphics2D g2d, Color color) {
		if (this.lives > 0 && this.isVisible) {
			initTransform();
			
			translatePolygon(this.x, this.y);
			rotatePolygon(this.rot);
			scalePolygon(CRAFT_SCALE);
			
			drawPolygon(g2d, color);
		}
	}
	
	// Called during the animation process (Board.java); move the 
	// player's craft in the direction needed	
    public void move() {
		if (this.lives > 0 && this.isVisible) {
			this.x += this.dx;
			this.y += this.dy;
		}
    }

	// Called during the animation process (Board.java); check the
	// boundries of the window, keeping the craft within the bounds,
	// wrapping on edges (toroid space)
	public void bounds() {
        if (this.x > PANEL_WIDTH) {
			this.x = 0;
		}
		
        if (this.x < 0) {
			this.x = PANEL_WIDTH;
		}
		
		if (this.y > PANEL_HEIGHT) {
			this.y = 0;
		}
		
        if (this.y < 0) {
			this.y = PANEL_HEIGHT;
		}
	}

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
    
    public int getLives() {
		return this.lives;
	}
	
	public boolean isVisible() {
		return this.isVisible;
	}

	public void setVisible() {
		this.isVisible = true;
	}

	// Check key-press events for fire of missiles, rotation, and 
	// acceleration
    public void keyPressed(KeyEvent e) {

		if (this.lives > 0 && this.isVisible) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_SPACE) {
				fire(); // fire missile
			}

			// rotate craft left (ccw)
			if (key == KeyEvent.VK_LEFT) {
				this.rot -= CRAFT_ROTSPEED;

				if (this.rot < 0) {
					this.rot = 360 - CRAFT_ROTSPEED;
				}
			}

			// rotate craft right (cw)
			if (key == KeyEvent.VK_RIGHT) {
				this.rot += CRAFT_ROTSPEED;

				if (this.rot > 359) {
					this.rot = CRAFT_ROTSPEED - 1;
				}
			}

			// accelerate ship in direction of rotation
			if (key == KeyEvent.VK_UP) {
				this.dx += Math.sin(Math.toRadians(this.rot)) * CRAFT_ACCEL;
				this.dy += Math.cos(Math.toRadians(this.rot) - Math.PI) * CRAFT_ACCEL;
			}

			// future use for shields
			if (key == KeyEvent.VK_DOWN) {
				//dy = 1;
			}
		}
    }

	// Fire a missle by adding it to the list
    public void fire() {
        this.missiles.add(new Missile(this.x, this.y, this.rot));
    }
    
    // Get the list of missles (for animation - see Board.java)
    public ArrayList getMissiles() {
        return this.missiles;
    }

	// Future use
    public void keyReleased(KeyEvent e) {
		if (this.lives > 0 && this.isVisible) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				//dx = 0;
			}

			if (key == KeyEvent.VK_RIGHT) {
				//dx = 0;
			}

			if (key == KeyEvent.VK_UP) {
				//dy = 0;
			}

			if (key == KeyEvent.VK_DOWN) {
				//dy = 0;
			}
		}
    }
}
