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

public class Asteroid extends Polyshape implements Commons {
	// Size, vector movement deltas, position, rotation amount, and
	// direction (in degrees)
	private int size;
    private double dx;
    private double dy;
    private double x;
    private double y;
    private double rot;
    private int dir;

    private boolean visible; // is this asteroid visible?

	// Constants for asteroid
	private static final int ASTEROID_SCALE = 10;
	private static final double ASTEROID_ROTSPEED = .1;
	private static final double ASTEROID_SPEED = .1;
	
	// X-coordinates for polyshape of asteroid (large=0, medium=1, small=2)
	private int polyx[][] = {
		{-1, 3, 2, 3, 2, 0, -3, -4, -2},
		{-2, 2, 3, 2, 0,-3},
		{-2, 2, 3, 2}
	};

	// Y-coordinates for polyshape of asteroid (large=0, medium=1, small=2)
	private int polyy[][] = {
		{-5, -4, -2, 0, 1, 2, 1, -2, -3},
		{-3, -2, 0, 1, 2, 1},
		{-3, -2, 0, 1}
	};

	// Instantiate an Asteroid object; set starting coordinates, size,
	// initial rotation amount (in degrees), and initial direction of
	// rotation (cw, ccw, or none)
    public Asteroid(double ax, double ay, int size, int rot, int dir) {
		this.visible = true;

		double speed = ASTEROID_SPEED * (size + 1);
		
		this.size = size;
        this.x = ax;
        this.y = ay;
		this.dx = Math.sin(Math.toRadians(dir)) * speed;
		this.dy = Math.cos(Math.toRadians(dir) - Math.PI) * speed;
		this.dir = (int) Math.floor(Math.random() * 3) - 1;
        
        this.rot = (double) rot;

		// Build a new Polyshape for this asteroid
        initPolygon(this.polyx[size], this.polyy[size]);
    }
    
	// Called during the animation process (Board.java); draw the
	// asteroid with the color based on size
	public void draw(Graphics2D g2d, Color color) {
		// Need to re-initialize transform on each frame, otherwise
		// things get wonky
		initTransform();
		
		// Move, rotate, and scale the asteroid at its position
		translatePolygon(this.x, this.y);
		rotatePolygon(this.rot);
		scalePolygon(ASTEROID_SCALE);
		
		// Alter of asteroid color based on size 
		switch (this.size) {
			case 0: 
				color = Color.red;
				break;
		    case 1:
		        color = Color.blue;
		        break;
		    case 2:
				color = Color.orange;
		}		
		
		// Draw the asteroid
		drawPolygon(g2d, color);
	}
	
	// Called during the animation process (Board.java); move the
	// asteroid by the vector movement delta amounts, and update 
	// the rotation amount
    public void move() {
        this.x += this.dx;
        this.y += this.dy;
        this.rot += (ASTEROID_ROTSPEED * (this.size + 1)) * this.dir;
        
        if (this.rot > 359)
			this.rot = 1;
    }

	// Called during the animation process (Board.java); check the
	// boundries of the window, keeping the asteroid within the bounds,
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

    public boolean isVisible() {
        return this.visible;
    }

    public void inVisible() {
        this.visible = false;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
    
    public int getSize() {
		return this.size;
	}
}
