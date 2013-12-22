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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
import com.louhigames.editor.ui.objects.MapCellPropertyDialog;
import com.louhigames.editor.ui.objects.MapCreationDialog;
import com.louhigames.editor.ui.objects.MenuCellPropertyTable;
import com.louhigames.editor.ui.objects.OpenMapDialog;
import com.louhigames.editor.util.MenuPropertyReader;

public class Louhieditor implements ApplicationListener, CallBack {
	
	public static final String SKIN_LIBGDX_UI = "skin/uiskin.json";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "skin/uiskin.atlas";
	
	public static final String SKIN_TOOLBAR_UI = "skin/toolbar-skin.json";
	public static final String TEXTURE_ATLAS_TOOLBAR_UI = "skin/toolbar-icons.atlas";
	
	private final boolean debug = true;
	
	private Stage stage;
	private Skin uiSkin;
	private Skin toolbarSkin;
	
	private Table mapTable;
	private Table propertyTable;
	
	private OpenMapDialog openMapDialog;
	private MapCreationDialog newMapDialog;
	
	private Button setCellsButton;
	private Button editCellsButton;
	private Button eraseCellsButton;
	
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
	    
	    mainTable.add(toolbar).fill().colspan(2).height(55).expandX().left();
	    mainTable.row();
	    mainTable.add(mapArea).expand().pad(1).left().top().fill();
	    mainTable.add(optionsArea).width(200).pad(1).top().fillY();
	    
