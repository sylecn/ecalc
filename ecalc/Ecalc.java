package ecalc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
 * The end user UI
 */
public class Ecalc extends JFrame
	implements ActionListener, KeyListener, IFScreen {

	JTextArea displayArea;
	JTextField typingArea;
	static final String newline = System.getProperty("line.separator");
	
	private static Machine m;
	private static Keyboard k;
	private static Screen s;

	private static void debug(String msg) {
		System.out.println(msg);
	}
	
	public static void main(String[] args) {
		debug("Ecalc starts.");

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});

		debug("exit main.");
	}

	public Ecalc(String name) {
		super(name);
	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		Ecalc frame = new Ecalc("Ecalc");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m = new Machine();
		k = new Keyboard();
		k.connectToMachine(m);
		s = new Screen();
		m.addScreen((IFScreen)s);

		m.addScreen((IFScreen)frame);
        
		//Set up the content pane.
		frame.addComponentsToPane();
        
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private void addComponentsToPane() {
        
		JButton button = new JButton("Clear");
		button.addActionListener(this);
        
		typingArea = new JTextField(20);
		typingArea.addKeyListener(this);
        
		displayArea = new JTextArea();
		displayArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setPreferredSize(new Dimension(375, 125));
        
		getContentPane().add(typingArea, BorderLayout.PAGE_START);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(button, BorderLayout.PAGE_END);
	}

	public void actionPerformed(ActionEvent e) {
		//Clear the text components.
		displayArea.setText("");
		typingArea.setText("");
        
		//Return the focus to the typing area.
		typingArea.requestFocusInWindow();
	}

	/**
	 * press key on Keyboard according to KeyEvent char key
	 */
	private void pressKeychar(char key) {
		try {
			String numbers = "0123456789._";
			//- means subtract, _ means minus
			if (numbers.indexOf(key) != -1) {
				k.pressNumberKeys("" + key);
				return;
			}
			//c means clear, b means backspace
			String ops = "+-*/cb";
			if (ops.indexOf(key) != -1) {
				k.pressOpKey("" + key);
				return;
			}
			
		} catch (NoNumberKeyForGivenNumber e) {
			debug(e.getMessage());
		} catch (NoOpKeyForGivenString e) {
			debug(e.getMessage());
		}
	}
	
	public void keyTyped(KeyEvent e) {
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyPressed(KeyEvent e){
		pressKeychar(e.getKeyChar());
	}

	public void toggleMinusSign() {}
	public void resetMinusSign(boolean minus) {}
	public void updateOp(Keys op) {}
	public void updateMainPanel(String num) {}
	
	public void clear() {}
	
	public void clearErrorMsg() {}
	public void setErrorMsg(String msg) {}

	/**
	 * TODO add screen inside Machine class.
	 * maybe add screen to a component/variable of Machine.
	 * external screen use Machine.Screen.variables help them display.
	 * they only need to know how to display the value.
	 * they should not know what is the value now.
	 *
	 * keep my IFScreen interface.
	 */
	
	public void updateScreen() {
		displayArea.setText("new value" + s.main_panel + newline);
	}
}
