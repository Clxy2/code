/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.clxy.codes.swing;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * <a href="http://www.javaspecialists.eu/archive/Issue106.html">See here</a>
 * @author clxy
 */
public class TextAreaEditor extends DefaultCellEditor {

    private JTable table;
    private int row;

    public TextAreaEditor() {

        super(new JTextField());
        final JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                resize(e);
            }

            public void removeUpdate(DocumentEvent e) {
                resize(e);
            }

            public void changedUpdate(DocumentEvent e) {
                resize(e);
            }

            private void resize(DocumentEvent e) {
                resizeRowHeight(textArea, table, row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        editorComponent = scrollPane;

        delegate = new DefaultCellEditor.EditorDelegate() {

            @Override
            public void setValue(Object value) {
                textArea.setText((value != null) ? value.toString() : "");
            }

            @Override
            public Object getCellEditorValue() {
                return textArea.getText();
            }
            /**
             * Default serial version.
             */
            private static final long serialVersionUID = 1L;
        };
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {

        this.table = table;
        this.row = row;

        Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        resizeRowHeight(editor, table, row);
        return editor;
    }

    /**
     * Set table row height by specified component.
     * @param component
     * @param table
     * @param row
     */
    private static void resizeRowHeight(Component component, JTable table, int row) {

        if (table == null) {
            return;
        }
        int rowHeight = table.getRowHeight(row);
        int textHeight = (int) component.getPreferredSize().getHeight();

        if (textHeight > rowHeight) {
            table.setRowHeight(row, textHeight);
        }
    }
    /**
     * Default serial version.
     */
    private static final long serialVersionUID = 1L;
}
