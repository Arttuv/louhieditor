package com.louhigames.editor.objects;

import java.util.ArrayList;

public class MapPropertyObject {

	private ArrayList<MapPropertyObject> childrenObjects;
	
	private String useValue;
	private String displayValue;
	
	public MapPropertyObject(String useValue, String displayValue) {
		this.useValue = useValue;
		this.displayValue = displayValue;
		
		childrenObjects = new ArrayList<MapPropertyObject>();
	}
	
	public void addChildren(MapPropertyObject children) {
		
		if (childrenObjects == null) childrenObjects = new ArrayList<MapPropertyObject>();
		
		childrenObjects.add(children);
		
	}

	public ArrayList<MapPropertyObject> getChildrenObjects() {
		return childrenObjects;
	}

	public void setChildrenObjects(ArrayList<MapPropertyObject> childrenObjects) {
		this.childrenObjects = childrenObjects;
	}

	public String getUseValue() {
		return useValue;
	}

	public void setUseValue(String useValue) {
		this.useValue = useValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	
}
