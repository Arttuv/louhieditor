package com.louhigames.editor.ui.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.louhigames.editor.objects.MapCellObject;

public class MapCellButton extends Button {

	private MapCellObject mapCellObject;

	public MapCellButton(ButtonStyle buttonStyle, MapCellObject mapCellObject) {
		super(buttonStyle);
		this.mapCellObject = mapCellObject;
	}

	public MapCellObject getMapCellObject() {
		return mapCellObject;
	}

	public void setMapCellObject(MapCellObject mapCellObject) {
		this.mapCellObject = mapCellObject;
	}
	
	
	
}
