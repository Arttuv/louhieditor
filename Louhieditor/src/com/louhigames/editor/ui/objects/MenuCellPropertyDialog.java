package com.louhigames.editor.ui.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.louhigames.editor.CallBack;
import com.louhigames.editor.objects.MenuPropertyObject;

public class MenuCellPropertyDialog extends Dialog {

	private TextField keyField;
	private TextField valueField;

	private MenuPropertyObject menuPropertyObject;
	
	private CallBack callBack;	
	private Skin uiSkin;
	private boolean isAddMode;
	
	private HashMap<String, CheckBox> removedProperties;
	
	public MenuCellPropertyDialog(String title, Skin skin, MenuPropertyObject menuPropertyObject, CallBack listener, boolean isAddMode) {
		super(title, skin);
		this.uiSkin = skin;
		this.menuPropertyObject = menuPropertyObject;
		this.callBack = listener;
		this.isAddMode = isAddMode;
		buildUI();
	}
	
	private void buildUI() {
		
		Table mainTable = null;
		String okButtonName = "";
		
		if (isAddMode) {
			mainTable = buildAddModeUI();
			okButtonName = "MenuCellPropertyDialog Add OK";
		}
		else {
			mainTable = buildRemoveModeUI();
			okButtonName = "MenuCellPropertyDialog Del OK";
		}

		TextButton okButton = new TextButton("OK", uiSkin);
		okButton.setName(okButtonName);
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
		
		getContentTable().add(mainTable).fill().expand().padTop(2).padBottom(20);
		button(okButton);
		button(cancelButton);
		
	}

	private Table buildAddModeUI() {
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
		
		return mainTable;
	}
	
	private Table buildRemoveModeUI() {
		Table mainTable = new Table(uiSkin);
		
		mainTable.defaults().space(0).pad(0.5f);
		
		HashMap<String, String> properties = menuPropertyObject.getProperties();
		removedProperties = new HashMap<String, CheckBox>();
	
		Label keyTitle = new Label("Key", uiSkin, "property-header");
		keyTitle.setAlignment(Align.center, Align.center);
		Label valueTitle = new Label("Default Value", uiSkin, "property-header");
		valueTitle.setAlignment(Align.center, Align.center);
		
		mainTable.add(new Label("", uiSkin, "property-header")).center().fillX().expandX().height(25);
		mainTable.add(keyTitle).center().fillX().expandX().height(25);
		mainTable.add(valueTitle).center().fillX().expandX().height(25);
		mainTable.row();
		
		Iterator<Entry<String, String>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());

			CheckBox removeBox = new CheckBox("", uiSkin);
			
	        String key = pairs.getKey();
	        String value = pairs.getValue() != null && pairs.getValue().length() > 0 ? pairs.getValue() : "(none)";
			
			Label keyLabel = new Label(key, uiSkin, "property");
			keyLabel.setWrap(true);
			Label valueLabel = new Label(value, uiSkin, "property");
			valueLabel.setWrap(true);
			
			mainTable.add(removeBox).left().expandX().fillX();
			mainTable.add(keyLabel).top().left().expandX().fillX();
			mainTable.add(valueLabel).top().left().expandX().fillX();
			mainTable.row();

			removedProperties.put(pairs.getKey(), removeBox);

		}
		mainTable.add().fill().expand();
		
		return mainTable;
	}
	
	public String getKey() {
		if (keyField == null) return null;
		return keyField.getText();
	}

	public String getValue() {
		if (keyField == null) return null;
		return valueField.getText();
	}

	public HashMap<String, CheckBox> getRemovedProperties() {
		return removedProperties;
	}

	public boolean isAddMode() {
		return isAddMode;
	}


}
