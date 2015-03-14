package cn.clxy.codes.swing;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.application.ResourceMap;

import cn.clxy.codes.utils.ClassUtil;
import cn.clxy.codes.utils.StringUtil;

/**
 * Column informaton for table.
 * 
 * <pre>
 * Editor and renderer are bad idea!Like combobox.
 * </pre>
 * @author clxy
 */
public class ColumnInfo {

	private String title;
	private String property;
	private Class<?> clazz;
	private boolean editable = true;

	ColumnInfo(String... params) {

		this.title = params[0];
		this.property = params[1];
		this.clazz = ClassUtil.forName(params[2]);

		String editableString = params[3];
		if (StringUtil.isBlank(editableString)) {
			return;
		}
		this.editable = Boolean.valueOf(editableString);
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getProperty() {
		return property;
	}

	public String getTitle() {
		return title;
	}

	public boolean isEditable() {
		return editable;
	}

	/**
	 * <pre>
	 * Get column information from resource map.
	 * Column Format is:
	 *  table.column.#=title, property, class(Auto add java.lang and java.util), editable(Default is true)
	 * eg:
	 *  table1.column.0 = Finished, finished, Boolean
	 *  table1.column.1 = Content, content, String, false
	 * </pre>
	 * @param resourceMap
	 * @param table
	 * @return
	 */
	public static List<ColumnInfo> getColumns(ResourceMap resourceMap, String table) {

		List<ColumnInfo> columns = new ArrayList<ColumnInfo>();

		String columnPrefix = (table == null ? "" : table + ".") + prefix;
		for (int i = 0; i < limited; i++) {
			String info = resourceMap.getString(columnPrefix + i);
			if (info == null) {
				break;
			}
			String[] ary = info.split("\\s*\\,\\s*");
			String[] params = new String[4];
			System.arraycopy(ary, 0, params, 0, ary.length);

			columns.add(new ColumnInfo(params));
		}

		return columns;
	}

	/**
	 * <pre>
	 * Get column information from resource map.
	 * Column Format is:
	 *  column.#=title, property, class(Auto add java.lang and java.util), editable(Default is true)
	 * eg:
	 *  column.0 = Finished, finished, Boolean
	 *  column.1 = Content, content, String, false
	 * </pre>
	 * @param resourceMap
	 * @return
	 */
	public static List<ColumnInfo> getColumns(ResourceMap resourceMap) {
		return getColumns(resourceMap, null);
	}

	private static final int limited = 255;
	private static final String prefix = "column.";
}