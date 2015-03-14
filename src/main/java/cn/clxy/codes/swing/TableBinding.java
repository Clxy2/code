package cn.clxy.codes.swing;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.jdesktop.swingbinding.SwingBindings;

/**
 * JTableBinding delegate.<br>
 * For resolving JTableBinding reset every thing after rebind.
 * @author clxy
 */
public final class TableBinding {

    private JTable table;
    @SuppressWarnings("rawtypes")
	private JTableBinding tableBinding;
    private Map<Object, Column> columns;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public TableBinding(UpdateStrategy strategy, List list, JTable table) {
        this.table = table;
        tableBinding = SwingBindings.createJTableBinding(strategy, list, table);
    }

    public final void unbind() {

        if (!tableBinding.isBound()) {
            return;
        }
        getColumnInfo();
        tableBinding.unbind();
    }

    public final void bind() {

        if (tableBinding.isBound()) {
            return;
        }
        tableBinding.bind();
        setColumnInfo();
    }

    /**
     * Create and add ColumnBinding.
     * @param infos
     * @param table
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setColumnBinding(List<ColumnInfo> infos, TableBinding table) {

        for (ColumnInfo info : infos) {
            ColumnBinding binding = table.addColumnBinding(BeanProperty.create(info.getProperty()));
            binding.setColumnClass(info.getClazz());
            binding.setColumnName(info.getTitle());
            binding.setEditable(info.isEditable());
        }
    }

    /**
     * Collect current information.Because that Binding reset everything!
     */
    private void getColumnInfo() {

        columns = new HashMap<Object, Column>();
        for (Enumeration<TableColumn> tableColumns = table.getColumnModel().getColumns();
                tableColumns.hasMoreElements();) {

            TableColumn tableColumn = tableColumns.nextElement();
            Column column = new Column();
            column.setWidth(tableColumn.getWidth());
            column.setEditor(tableColumn.getCellEditor());
            column.setRenderer(tableColumn.getCellRenderer());
            columns.put(tableColumn.getHeaderValue(), column);
        }
    }

    /**
     * Set information back.
     * @param widths
     */
    private void setColumnInfo() {

        if (columns == null) {
            return;
        }

        for (Enumeration<TableColumn> tableColumns = table.getColumnModel().getColumns();
                tableColumns.hasMoreElements();) {

            TableColumn tableColumn = tableColumns.nextElement();
            //Use head value for key.
            Column column = columns.get(tableColumn.getHeaderValue());
            if (column == null) {
                continue;
            }

            tableColumn.setPreferredWidth(column.getWidth());

            TableCellEditor editor = column.getEditor();
            if (editor != null) {
                tableColumn.setCellEditor(editor);
            }

            TableCellRenderer renderer = column.getRenderer();
            if (renderer != null) {
                tableColumn.setCellRenderer(renderer);
            }
        }
    }

    /**
     * Contain the information that will be reset by JTableBinding.
     */
    private static class Column {

        private int width;
        private TableCellEditor editor;
        private TableCellRenderer renderer;

        public TableCellEditor getEditor() {
            return editor;
        }

        public void setEditor(TableCellEditor editor) {
            this.editor = editor;
        }

        public TableCellRenderer getRenderer() {
            return renderer;
        }

        public void setRenderer(TableCellRenderer renderer) {
            this.renderer = renderer;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ColumnBinding addColumnBinding(Property columnProperty) {
        return tableBinding.addColumnBinding(columnProperty);
    }

    public final SyncFailure refresh() {
        return tableBinding.refresh();
    }

    @SuppressWarnings("rawtypes")
	public JTableBinding getBinding() {
        return tableBinding;
    }
}
