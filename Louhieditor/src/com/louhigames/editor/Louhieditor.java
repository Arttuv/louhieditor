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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.louhigames.editor.objects.MapCellObject;
import com.louhigames.editor.objects.MapObject;
import com.louhigames.editor.objects.MenuPropertyObject;
import com.louhigames.editor.ui.objects.MapCellButton;
import com.louhigames.editor.ui.objects.MapCreationDialog;
import com.louhigames.editor.util.MenuPropertyReader;

public class Louhieditor implements ApplicationListener, ButtonListener {
	
	public static final String SKIN_LIBGDX_UI = "skin/uiskin.json";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "skin/uiskin.atlas";
	
	public static final String SKIN_TOOLBAR_UI = "skin/toolbar-skin.json";
	public static final String TEXTURE_ATLAS_TOOLBAR_UI = "skin/toolbar-icons.atlas";
	
	private final boolean debug = false;
	
	private Stage stage;
	private Skin uiSkin;
	private Skin toolbarSkin;
	
	private Table mapTable;
	private MapCreationDialog newMapDialog;
	private Button eraseButton;
	
	private Tree menuTree;
	private ArrayList<MenuPropertyObject> menuPropertyObjects;
	
	private MapObject mapObject;
	
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
		//if (debug) mainTable.debug();
		
	    mainTable.setFillParent(true);

	    initSkins();
	    
	    Actor toolbar = buildToolbar();
	    Actor mapArea = buildMapArea();
	    Actor optionsArea = buildOptionsArea();
	    
	    mainTable.add(toolbar).fill().colspan(2).height(50).expandX().left();
	    mainTable.row();
	    mainTable.add(mapArea).expand().pad(1).left().top().fill();
	    mainTable.add(optionsArea).width(150).pad(1).top().fillY();
	    
	    stage.addActor(mainTable);

		
	}
	
	private void initSkins() {
		this.uiSkin = new Skin(Gdx.files.internal(SKIN_LIBGDX_UI), new TextureAtlas(TEXTURE_ATLAS_LIBGDX_UI));
		this.toolbarSkin = new Skin(Gdx.files.internal(SKIN_TOOLBAR_UI), new TextureAtlas(TEXTURE_ATLAS_TOOLBAR_UI));
	}
	
	private Actor buildMapArea() {
		
		mapTable = new Table();
	 
		ScrollPane scrollPanel = new ScrollPane(mapTable, uiSkin);
		scrollPanel.setFadeScrollBars(false);
		
		
		return scrollPanel;
	    
	}
	
	private void buildMapCellTable(int w, int h, int mapId, String mapName) {
	    
		Table table = mapTable;
		mapTable.clearChildren();
		
	    ButtonStyle buttonStyle = uiSkin.get("map-cell", ButtonStyle.class);
	    
	    this.mapObject = new MapObject(mapId, mapName); 

	    for (int ix = 0; ix < w; ix++) {
	    	
	    	for (int iy = 0; iy < h; iy++) {

	    		MapCellObject cellObject = new MapCellObject(ix, iy);
	    		MapCellButton button = new MapCellButton(buttonStyle, cellObject);
	    		this.mapObject.addGameObject(cellObject);
	    		
	    		button.addListener(new ChangeListener() {
	    				public void changed (ChangeEvent event, Actor actor) {
	    					mapCellClicked(event, actor);
	    				}
	    		});
	    		table.add(button).width(25f).height(25f);

	    		
	    	}
	    	
	    	table.row();
	    }
	}
	
	private Actor buildToolbar() {
	    Table toolbar = new Table();
	    //if (debug) toolbar.debug();
	    
	    toolbar.setSkin(uiSkin);
	    toolbar.setBackground("default-rect");
	    
	    Button newMapButton = new Button(toolbarSkin, "new-map");
	    newMapButton.setName("new-map");
	    newMapButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				buttonClicked(event, actor);
			}
	    });
	    
	    eraseButton = new Button(toolbarSkin, "erase-cell");
	    eraseButton.setName("erase-cell");
	    eraseButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				buttonClicked(event, actor);
			}
	    });
	    
	    toolbar.add(newMapButton).width(25).height(25).pad(2).left();
	    toolbar.add(eraseButton).width(25).height(25).pad(2).left().expand();
	    
	    return toolbar;
	}

	private Actor buildOptionsArea() {

		Table areaTable = new Table(uiSkin);
		//areaTable.debug();
		
		refreshMenuPropertyObjects();
		menuTree = buildTree(menuPropertyObjects);
		
		//Table propertyTable = new Table(uiSkin);
		//if (debug) propertyTable.debug();
		
		//Label title = new Label("Properties", uiSkin);
		
		//propertyTable.add(title);
		
		areaTable.add(menuTree).left().top().expand().fill();
		//areaTable.row();
		//areaTable.add(title);

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

	public void buttonClicked(ChangeEvent event, Actor actor) {
		
		System.out.println("Button clicked! " + actor.getName());
		
		if (actor.getName() == "new-map") {
			if (newMapDialog == null) newMapDialog = new MapCreationDialog("New map...", uiSkin, 300, 300, this);
			newMapDialog.show(stage);
		}
		else if (actor.getName() == "erase-cell") {
			
			Button b = (Button) actor;
			b.isChecked();
			
		}
		else if (actor.getName() == "Dialog OK") {
			String mapIdStr = newMapDialog.getMapIdField().getText();
			String mapName = newMapDialog.getMapNameField().getText();
			String wStr = newMapDialog.getWidthField().getText();
			String hStr = newMapDialog.getHeightField().getText();
			
			if (mapIdStr.length() > 0 && mapName.length() > 0 && wStr.length() > 0 && hStr.length() > 0) {
				int mapId = Integer.parseInt(mapIdStr);
				int width = Integer.parseInt(wStr);
				int height = Integer.parseInt(hStr);
				
				buildMapCellTable(width, height, mapId, mapName);
			}

		}
		else if (actor.getName() == "Dialog Cancel") {
			
		}
		
	}	
	
	private void mapCellClicked(ChangeEvent event, Actor actor) {
		
		Array<Node> selectedNodes = menuTree.getSelection();
		if (selectedNodes != null && selectedNodes.size > 0) {
			
			Node selectedNode = selectedNodes.first();
			MenuPropertyObject o = (MenuPropertyObject) selectedNode.getObject();
			
			if (selectedNode.getIcon() != null) {
				
				MapCellButton b = (MapCellButton) actor;
				MapCellObject cellObject =  b.getMapCellObject();
				String cellType = null;
				b.clearChildren();
				
				if (eraseButton.isChecked()) {
					
				} else {
					
					b.add(new Image(selectedNode.getIcon()));
					cellType = o.getName();
					
				}
				
				cellObject.setCellType(cellType);
				
			}
		}
		
	}
	
	
	@Override
	public void dispose() {
		stage.dispose();
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
