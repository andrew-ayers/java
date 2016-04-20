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
