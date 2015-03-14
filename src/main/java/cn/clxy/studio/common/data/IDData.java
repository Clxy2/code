package cn.clxy.studio.common.data;

import java.io.Serializable;

public abstract class IDData implements Serializable {

	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private static final long serialVersionUID = 1L;
}
