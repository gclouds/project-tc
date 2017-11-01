package domain;

import java.io.Serializable;
import java.util.List;

import constants.ItemType;

public class TreeItem implements Serializable {
	private static final long serialVersionUID = 5403380680838155670L;
	private ItemType type;
	private String key;
	private Object value;
	private List<TreeItem> child;
	
	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public List<TreeItem> getChild() {
		return child;
	}
	public void setChild(List<TreeItem> child) {
		this.child = child;
	}
	
	
	
}
