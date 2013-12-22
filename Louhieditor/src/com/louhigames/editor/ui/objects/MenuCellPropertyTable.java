package com.louhigames.editor.ui.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
		//this.debug();
		buildUI();
	}
	
	private void buildUI() {
		
		System.out.println("Build UI (menucellpropertytable)");
		
		this.clearChildren();
		this.defaults().space(1).pad(0);
		this.setBackground("property-table-background");
		
		Button addPropertyButton = new Button(buttonSkin, "add-property");
		addPropertyButton.setName("add-property");
		ButtonClickListener.createButtonClickListener(this, addPropertyButton);
		
		Label keyTitle = new Label("Key", skin, "property-header");
		keyTitle.setAlignment(Align.center, Align.center);
		Label valueTitle = new Label("Default Value", skin, "property-header");
		valueTitle.setAlignment(Align.center, Align.center);
		
		this.add(addPropertyButton).left().padBottom(5);
		this.row();
		this.add(keyTitle).top().center().fillX().expandX();
        this.add(valueTitle).top().center().fillX().expandX();
        this.row();
		
		if (menuPropertyObject != null && menuPropertyObject.getProperties() != null) {
			HashMap<String, String> properties = menuPropertyObject.getProperties();
			
			Iterator it = properties.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
		        
		        Label keyLabel = new Label(pairs.getKey(), skin, "property");
		        Label valueLabel = new Label(pairs.getValue(), skin, "property");
		        
		        this.add(keyLabel).top().left().expandX().fillX();
		        this.add(valueLabel).top().left().expandX().fillX();
		        this.row();
		        
		    }
		    this.add().fill().expand();
		}
		
	}
	
	public boolean addProperty(String key, String value) {
		
		HashMap<String, String> properties = menuPropertyObject.getProperties();
		if (properties != null && ! properties.containsKey(key) && key != null) {
			properties.put(key, value);
			buildUI();
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void buttonClicked(ChangeEvent event, Actor actor) {
		System.out.println("!!!!!");
		if (actor.getName() == "add-property") {
			dialog = new MenuCellPropertyDialog("Add property...", skin, this);
			dialog.show(this.getStage());
		}
		else if (actor.getName() == "MenuCellPropertyDialog OK") {
			
			if (dialog != null) {
				String key = dialog.getKeyField().getText();
				String value = dialog.getValueField().getText();
				boolean ok = addProperty(key, value);
				if (ok) {
					callBack.buttonClicked(new ChangeEvent(), actor);
				}
			}
		}
	}
	
}
