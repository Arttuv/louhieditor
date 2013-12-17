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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.louhigames.editor.objects.MenuPropertyObject;
import com.louhigames.editor.util.MenuPropertyReader;

public class Louhieditor implements ApplicationListener {
	
	public static final String SKIN_LIBGDX_UI = "skin/uiskin.json";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "skin/uiskin.atlas";
	
	private final boolean debug = false;
	
	private Stage stage;
	private Skin uiSkin;
	
	private Tree menuTree;
	private ArrayList<MenuPropertyObject> menuPropertyObjects;
	
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
		if (debug) mainTable.debug();
		
	    mainTable.setFillParent(true);

	    initSkins();
	    
	    Actor mapArea = buildMapArea();
	    Actor optionsArea = buildOptionsArea();
	    
	    mainTable.add(mapArea).expand().pad(10).left().top();
	    mainTable.add(optionsArea).width(150).top();
	    
	    stage.addActor(mainTable);

		
	}
	
	private Actor buildMapArea() {
		
		Table table = new Table();
	    ButtonStyle buttonStyle = uiSkin.get("map-cell", ButtonStyle.class);
	    
	    int x = 25;
	    int y = 25;
	    for (int ix = 0; ix < x; ix++) {
	    	
	    	for (int iy = 0; iy < y; iy++) {
	    		
	    		Button button = new Button(buttonStyle);
	    		
	    		button.addListener(new ChangeListener() {
	    				public void changed (ChangeEvent event, Actor actor) {
	    	
	    					Array<Node> selectedNodes = menuTree.getSelection();
	    					if (selectedNodes != null && selectedNodes.size > 0) {
		    					
	    						Node selectedNode = selectedNodes.first();
	    						MenuPropertyObject o = (MenuPropertyObject) selectedNode.getObject();
	    						
	    						if (o != null && o.getIconAtlasPath() != null && o.getIconName() != null) {
			    					TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(o.getIconAtlasPath()));
			    					AtlasRegion region = atlas.findRegion(o.getIconName());
			    					TextureRegionDrawable drawable = new TextureRegionDrawable(region);
			    					
			    					Button b = (Button) actor;
			    					b.clearChildren();
			    					
			    					b.add(new Image(drawable));
			    					//b.setBackground(drawable);
			    					
			    					System.out.println("Set background!");
	    						}

		    					
	    					}

	    					//ButtonStyle buttonStyle = louhiEditorSkin.get("mapCell", ButtonStyle.class);
	    					//b.setStyle(buttonStyle);
	    					System.out.println("Changed!");
	    				}
	    		});
	    		table.add(button).width(25f).height(25f);
	    		
	    	}
	    	
	    	table.row();
	    }
	 

		ScrollPane scrollPanel = new ScrollPane(table, uiSkin);
		scrollPanel.setFadeScrollBars(false);
		
		return scrollPanel;
	    
	}
	
	private Actor buildOptionsArea() {

		Table areaTable = new Table(uiSkin);
		areaTable.debug();
		
		refreshMenuPropertyObjects();
		menuTree = buildTree(menuPropertyObjects);
		
		//Table propertyTable = new Table(uiSkin);
		//if (debug) propertyTable.debug();
		
		Label title = new Label("Properties", uiSkin);
		
		//propertyTable.add(title);
		
		areaTable.add(menuTree).left().top();
		areaTable.row();
		areaTable.add(title);

		ScrollPane scrollPanel = new ScrollPane(areaTable, uiSkin);
		scrollPanel.setFadeScrollBars(false);
		
		return scrollPanel;
	}
	
	private Tree buildTree(ArrayList<MenuPropertyObject> objects) {
		Tree tree = new Tree(uiSkin);
		
		for (MenuPropertyObject o : objects) {
			Label l = new Label(o.getName(), uiSkin);
			Node n = new Node(l);
			n.setObject(o);
			
			if (o.getIconAtlasPath() != null && o.getIconName() != null) {
				TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(o.getIconAtlasPath()));
				AtlasRegion region = atlas.findRegion(o.getIconName());
				TextureRegionDrawable drawable = new TextureRegionDrawable(region);
				n.setIcon(drawable);
			}
			
			buildTree(o, n);
			
			tree.add(n);
		}
	    
		return tree;
	}
	
	private void buildTree(MenuPropertyObject parentObject, Node parentNode) {
		
		if (parentObject == null) return;
		
		for (MenuPropertyObject o : parentObject.getChildrenObjects()) {
			Label l = new Label(o.getName(), uiSkin);
			
			Node n = new Node(l);
			n.setObject(o);
			
			if (o.getIconAtlasPath() != null && o.getIconName() != null) {
				TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(o.getIconAtlasPath()));
				AtlasRegion region = atlas.findRegion(o.getIconName());
				TextureRegionDrawable drawable = new TextureRegionDrawable(region);
				n.setIcon(drawable);
			}
			
			buildTree(o, n);
			
			parentNode.add(n);
		}
		
	}
	
	private void refreshMenuPropertyObjects() {
		
		MenuPropertyReader r = new MenuPropertyReader();
		try {
			menuPropertyObjects = r.read("data/menu.property");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initSkins() {
		this.uiSkin = new Skin(Gdx.files.internal(SKIN_LIBGDX_UI), new TextureAtlas(TEXTURE_ATLAS_LIBGDX_UI));
	}
	
	@Override
	public void resize(int width, int height) {
		this.stage.setViewport(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	
}
