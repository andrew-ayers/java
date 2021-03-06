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
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Explosion extends Polyshape implements Commons {
	// Location and frame counter
    private double x;
    private double y;
    private int frame;

    private boolean visible; // is this explosion "visible"

    private final int MAX_SIZE = 25; // maximum radius of explosion

	// Instantiate an Explosion object; make visible, and set the
	// position of the explosion. Also, centering of the explosion
	// or random position near center may be flagged.
    public Explosion(double x, double y, boolean centered) {
        this.visible = true;
        
        this.x = x;
        this.y = y;
        
        if (!centered) {
			this.x += Math.floor(Math.random() * 24) - 12;
			this.y += Math.floor(Math.random() * 24) - 12;
		}
		
        this.frame = 0;
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
    
    // Called during the animation process (Board.java); if the
    // explosion is visible, draw a circle with a radius of the
    // "frame" count, to the maximum allowed size. Also set the
    // color based on radius
	protected void draw(Graphics2D g2d) {
		if (this.visible) {
			this.frame++;
			
			if (this.frame <= MAX_SIZE) {
				if (frame < 5) {
					g2d.setColor(Color.WHITE);
				}
				else if (frame < 10) {
					g2d.setColor(Color.YELLOW);
				}
				else if (frame < 15) {
					g2d.setColor(Color.ORANGE);
				}
				else {
					g2d.setColor(Color.RED);
				}
				
				Shape circle = new Ellipse2D.Double(this.x - frame, this.y - frame, 2.0 * frame, 2.0 * frame);
				
				g2d.draw(circle);				
			}
			else {
				this.visible = false;
			}
		}
	}    
}
