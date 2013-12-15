package com.louhigames.editor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Louhieditor implements ApplicationListener {
	
	public static final String SKIN_LIBGDX_UI = "skin/uiskin.json";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "skin/uiskin.atlas";
	
	private final boolean debug = true;
	
	private Stage stage;
	private Skin skin;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		System.out.println(w + " " + h);

		this.stage = new Stage( w, h, true );
		this.stage.setViewport(w, h);
		Gdx.input.setInputProcessor(stage);
		
		buildUI();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	    Table.drawDebug(stage);
	}

	private void buildUI() {
		
		System.out.println("BUILD UI!");

		Table mainTable = new Table();
		//mainTable.setSize(800f, 600f);
		mainTable.debug();
	    mainTable.setFillParent(true);
	    
		//_____________________________
		//	MAP CELL AREA     | OPTIONS
		//					  |
		//					  |
		//					  |
		//____________________|________
		
	    skin = getSkin();
	    
	    Table optionsAreaTable = buildOptionsArea();
	    
	    mainTable.left().top();
	    mainTable.add(buildMapArea()).expand().pad(10).left().top();
	    mainTable.add(optionsAreaTable).width(200).pad(10).left().top();
	    
	    stage.addActor(mainTable);

		
	}
	
	private Table testTable() {
	    Label nameLabel = new Label("Name:", skin);
	    TextField nameText = new TextField("", skin);
	    Label addressLabel = new Label("Address:", skin);
	    TextField addressText = new TextField("", skin);

	    Table table = new Table();
	    table.add(nameLabel);
	    table.add(nameText).width(100);
	    table.row();
	    table.add(addressLabel);
	    table.add(addressText).width(100);
		
	    return table;
	}
	
	private ScrollPane buildMapArea() {
		
		Table table = new Table();
	    ButtonStyle buttonStyle = skin.get("default", ButtonStyle.class);
	    
	    int x = 100;
	    int y = 100;
	    for (int ix = 0; ix < x; ix++) {
	    	
	    	for (int iy = 0; iy < y; iy++) {
	    		
	    		Button button = new Button(buttonStyle);
	    		button.addListener(new ChangeListener() {
	    				public void changed (ChangeEvent event, Actor actor) {
	    					Button b = (Button) actor;
	    					ButtonStyle buttonStyle = skin.get("mapCell", ButtonStyle.class);
	    					b.setStyle(buttonStyle);
	    					System.out.println("Changed!");
	    				}
	    		});
	    		table.add(button).width(25f).height(25f);
	    		
	    	}
	    	
	    	table.row();
	    }
	 

		ScrollPane scrollPanel = new ScrollPane(table, skin);
	    
	    //if (debug) table.debug(); // turn on all debug lines (table, cell, and widget)
		
	    return scrollPanel;
	    
	}
	
	private Table buildOptionsArea() {
		Table table = new Table();
		
	    Label l = new Label("OPTIONS AREA", skin);
	    table.add(l);
	    
		//if (debug) table.debug(); // turn on all debug lines (table, cell, and widget)
		
		return table;
	}
	
	
	private Skin getSkin() {
		Skin skin = new Skin(Gdx.files.internal(SKIN_LIBGDX_UI), new TextureAtlas(TEXTURE_ATLAS_LIBGDX_UI));
		return skin;
	}
	
	@Override
	public void resize(int width, int height) {
		//buildUI();
		this.stage.setViewport(width, height);
		
		buildUI();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
