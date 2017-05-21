import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JPanel_Background extends JPanel {
	private Image image = null;

	public JPanel_Background(Image image) {
		this.image = image;
	}

	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
