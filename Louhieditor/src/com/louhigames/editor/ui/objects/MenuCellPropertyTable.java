package com.louhigames.editor.ui.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.louhigames.editor.ButtonClickListener;
import com.louhigames.editor.CallBack;
import com.louhigames.editor.objects.MenuPropertyObject;

public class MenuCellPropertyTable extends Table implements CallBack {

	private MenuPropertyObject menuPropertyObject;
	private Skin skin;
	private Skin buttonSkin;
	private MenuCellPropertyDialog dialog;
	private CallBack callBack;

	public MenuCellPropertyTable(MenuPropertyObject menuPropertyObject, Skin skin, Skin buttonSkin, CallBack callBack) {
		super(skin);
		this.menuPropertyObject = menuPropertyObject;
		this.skin = skin;
		this.buttonSkin = buttonSkin;
		this.callBack = callBack;
		// this.debug();
		buildUI();
	}

	private void buildUI() {

		System.out.println("Build UI (menucellpropertytable)");

		this.clearChildren();
		this.defaults().space(1).pad(0);
		this.setBackground("property-table-background");

		Button addPropertyButton = new Button(buttonSkin, "add-property");
		addPropertyButton.setName("add-property");
		ButtonClickListener.createListener(this, addPropertyButton);

		Button delPropertyButton = new Button(buttonSkin, "del-property");
		delPropertyButton.setName("del-property");
		ButtonClickListener.createListener(this, delPropertyButton);

		Label keyTitle = new Label("Key", skin, "property-header");
		keyTitle.setAlignment(Align.center, Align.center);
		Label valueTitle = new Label("Default Value", skin, "property-header");
		valueTitle.setAlignment(Align.center, Align.center);

		Table buttonTable = new Table(skin);
		buttonTable.add(addPropertyButton).left().padBottom(5).padTop(5).padRight(2);
		buttonTable.add(delPropertyButton).left().padBottom(5).padTop(5);

		this.add(buttonTable).colspan(2).left();
		this.row();
		this.add(keyTitle).top().center().fillX().expandX().height(25);
		this.add(valueTitle).top().center().fillX().expandX().height(25);
		this.row();

		if (menuPropertyObject != null && menuPropertyObject.getProperties() != null) {
			HashMap<String, String> properties = menuPropertyObject.getProperties();

			Iterator<Entry<String, String>> it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());

				String key = pairs.getKey();
				String value = pairs.getValue() != null && pairs.getValue().length() > 0 ? pairs.getValue() : "(none)";

				Label keyLabel = new Label(key, skin, "property");
				keyLabel.setWrap(true);
				Label valueLabel = new Label(value, skin, "property");
				valueLabel.setWrap(true);

				this.add(keyLabel).top().left().expand().fillX();
				this.add(valueLabel).top().left().expand().fillX();
				this.row();

			}
			this.add().fill().expand();
		}

	}

	public boolean addProperty(String key, String value) {

		HashMap<String, String> properties = menuPropertyObject.getProperties();
		if (properties != null && !properties.containsKey(key) && key != null) {
			properties.put(key, value);
			buildUI();
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteProperties(HashMap<String, CheckBox> removedProperties) {
		
		HashMap<String, String> properties = menuPropertyObject.getProperties();
		Iterator<Entry<String, CheckBox>> it = removedProperties.entrySet().iterator();
		boolean removed = false;
		while (it.hasNext()) {
			Map.Entry<String, CheckBox> pairs = (Map.Entry<String, CheckBox>) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());

			String key = pairs.getKey();
			boolean remove = pairs.getValue().isChecked();

			if (remove) {

				if (properties.containsKey(key)) {
					properties.remove(key);
					removed = true;
				}

			}

		}

		if (removed) {
			buildUI();
		}
		
		return removed;
	}
	
	@Override
	public void buttonClicked(ChangeEvent event, Actor actor) {
		System.out.println("!!!!!");
		if (actor.getName() == "add-property") {
			dialog = new MenuCellPropertyDialog("Add property...", skin, menuPropertyObject, this, true);
			dialog.show(this.getStage());
		} 
		else if (actor.getName() == "del-property") {
			dialog = new MenuCellPropertyDialog("  Delete properties...  ", skin, menuPropertyObject, this, false);
			dialog.show(this.getStage());
		} 
		else if (actor.getName() == "MenuCellPropertyDialog Add OK") {

			if (dialog != null) {

				String key = dialog.getKey();
				String value = dialog.getValue();
				boolean ok = addProperty(key, value);

				if (ok) {
					callBack.buttonClicked(event, actor);
				}
			}
		} 
		else if (actor.getName() == "MenuCellPropertyDialog Del OK") {

			if (dialog != null) {
				boolean removed = deleteProperties(dialog.getRemovedProperties());
				
				if (removed) {
					callBack.buttonClicked(event, actor);
				}
			}
			
		}
		
	}

}
