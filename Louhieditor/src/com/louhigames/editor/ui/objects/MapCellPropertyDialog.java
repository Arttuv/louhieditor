package com.louhigames.editor.ui.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.louhigames.editor.CallBack;
import com.louhigames.editor.objects.MapCellObject;
import com.louhigames.editor.objects.MenuPropertyObject;

public class MapCellPropertyDialog extends Dialog {

	private MapCellObject mapCellObject;
	private CallBack callBack;
	private Skin uiSkin;
	private HashMap<String, TextField> editableProperties;

	public MapCellPropertyDialog(String title, Skin skin, MapCellObject mapCellObject, CallBack listener) {
		super(title, skin);
		this.uiSkin = skin;
		this.callBack = listener;
		this.mapCellObject = mapCellObject;
		buildUI();
	}

	private void buildUI() {

		Table mainTable = new Table(uiSkin);

		TextButton okButton = new TextButton("OK", uiSkin);
		okButton.setName("MapCellPropertyDialog OK");
		okButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				updateProperties();
				//callBack.buttonClicked(event, actor);
			}
		});

		TextButton cancelButton = new TextButton("Cancel", uiSkin);
		cancelButton.setName("MapCellPropertyDialog Cancel");
		cancelButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				//callBack.buttonClicked(event, actor);
			}
		});

		mainTable.clearChildren();
		mainTable.defaults().space(1).pad(0);
		mainTable.setBackground("property-table-background");

		Label keyTitle = new Label("Key", uiSkin, "property-header");
		keyTitle.setAlignment(Align.center, Align.center);
		Label valueTitle = new Label("Value", uiSkin, "property-header");
		valueTitle.setAlignment(Align.center, Align.center);

		mainTable.add(keyTitle).top().center().fillX().expandX();
		mainTable.add(valueTitle).top().center().fillX().expandX();
		mainTable.row();

		editableProperties = new HashMap<String, TextField>();

		// MenuPropertyObject menuPropertyObject =
		// mapCellObject.getMenuPropertyObject();

		HashMap<String, String> properties = mapCellObject.getProperties();

		Iterator it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());

			Label keyLabel = new Label(pairs.getKey(), uiSkin, "property");
			TextField valueField = new TextField(pairs.getValue(), uiSkin);

			mainTable.add(keyLabel).top().left().expandX().fillX().height(25);
			mainTable.add(valueField).top().left().expandX().fillX().height(25);
			mainTable.row();

			editableProperties.put(pairs.getKey(), valueField);

		}
		mainTable.add().fill().expand();

		getContentTable().add(mainTable).fill().expand().padBottom(20);
		button(okButton);
		button(cancelButton);

	}

	private void updateProperties() {

		HashMap<String, String> properties = mapCellObject.getProperties();
		
		Iterator it = editableProperties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, TextField> pairs = (Map.Entry<String, TextField>) it.next();

			String key = pairs.getKey();
			TextField valueField = pairs.getValue();
			
			properties.remove(key);
			properties.put(key, valueField.getText());
			
		}
	}

}
