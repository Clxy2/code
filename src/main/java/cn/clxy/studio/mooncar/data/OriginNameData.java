package cn.clxy.studio.mooncar.data;

import cn.clxy.studio.common.data.BaseData;

public class OriginNameData extends BaseData {

	private Integer originId;

	private String name;

	private String site;

	public static final OriginNameData NULL = new OriginNameData();

	public OriginNameData() {
	}

	public OriginNameData(Integer originId, String name, String site) {
		this.originId = originId;
		this.name = name;
		this.site = site;
	}

	public Integer getOriginId() {
		return originId;
	}

	public void setOriginId(Integer originId) {
		this.originId = originId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Override
	public String toString() {
		return "OriginNameData [originId=" + originId + ", name=" + name + ", site=" + site + "]";
	}

	private static final long serialVersionUID = 1L;
}
