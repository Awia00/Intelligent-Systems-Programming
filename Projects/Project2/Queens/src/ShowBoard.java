/**
 * This class initializes the logic and shows the board. 
 * @author Stavros Amanatidis
 *
 */
import javax.swing.*;

public class ShowBoard {
	public static void main(String[] arg) {
		QueensLogic l = new QueensLogic();

		int size;

		if (arg.length == 0)
			size = 8;
		else
			size = Integer.parseInt(arg[0]);

		l.initializeGame(size);

		QueensGUI g = new QueensGUI(l);

		// Setup of the frame containing the game
		JFrame f = new JFrame();
		f.setSize(200 + size * 100, 200 + size * 100);
		f.setTitle("Queens Problem");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.getContentPane().add(g);
		f.setVisible(true);
	}
}