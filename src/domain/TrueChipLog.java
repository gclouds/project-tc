package domain;

import java.io.Serializable;
import java.util.List;

import logger.DataList;

public class TrueChipLog implements Serializable {

	private static final long serialVersionUID = -7680352874118668686L;
	private List<String> primaryTable;
	private List<String> secondaryTable;
	private List<DataList> data;

	public List<String> getPrimaryTable() {
		return primaryTable;
	}

	public void setPrimaryTable(List<String> primaryTable) {
		this.primaryTable = primaryTable;
	}

	public List<String> getSecondaryTable() {
		return secondaryTable;
	}

	public void setSecondaryTable(List<String> secondaryTable) {
		this.secondaryTable = secondaryTable;
	}

	public List<DataList> getData() {
		return data;
	}

	public void setData(List<DataList> data) {
		this.data = data;
	}

}
