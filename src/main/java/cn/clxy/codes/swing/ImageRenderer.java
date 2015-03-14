package cn.clxy.codes.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ImageRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);

		label.setText(null);

		Icon icon = null;
		if (Icon.class.isInstance(value)) {
			icon = (Icon) value;
		} else if (Image.class.isInstance(value)) {
			icon = new ImageIcon((Image) value);
		} else {
			return label;
		}

		label.setIcon(icon);

		if (icon != null) {
			label.setPreferredSize(new Dimension(icon.getIconWidth(), icon
					.getIconHeight()));
		}

		return label;
	}

	private static final long serialVersionUID = 1L;
}