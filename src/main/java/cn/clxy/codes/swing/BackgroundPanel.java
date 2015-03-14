package cn.clxy.codes.swing;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import org.jdesktop.application.Application;

/**
 * JPanel with background.
 * @author clxy
 */
public class BackgroundPanel extends JPanel {

	private static final String key_background = "background";

	protected Image backgroundImg;

	public BackgroundPanel() {

		super();
		backgroundImg = SwingUtil.getImageBy(
				SwingUtil.getResource(Application.class, getClass()),
				key_background);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		if (backgroundImg == null) {
			return;
		}
		g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);
	}

	public Image getBackgroundImg() {
		return backgroundImg;
	}

	public void setBackgroundImg(Image backgroundImg) {
		this.backgroundImg = backgroundImg;
	}

	private static final long serialVersionUID = 1L;
}