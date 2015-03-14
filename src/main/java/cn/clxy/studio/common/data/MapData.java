package cn.clxy.studio.common.data;

import java.util.HashMap;
import java.util.Map;

public class MapData<K, V> extends HashMap<K, V> {

	public MapData() {
		super();
	}

	public MapData(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public MapData(int initialCapacity) {
		super(initialCapacity);
	}

	public MapData(Map<? extends K, ? extends V> m) {
		super(m);
	}

	public MapData(K key, V value) {
		this();
		put(key, value);
	}

	private static final long serialVersionUID = 1L;
}
