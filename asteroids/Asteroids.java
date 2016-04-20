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

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Asteroids extends JFrame implements Commons {

	// Constructor initializes the JFrame UI
    public Asteroids() {
		initUI();
    }

	// Initialize the game and UI
    private void initUI() {
        
        add(new Board());
        setTitle("Asteroids");
        pack();
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);        
    }

	// Kick off timer loop for animation purposes
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {                
                JFrame ex = new Asteroids();
                ex.setVisible(true);                
            }
        });
    }
}
