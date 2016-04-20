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
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Missile extends Polyshape implements Commons {
	// Position, deltas, angle of travel direction, and distance
	// to travel
    private double x;
    private double y;
    private double dx;
    private double dy;
    private int dist;

    private boolean visible;

    private final int MISSILE_DISTANCE = 150; // maximum travel distance
    private final int MISSILE_SPEED = 2; // speed of missile

	// Instantiate a Missile object; set visibility, initial position,
	// direction deltas, and maximum travel distance
    public Missile(double x, double y, double rot) {
        this.visible = true;
        
        this.x = x;
        this.y = y;

		// Calculate direction deltas based on angle of rotation (of the
		// player's craft)
		this.dx = Math.sin(Math.toRadians(rot)) * MISSILE_SPEED;
		this.dy = Math.cos(Math.toRadians(rot) - Math.PI) * MISSILE_SPEED;
        
        this.dist = MISSILE_DISTANCE;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void inVisible() {
        this.visible = false;
    }

	// Called during the animation process (Board.java); move the missle
	// in the direction fired
    public void move() {
		this.x += dx;
		this.y += dy;
		
		// Only allow a missile to travel so far before disappearing
		this.dist--;

        if (this.dist <= 0)
            this.visible = false;
    }
    
	// Called during the animation process (Board.java); check the
	// boundries of the window, keeping the missile within the bounds,
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
    
    // Draw a missile on the screen
	protected void drawMissile(Graphics2D g2d, Color color) {
		g2d.setColor(color);
		//g2d.setStroke(new BasicStroke(1));
		g2d.drawLine((int) this.x, (int) this.y, (int) this.x, (int) this.y);
	}    
}
