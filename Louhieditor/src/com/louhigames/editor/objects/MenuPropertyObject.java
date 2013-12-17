package com.louhigames.editor.objects;

import java.util.ArrayList;

public class MenuPropertyObject {

	private ArrayList<MenuPropertyObject> childrenObjects;
	private MenuPropertyObject parent;
	
	private String name;
	private String iconAtlasPath;
	private String iconName;
	
	public MenuPropertyObject() {
		this("");
	}
	
	public MenuPropertyObject(String name) {
		this.name = name;
		childrenObjects = new ArrayList<MenuPropertyObject>();
	}
	
	public void addChildren(MenuPropertyObject children) {
		
		if (childrenObjects == null) childrenObjects = new ArrayList<MenuPropertyObject>();
		
		children.setParent(this);
		childrenObjects.add(children);
		
	}

	public ArrayList<MenuPropertyObject> getChildrenObjects() {
		return childrenObjects;
	}

	public void setChildrenObjects(ArrayList<MenuPropertyObject> childrenObjects) {
		this.childrenObjects = childrenObjects;
	}

	public MenuPropertyObject getParent() {
		return parent;
	}

	public void setParent(MenuPropertyObject parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconAtlasPath() {
		return iconAtlasPath;
	}

	public void setIconAtlasPath(String iconAtlasPath) {
		this.iconAtlasPath = iconAtlasPath;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	
}
