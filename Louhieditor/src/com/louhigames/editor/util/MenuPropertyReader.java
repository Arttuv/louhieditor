package com.louhigames.editor.util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.louhigames.editor.objects.MenuPropertyObject;

public class MenuPropertyReader {

	public MenuPropertyReader() {
	}
	
	public ArrayList<MenuPropertyObject> read(String path) throws Exception {
		
		ArrayList<MenuPropertyObject> objects = new ArrayList<MenuPropertyObject>();
		
		FileHandle handle = Gdx.files.internal(path);
		
		String fileContents = handle.readString();
		String[] rows = fileContents.split(LouhiMenuPropertyFileSchema.PROPERTY_SEPARATOR);
		
		MenuPropertyObject prevObject = null;
		int prevLevel = 0;
		
		for (int i = 0; i < rows.length; i++) {
			
			String row = rows[i];
			
			int identifierPartEnd = row.lastIndexOf(LouhiMenuPropertyFileSchema.LEVEL_IDENTIFIER) + 1;
			int iconPartStart = row.indexOf(LouhiMenuPropertyFileSchema.ICON_PROPERTY_BLOCK[0]);
			int iconPartEnd = row.indexOf(LouhiMenuPropertyFileSchema.ICON_PROPERTY_BLOCK[1]);
			int namePartEnd = iconPartStart > 0 && iconPartStart < row.length() ? iconPartStart : row.length();
			
			String identifierPart = row.substring(0, identifierPartEnd).trim();
			String namePart = row.substring(identifierPartEnd, namePartEnd).trim();
			
			String iconPropertyPart = "";
			if (iconPartStart >= 0 && iconPartStart+1 < row.length() && iconPartEnd >= 0 && iconPartEnd < row.length()) {
				iconPropertyPart = row.substring(iconPartStart+1, iconPartEnd);
			}
			
			if (identifierPart == null || identifierPart.length() <= 0) {
				throw new Exception("FileFormatException: Identifier missing");
			}
			if (namePart == null || namePart.length() <= 0) {
				throw new Exception("FileFormatException: Name missing");
			}

			String atlasFilePath = null;
			String iconName = null;
			
			if (iconPropertyPart != null && iconPropertyPart.length() > 0) {
				
				String[] iconProperties = iconPropertyPart.split(LouhiMenuPropertyFileSchema.ITEM_SEPARATOR);
				
				if (iconProperties != null && iconProperties.length == 2) {
					atlasFilePath = iconProperties[0];
					iconName = iconProperties[1];
				}
				
			}
			
			
			MenuPropertyObject o = new MenuPropertyObject();
			o.setName(namePart);
			o.setIconAtlasPath(atlasFilePath);
			o.setIconName(iconName);
			
			int level = StringTool.occurrences(identifierPart, LouhiMenuPropertyFileSchema.LEVEL_IDENTIFIER);
			
			// root level
			if (level == 1) {
				objects.add(o);
			}
			else {
				
				// on the same level as previous
				if (level == prevLevel) {
					prevObject.getParent().addChildren(o);
				}
				// closer to root level than previous (new parent)
				else if (level < prevLevel) {
					
					if (prevObject != null) {
						prevObject.getParent().getParent().addChildren(o);
					}
					
				}
				// child to previous level
				else if (level > prevLevel) {
					
					if (prevObject != null) {
						prevObject.addChildren(o);
					}
					
				}
				
			}
			
			prevLevel = level;
			prevObject = o;
			
			//System.out.println("    " +  identifierPart);
			//System.out.println("    " +  namePart);
			//System.out.println("    atlasFilePath: " +  atlasFilePath + " iconName: " + iconName);
		}
		
		return objects;
	}

	
}
