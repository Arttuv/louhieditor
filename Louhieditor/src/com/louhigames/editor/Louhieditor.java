package com.louhigames.editor;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.louhigames.editor.objects.MapPropertyObject;

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
	    
        if (debug) Table.drawDebug(stage);
	}

	private void buildUI() {
		
		System.out.println("BUILD UI!");

		Table mainTable = new Table();
		//mainTable.setSize(800f, 600f);
		if (debug) mainTable.debug();
		
	    mainTable.setFillParent(true);
	    
		//_____________________________
		//	MAP CELL AREA     | OPTIONS
		//					  |
		//					  |
		//					  |
		//____________________|________
		
	    skin = getSkin();
	    
	    Actor mapArea = buildMapArea();
	    Actor optionsArea = buildOptionsArea();
	    
	    mainTable.add(mapArea).expand().pad(10).left().top();
	    mainTable.add(optionsArea).width(200).pad(10).left().top();
	    
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
	
	private Actor buildMapArea() {
		
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
		scrollPanel.setFadeScrollBars(false);
		
		return scrollPanel;
	    
	}
	
	private Actor buildOptionsArea() {

		Table areaTable = new Table(skin);
		
		Tree tree = buildTree(getMapPropertyObjects());

		Table propertyTable = new Table(skin);
		//if (debug) propertyTable.debug();
		
		Label title = new Label("Properties", skin);
		
		propertyTable.add(title);
		
		areaTable.add(tree).expand().left().top();
		areaTable.row();
		areaTable.add(propertyTable).expandX().left();
		
		
		
		return areaTable;
	}
	
	private Tree buildTree(ArrayList<MapPropertyObject> objects) {
		Tree tree = new Tree(skin);
		   
		//check-off
		//TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
		//AtlasRegion region = atlas.findRegion("check-on");
		//TextureRegionDrawable drawable = new TextureRegionDrawable(region);
		
		
		for (MapPropertyObject o : objects) {
			TextButton b = new TextButton(o.getDisplayValue(), skin);
			b.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Changed!");
				}
			});

			Node n = new Node(b);
			//n.setIcon(drawable);
			buildTree(o, n);
			
			tree.add(n);
		}
	    
		return tree;
	}
	
	private void buildTree(MapPropertyObject parentObject, Node parentNode) {
		
		if (parentObject == null) return;
		
		for (MapPropertyObject o : parentObject.getChildrenObjects()) {
			TextButton b = new TextButton(o.getDisplayValue(), skin);
			b.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Changed!");
				}
			});
			
			Node n = new Node(b);
			buildTree(o, n);
			
			parentNode.add(n);
		}
		
	}
	
	// TODO: Read from file
	private ArrayList<MapPropertyObject> getMapPropertyObjects() {
		
		ArrayList<MapPropertyObject> objects = new ArrayList<MapPropertyObject>();
		
		MapPropertyObject o01 = new MapPropertyObject("Map properties", "Map properties");
		MapPropertyObject o02 = new MapPropertyObject("Start", "Start");
		MapPropertyObject o03 = new MapPropertyObject("Goal", "Goal");
		o01.addChildren(o02);
		o01.addChildren(o03);
		
		MapPropertyObject o11 = new MapPropertyObject("Blocks", "Blocks");
		MapPropertyObject o12 = new MapPropertyObject("Rock", "Rock");
		MapPropertyObject o13 = new MapPropertyObject("Grass", "Grass");
		o11.addChildren(o12);
		o11.addChildren(o13);
		
		MapPropertyObject o21 = new MapPropertyObject("Game objects", "Game objects");
		MapPropertyObject o22 = new MapPropertyObject("Enemy", "Enemy");
		MapPropertyObject o23 = new MapPropertyObject("Door", "Door");
		MapPropertyObject o24 = new MapPropertyObject("Key", "Key");
		MapPropertyObject o25 = new MapPropertyObject("Item", "Item");
		MapPropertyObject o26 = new MapPropertyObject("Teleport", "Teleport");
		o21.addChildren(o22);
		o21.addChildren(o23);
		o21.addChildren(o24);
		o21.addChildren(o25);
		o21.addChildren(o26);
		
		objects.add(o01);
		objects.add(o11);
		objects.add(o21);
		
		return objects;
		
	}
	
	private Skin getSkin() {
		Skin skin = new Skin(Gdx.files.internal(SKIN_LIBGDX_UI), new TextureAtlas(TEXTURE_ATLAS_LIBGDX_UI));
		return skin;
	}
	
	@Override
	public void resize(int width, int height) {
		this.stage.setViewport(width, height);
		
		//buildUI();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	
}
