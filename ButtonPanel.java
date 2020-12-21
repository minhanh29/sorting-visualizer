import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class ButtonPanel extends JPanel
{
	public static final long serialVersionUID = 1L;
	private static final int BUTTON_WIDTH = 200, BUTTON_HEIGHT = 80;
	private JLabel[] buttons;
	private Visualizer visualizer;
	private int number = 6;

	public ButtonPanel()
	{
		super();

		buttons = new JLabel[number];
		for (int i = 0; i < buttons.length; i++)
			buttons[i] = new JLabel();

		initButtons(buttons[0], "create_button", 0);
		initButtons(buttons[1], "bubble_button", 1);
		initButtons(buttons[2], "selection_button", 2);
		initButtons(buttons[3], "insertion_button", 3);
		initButtons(buttons[4], "quick_button", 4);
		initButtons(buttons[5], "merge_button", 5);

		// add button to the panel
		setLayout(null);
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].setBounds(20, 20 + (BUTTON_HEIGHT + 5) * i, BUTTON_WIDTH, BUTTON_HEIGHT);
			add(buttons[i]);
		}
	}


	private void initButtons(JLabel button, String name, int id)
	{
		button.setIcon(new ImageIcon(String.format("buttons/%s.png", name)));
		button.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("buttons/%s_entered.png", name)));
			}

			public void mouseExited(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("buttons/%s.png", name)));
			}

			public void mousePressed(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("buttons/%s_pressed.png", name)));
			}

			public void mouseReleased(MouseEvent e) {
				buttonClicked(id);
				button.setIcon(new ImageIcon(String.format("buttons/%s_entered.png", name)));
			}
		});
	}


	private void buttonClicked(int id)
	{
		switch (id)
		{
			case 0:  // create button
				visualizer.createRandomArray();
				break;
			case 1:  // bubble button
				visualizer.bubbleSort();
				break;
			case 2:  // selection button
				visualizer.selectionSort();
				break;
			case 3:  // insertion button
				visualizer.insertionSort();
				break;
			case 4:  // quick button
				visualizer.quickSort();
				break;
			case 5:  // merge button
				visualizer.mergeSort();
				break;

		}
	}


	public void setVisualizer(Visualizer visualizer) {
		this.visualizer = visualizer;
	}
}