	    stage.addActor(mainTable);

		
	}
	
	private void initSkins() {
		this.uiSkin = new Skin(Gdx.files.internal(SKIN_LIBGDX_UI), new TextureAtlas(TEXTURE_ATLAS_LIBGDX_UI));
		this.toolbarSkin = new Skin(Gdx.files.internal(SKIN_TOOLBAR_UI), new TextureAtlas(TEXTURE_ATLAS_TOOLBAR_UI));
	}
	
	private Actor buildMapArea() {
		
		mapTable = new Table();
		
		Label l = new Label("No map yet...?", uiSkin);
		Label l2 = new Label("Create a new one ", uiSkin);
		Label l3 = new Label("or", uiSkin);
		Label l4 = new Label("open existing ", uiSkin);
		
		Button newMapButton = new Button(toolbarSkin, "new-map");
	    newMapButton.setName("new-map");
	    ButtonClickListener.createButtonClickListener(this, newMapButton);
	    
	    Button openButton = new Button(toolbarSkin, "open");
	    openButton.setName("open");
	    ButtonClickListener.createButtonClickListener(this, openButton);
	    
	    mapTable.add(l);
	    mapTable.row();
	    mapTable.add(l2);
	    mapTable.add(newMapButton).width(25).height(25);
	    mapTable.row();
	    mapTable.add(l3);
	    mapTable.row();
	    mapTable.add(l4);
	    mapTable.add(openButton).width(25).height(25);
		
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
	    ButtonClickListener.createButtonClickListener(this, newMapButton);
	    
	    Button openButton = new Button(toolbarSkin, "open");
	    openButton.setName("open");
	    ButtonClickListener.createButtonClickListener(this, openButton);
	    
	    Button saveButton = new Button(toolbarSkin, "save");
	    saveButton.setName("save");
	    ButtonClickListener.createButtonClickListener(this, saveButton);
	    
	    Button deleteMapButton = new Button(toolbarSkin, "delete-map");
	    deleteMapButton.setName("delete-map");
	    ButtonClickListener.createButtonClickListener(this, deleteMapButton);

	    Button exportMapButton = new Button(toolbarSkin, "export-map");
	    exportMapButton.setName("export-map");
	    ButtonClickListener.createButtonClickListener(this, exportMapButton);
	    
	    setCellsButton = new Button(toolbarSkin, "set-cells");
	    setCellsButton.setChecked(true);
	    setCellsButton.setName("set-cells");
	    ButtonClickListener.createButtonClickListener(this, setCellsButton);
	    
	    editCellsButton = new Button(toolbarSkin, "edit-cells");
	    editCellsButton.setChecked(false);
	    editCellsButton.setName("edit-cells");
	    ButtonClickListener.createButtonClickListener(this, editCellsButton);

	    eraseCellsButton = new Button(toolbarSkin, "erase-cells");
	    eraseCellsButton.setChecked(false);
	    eraseCellsButton.setName("erase-cells");	    
	    ButtonClickListener.createButtonClickListener(this, eraseCellsButton);

	    
	    // to the last button cell, do expand()
	    
	    toolbar.add(new Label("Map", uiSkin)).colspan(6);
	    toolbar.add(new Label("Cells", uiSkin)).colspan(3);
	    toolbar.row();
	    toolbar.add(newMapButton).width(25).height(25).pad(2).padLeft(5).left();
	    toolbar.add(openButton).width(25).height(25).pad(2).left();
	    toolbar.add(saveButton).width(25).height(25).pad(2).left();
	    toolbar.add(exportMapButton).width(25).height(25).pad(2).left();
	    toolbar.add(deleteMapButton).width(25).height(25).pad(2).left();
	    toolbar.add().padLeft(10);
	    toolbar.add(setCellsButton).width(25).height(25).pad(2).left();
	    toolbar.add(editCellsButton).width(25).height(25).pad(2).left();
	    toolbar.add(eraseCellsButton).width(25).height(25).pad(2).left();
	    toolbar.add().expand();
	    
	    return toolbar;
	}

	private Actor buildOptionsArea() {

		Table areaTable = new Table(uiSkin);
		//areaTable.debug();
		
		refreshMenuPropertyObjects();
		menuTree = buildTree(menuPropertyObjects);
		menuTree.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				menuTreeClicked(event, actor);
			}
		});
		
		propertyTable = new Table(uiSkin);
		//if (debug) propertyTable.debug();

		
		Label title = new Label("Cell properties", uiSkin);
		
		areaTable.add(menuTree).left().top().fillX();
		areaTable.row();
		areaTable.add(title).top().padTop(10);
		areaTable.row();
		areaTable.add(propertyTable).top().fill();
		areaTable.row();
		areaTable.add().expand().fill();
		
		ScrollPane scrollPanel = new ScrollPane(areaTable, uiSkin);
		
		scrollPanel.setFadeScrollBars(false);
		
		return scrollPanel;
	}
	
	private Tree buildTree(ArrayList<MenuPropertyObject> objects) {
		Tree tree = new Tree(uiSkin);
		tree.setMultiSelect(false);
		
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
		else if (actor.getName() == "erase-cells") {
			
			setCellsButton.setChecked(false);
			editCellsButton.setChecked(false);
			
		}
		else if (actor.getName() == "set-cells") {
			
			eraseCellsButton.setChecked(false);
			editCellsButton.setChecked(false);
			
		}
		else if (actor.getName() == "edit-cells") {
	
			setCellsButton.setChecked(false);
			eraseCellsButton.setChecked(false);
	
		}
		else if (actor.getName() == "NewMapDialog OK") {
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
		else if (actor.getName() == "open") {
			if (openMapDialog == null) openMapDialog = new OpenMapDialog("Open map...", uiSkin, 300, 300, this);
			openMapDialog.show(stage);
		}
		else if (actor.getName() == "OpenMapDialog OK") {
			// TODO: load map
		}
		else if (actor.getName() == "MenuCellPropertyDialog OK") {
			Array<Node> selectedNodes = menuTree.getSelection();
			if (selectedNodes != null && selectedNodes.size > 0) {
				
				Node selectedNode = selectedNodes.first();
				MenuPropertyObject menuPropertyObject = (MenuPropertyObject) selectedNode.getObject();
				
				ArrayList<MapCellObject> gameObjects = mapObject.getGameObjects();
				for (MapCellObject object : gameObjects) {
					
					if (menuPropertyObject.equals(object.getMenuPropertyObject())) {
						object.refreshProperties(false);
					}
					
				}
				
			}
		}
		/*else {
			Dialog wutDialog = new Dialog("Wut?", uiSkin);
			wutDialog.getContentTable().add(new Label("Wut :O", uiSkin)).width(200).height(50).center().expand();
			wutDialog.button(new TextButton("Never mind...", uiSkin));
			
			wutDialog.show(stage);
			
		}*/
		
	}
	
	private void menuTreeClicked(ChangeEvent event, Actor actor) {
		Array<Node> selectedNodes = menuTree.getSelection();
		Node selectedNode = selectedNodes.first();
		MenuPropertyObject o = (MenuPropertyObject) selectedNode.getObject();
		System.out.println(o.getName());
		
		MenuCellPropertyTable mcpt = new MenuCellPropertyTable(o, uiSkin, toolbarSkin, this);
		//mcpt.addProperty("Testi", "testiarvo");
		
		propertyTable.clearChildren();
		propertyTable.add(mcpt).top().left().expand().fill();
	}
	
	private void mapCellClicked(ChangeEvent event, Actor actor) {
		
		Array<Node> selectedNodes = menuTree.getSelection();
		if (selectedNodes != null && selectedNodes.size > 0) {
			
			Node selectedNode = selectedNodes.first();
			MenuPropertyObject o = (MenuPropertyObject) selectedNode.getObject();
			
			if (selectedNode.getIcon() != null) {
				
				MapCellButton b = (MapCellButton) actor;
				MapCellObject cellObject =  b.getMapCellObject();
				
				if (setCellsButton.isChecked()) {
					b.clearChildren();
					b.add(new Image(selectedNode.getIcon()));
					cellObject.setMenuPropertyObject(o);
				}
				else if (eraseCellsButton.isChecked()) {
					b.clearChildren();
					cellObject.setMenuPropertyObject(null);
				}
				else if (editCellsButton.isChecked()) {
					if (cellObject.getMenuPropertyObject() != null) {
						MapCellPropertyDialog mcpDialog = new MapCellPropertyDialog("Change cell properties", uiSkin, cellObject, this);
						mcpDialog.show(stage);
					}

				}
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
