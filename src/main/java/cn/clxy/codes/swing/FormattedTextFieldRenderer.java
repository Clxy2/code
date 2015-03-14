package cn.clxy.codes.swing;

import java.awt.Component;
import java.text.Format;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * TableCellRender using JFormattedTextField.
 * @author clxy
 */
public class FormattedTextFieldRenderer implements TableCellRenderer {

	private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();
	private JFormattedTextField component;

	public FormattedTextFieldRenderer() {
		component = new JFormattedTextField();
	}

	public FormattedTextFieldRenderer(Format format) {
		component = new JFormattedTextField(format);
	}

	public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected,
			boolean hasFocus, int row, int column) {

		adaptee.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
		component.setForeground(adaptee.getForeground());
		component.setBackground(adaptee.getBackground());
		component.setBorder(adaptee.getBorder());
		component.setFont(adaptee.getFont());

		// TODO Cann't set center by hard coding.
		component.setHorizontalAlignment(JTextField.CENTER);

		component.setValue(obj);

		return component;
	}
}
