package com.louhigames.editor.ui.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.louhigames.editor.CallBack;

public class MapCreationDialog extends Dialog {

	private TextField mapIdField;
	private TextField mapNameField;
	private TextField widthField;
	private TextField heightField;

	private CallBack callBack;	
	private Skin uiSkin;
	
	public MapCreationDialog(String title, Skin skin) {
		super(title, skin);
		this.uiSkin = skin;
		buildUI();
	}
	
	public MapCreationDialog(String title, Skin skin, int width, int height, CallBack listener) {
		this(title, skin);
		this.setSize(width, height);
		this.callBack = listener;
	}
	
	private void buildUI() {
				
		Table mainTable = new Table(uiSkin);
		
		Label lid = new Label("Set map id:", uiSkin);
		Label lname = new Label("Set map name:", uiSkin);
		Label lw = new Label("Set width:", uiSkin);
		Label lh = new Label("Set height:", uiSkin);
		
		TextFieldFilter filter = new TextFieldFilter.DigitsOnlyFilter();
		
		mapIdField = new TextField("", uiSkin);
		mapIdField.setTextFieldFilter(filter); 
		
		mapNameField = new TextField("", uiSkin);
		
		widthField = new TextField("", uiSkin);
		widthField.setTextFieldFilter(filter);
		
		heightField = new TextField("", uiSkin);
		heightField.setTextFieldFilter(filter);
		
		mainTable.add(lid).left().pad(5);
		mainTable.add(mapIdField);
		mainTable.row();
		mainTable.add(lname).left().pad(5);
		mainTable.add(mapNameField);
		mainTable.row();
		mainTable.add(lw).left().pad(5);
		mainTable.add(widthField);
		mainTable.row();
		mainTable.add(lh).left().pad(5);
		mainTable.add(heightField);
		
		TextButton okButton = new TextButton("OK", uiSkin);
		okButton.setName("NewMapDialog OK");
		okButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				callBack.buttonClicked(event, actor);
			}
	    });
		
		TextButton cancelButton = new TextButton("Cancel", uiSkin);
		cancelButton.setName("NewMapDialog Cancel");
		cancelButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				callBack.buttonClicked(event, actor);
			}
	    });
		
		getContentTable().add(mainTable);
		button(okButton);
		button(cancelButton);
		
	}

	public TextField getMapIdField() {
		return mapIdField;
	}

	public TextField getMapNameField() {
		return mapNameField;
	}

	public TextField getWidthField() {
		return widthField;
	}

	public TextField getHeightField() {
		return heightField;
	}

}
