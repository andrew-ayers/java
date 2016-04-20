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
import java.awt.Shape;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Polyshape implements Commons {

    private Polygon poly;
    private AffineTransform at;

	// Initialize a polygon shape using X/Y vertex lists
	protected void initPolygon(int polyx[], int polyy[]) {
		initTransform();
		
        poly = new Polygon(polyx, polyy, polyx.length);
	}
	
	// Initialize the transform object
	protected void initTransform() {
        at = new AffineTransform();
	}
	
	// Apply scaling to the transform object
	protected void scalePolygon(int scale) {
        at.scale(scale, scale);
	}

	// Apply translation to the transform object
	protected void translatePolygon(double x, double y) {
        at.translate(x, y);
	}
	
	// Apply rotation to the transform object
	protected void rotatePolygon(double angle) {
		Rectangle brect = poly.getBounds();

        at.rotate(Math.toRadians(angle), brect.getX() + brect.width/2, brect.getY() + brect.height/2);
	}

	// Draw the polygon using the defined color and transforms
	protected void drawPolygon(Graphics2D g2d, Color color) {
		g2d.setColor(color);
		//g2d.setStroke(new BasicStroke(1));        
        g2d.draw(at.createTransformedShape(poly));
	}
	
	// Get the rectangular boundry of the transformed shape
	protected Rectangle getBBounds() {
		return at.createTransformedShape(poly).getBounds();
	}
}
