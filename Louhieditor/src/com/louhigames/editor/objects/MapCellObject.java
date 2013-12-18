package com.louhigames.editor.objects;

import java.util.HashMap;

public class MapCellObject {

	private String cellType;
	private final int x;
	private final int y;
	
	private HashMap<String, String> properties;
	
	public MapCellObject(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void addProperty(String key, String value) {
		
		if (! properties.containsKey(key)) {
			properties.put(key, value);
		}
		
	}
	
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	
	
}
