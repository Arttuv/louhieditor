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

public class OpenMapDialog extends Dialog {
	
	private TextField mapFilePathField;

	private CallBack callBack;	
	private Skin uiSkin;
	
	public OpenMapDialog(String title, Skin skin) {
		super(title, skin);
		this.uiSkin = skin;
		buildUI();
	}
	
	public OpenMapDialog(String title, Skin skin, int width, int height, CallBack listener) {
		this(title, skin);
		this.setSize(width, height);
		this.callBack = listener;
	}
	
	private void buildUI() {
				
		Table mainTable = new Table(uiSkin);
		
		Label l = new Label("Type path for map file:", uiSkin);
		
		mapFilePathField = new TextField("", uiSkin);

		mainTable.add(l).left().pad(5);
		mainTable.row();
		mainTable.add(mapFilePathField).expand().fill();

		TextButton okButton = new TextButton("OK", uiSkin);
		okButton.setName("OpenMapDialog OK");
		okButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				callBack.buttonClicked(event, actor);
			}
	    });
		
		TextButton cancelButton = new TextButton("Cancel", uiSkin);
		cancelButton.setName("OpenMapDialog Cancel");
		cancelButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				callBack.buttonClicked(event, actor);
			}
	    });
		
		getContentTable().add(mainTable);
		button(okButton);
		button(cancelButton);
		
	}

	public TextField getMapFilePathField() {
		return mapFilePathField;
	}

}
