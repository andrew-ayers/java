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
