package cn.clxy.codes.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * <a href="http://www.javaspecialists.eu/archive/Issue106.html">See here</a>
 * 
 * @author clxy
 */
public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

	private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();

	public TextAreaRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column) {

		// set the colours, etc. using the standard for that platform
		adaptee.getTableCellRendererComponent(table, obj, isSelected, hasFocus,
				row, column);
		setForeground(adaptee.getForeground());
		setBackground(adaptee.getBackground());
		setBorder(adaptee.getBorder());
		setFont(adaptee.getFont());
		setText(adaptee.getText());

		TableColumnModel columnModel = table.getColumnModel();
		setSize(columnModel.getColumn(column).getWidth(), 100000);

		return this;
	}

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
}