package com.louhigames.editor.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapCellObject {

	private final int x;
	private final int y;
	
	private MenuPropertyObject menuPropertyObject;
	private HashMap<String, String> properties;
	
	public MapCellObject(int x, int y) {
		this.x = x;
		this.y = y;
		properties = new HashMap<String, String>();
	}

	public String getCellType() {
		if (menuPropertyObject == null) return null;
		return menuPropertyObject.getName();
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	/*public void addProperty(String key, String value) {
		
		if (! properties.containsKey(key)) {
			properties.put(key, value);
		}
		
	}*/
	
	/*public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}*/

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public MenuPropertyObject getMenuPropertyObject() {
		return menuPropertyObject;
	}

	public void setMenuPropertyObject(MenuPropertyObject menuPropertyObject) {
		this.menuPropertyObject = menuPropertyObject;
		
		if (menuPropertyObject != null) {
			refreshProperties(false);
		}
	}
	
	public void refreshProperties(boolean overwriteExistingValues) {
		
		System.out.println("Refreshing...");
		
		HashMap<String, String> menuProperties = menuPropertyObject.getProperties();
		
		Iterator it = menuProperties.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        
	        if (! properties.containsKey(pairs.getKey())) {
	        	properties.put(pairs.getKey(), pairs.getValue());
	        }
	        else {
	        	
	        	if (overwriteExistingValues) {
	        		properties.remove(pairs.getKey());
	        		properties.put(pairs.getKey(), pairs.getValue());
	        	}
	        	
	        }

	    }
	    
	}
	
}
