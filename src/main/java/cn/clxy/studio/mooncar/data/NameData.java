package cn.clxy.studio.mooncar.data;

import java.util.Date;

import cn.clxy.studio.common.data.BaseData;

public class NameData extends BaseData {

	private String name;
	private int count;

	private OriginNameData origin = new OriginNameData();

	public NameData() {
	}

	public NameData(String name) {
		this.name = name;
	}

	public void countUp() {
		this.count++;
	}

	public void mergeOrigin(OriginNameData origin) {
		if (isFirst(origin)) {
			this.origin = origin;
		}
	}

	public Date getEarliest() {
		return origin.getCreateAt();
	}

	public void setEarliest(Date date) {
		origin.setCreateAt(date);
	}

	public String getOriginName() {
		return origin.getName();
	}

	public void setOriginName(String name) {
		origin.setName(name);
	}

	public String getAuthor() {
		return origin.getCreateBy();
	}

	public void setAuthor(String author) {
		origin.setCreateBy(author);
	}

	public Integer getOriginId() {
		return origin.getOriginId();
	}

	public void setOriginId(Integer originId) {
		origin.setOriginId(originId);
	}

	public String getSite() {
		return origin.getSite();
	}

	public void setSite(String site) {
		origin.setSite(site);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private boolean isFirst(OriginNameData other) {

		if (origin == other) {
			return false;
		}

		Date earlist = getEarliest();
		if (earlist == null) {
			return true;
		}
		return earlist.compareTo(other.getCreateAt()) > 0;
	}

	private static final long serialVersionUID = 1L;
}
