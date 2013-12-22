package com.louhigames.editor.ui.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.louhigames.editor.CallBack;

public class MenuCellPropertyDialog extends Dialog {

	private TextField keyField;
	private TextField valueField;

	private CallBack callBack;	
	private Skin uiSkin;
	
	public MenuCellPropertyDialog(String title, Skin skin, CallBack listener) {
		super(title, skin);
		this.uiSkin = skin;
		this.callBack = listener;
		buildUI();
	}
	
	public MenuCellPropertyDialog(String title, Skin skin, int width, int height, CallBack listener) {
		this(title, skin, listener);
		this.setSize(width, height);
		this.callBack = listener;
	}
	
	private void buildUI() {
				
		Table mainTable = new Table(uiSkin);
		
		Label keyLabel = new Label("Set key:", uiSkin);
		Label valueLabel = new Label("Set def. value:", uiSkin);

		keyField = new TextField("", uiSkin);
		valueField = new TextField("", uiSkin);
		
		mainTable.add(keyLabel).left().pad(5);
		mainTable.add(keyField);
		mainTable.row();
		mainTable.add(valueLabel).left().pad(5);
		mainTable.add(valueField);

		
		TextButton okButton = new TextButton("OK", uiSkin);
		okButton.setName("MenuCellPropertyDialog OK");
		okButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				callBack.buttonClicked(event, actor);
			}
	    });
		
		TextButton cancelButton = new TextButton("Cancel", uiSkin);
		cancelButton.setName("MenuCellPropertyDialog Cancel");
		cancelButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				callBack.buttonClicked(event, actor);
			}
	    });
		
		getContentTable().add(mainTable);
		button(okButton);
		button(cancelButton);
		
	}

	public TextField getKeyField() {
		return keyField;
	}

	public TextField getValueField() {
		return valueField;
	}


}
