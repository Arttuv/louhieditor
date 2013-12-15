package com.louhigames.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Louhieditor";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 480;
		
		new LwjglApplication(new Louhieditor(), cfg);
	}
}
