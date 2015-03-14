package cn.clxy.codes.swing;

import java.awt.Component;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.TaskService;

/**
 * Utils for operating application.
 * 
 * @author clxy
 */
public final class SwingUtil {

	public static void packRows(JTable table) {

		for (int row = 0, r = table.getRowCount(); row < r; row++) {

			int height = table.getRowHeight();

			for (int column = 0, c = table.getColumnCount(); column < c; column++) {
				Component comp = table.prepareRenderer(
						table.getCellRenderer(row, column), row, column);
				height = Math.max(height, comp.getPreferredSize().height);
			}

			if (height != table.getRowHeight())
				table.setRowHeight(row, height);
		}
	}

	public static void setTableHeaderAlignment(JTable table, int hAlignment) {

		JTableHeader header = table.getTableHeader();
		TableCellRenderer headerRenderer = header.getDefaultRenderer();
		if (headerRenderer instanceof JLabel) {
			((JLabel) headerRenderer).setHorizontalAlignment(hAlignment);
		}
	}

	/**
	 * For making code shorter.
	 * 
	 * @param root
	 * @param clazz
	 * @return
	 */
	public static ResourceMap getResource(Application root, Class<?> clazz) {
		return root.getContext().getResourceMap(clazz);
	}

	/**
	 * For making code shorter.
	 * 
	 * @param root
	 * @param clazz
	 * @return
	 */
	public static ResourceMap getResource(Class<? extends Application> root,
			Class<?> clazz) {
		return getResource(Application.getInstance(root), clazz);
	}

	/**
	 * @param rm
	 * @param key
	 * @return
	 */
	public static Image getImageBy(ResourceMap rm, String key) {

		try {

			ImageIcon ii = rm.getImageIcon(key);
			return (ii == null) ? null : ii.getImage();
		} catch (Exception e) {
			log.warn(e);
			return null;
		}
	}

	/**
	 * Execute a task handle.
	 * 
	 * @param task
	 */
	public static <T, V> void executeTask(Task<T, V> task) {

		ApplicationContext ac = task.getApplication().getContext();

		TaskMonitor tm = ac.getTaskMonitor();
		TaskService ts = ac.getTaskService();
		tm.setForegroundTask(task);
		ts.execute(task);
	}

	/**
	 * Get specified parent.
	 * 
	 * @param <T>
	 * @param component
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getParent(Component component, Class<T> clazz) {

		if (component == null) {
			return null;
		}

		if (clazz.isInstance(component)) {
			return (T) component;
		}

		return getParent(component.getParent(), clazz);
	}

	/**
	 * Close the parent window.
	 * 
	 * @see Window#dispose()
	 * @param component
	 */
	public static void disposeWindow(Component component) {

		if (component == null) {
			return;
		}

		Window window = getParent(component, Window.class);
		if (window == null) {
			return;
		}

		window.dispose();
	}

	private SwingUtil() {
	}

	private static final Log log = LogFactory.getLog(SwingUtil.class);
}
