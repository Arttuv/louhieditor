package com.louhigames.editor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Louhieditor implements ApplicationListener {
	
	public static final String SKIN_LIBGDX_UI = "skin/louhiuiskin.json";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "skin/block_pictures_48x48.atlas";
	
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
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	    Table.drawDebug(stage);
	}

	private void buildUI() {
		
		System.out.println("BUILD UI!");

	    skin = getSkin();
	    
	    Table table = new Table();
	   
	    ButtonStyle buttonStyle = skin.get("default", ButtonStyle.class);
	    
	    int x = 10;
	    int y = 10;
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

	    table.setFillParent(true);
	    stage.addActor( table );
	    
	    table.debug(); // turn on all debug lines (table, cell, and widget)
	   // table.debugTable(); // turn on only table lines
	    
		/*		
        final float buttonX = ( width - BUTTON_WIDTH ) / 2;
        float currentY = 280f;
 
        // label "welcome"
        Label welcomeLabel = new Label( "Welcome!", getSkin() );
        welcomeLabel.setX(( ( width - welcomeLabel.getWidth() ) / 2 ));
        welcomeLabel.setY(( currentY + 100 ));
        stage.addActor( welcomeLabel );
 
        final float buttonX = ( width - BUTTON_WIDTH ) / 2;
        float currentY = 400f;
        
        // button "start game"
        TextButton startGameButton = new TextButton( "Start game", getSkin() );
        startGameButton.setX(buttonX);
        startGameButton.setY(currentY);
        startGameButton.setWidth(BUTTON_WIDTH);
        startGameButton.setHeight(BUTTON_HEIGHT);
        startGameButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Changed!");
			}
	});
        
        stage.addActor( startGameButton );
 
        // button "options"
        TextButton optionsButton = new TextButton( "Options", getSkin() );
        optionsButton.setX(buttonX);
        optionsButton.setY(( currentY -= BUTTON_HEIGHT + BUTTON_SPACING ));
        optionsButton.setWidth(BUTTON_WIDTH);
        optionsButton.setHeight(BUTTON_HEIGHT);
        stage.addActor( optionsButton );
 
        // button "hall of fame"
        TextButton hallOfFameButton = new TextButton( "Hall of Fame", getSkin() );
        hallOfFameButton.setX(buttonX);
        hallOfFameButton.setY(( currentY -= BUTTON_HEIGHT + BUTTON_SPACING ));
        hallOfFameButton.setWidth(BUTTON_WIDTH);
        hallOfFameButton.setHeight(BUTTON_HEIGHT);
        stage.addActor( hallOfFameButton );
        
        */
		
	}
	
	
	private Skin getSkin() {
		Skin skin = new Skin(Gdx.files.internal(SKIN_LIBGDX_UI), new TextureAtlas(TEXTURE_ATLAS_LIBGDX_UI));
		return skin;
	}
	
	@Override
	public void resize(int width, int height) {
		//buildUI();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
