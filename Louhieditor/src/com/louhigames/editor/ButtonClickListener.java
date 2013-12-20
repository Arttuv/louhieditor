package com.louhigames.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ButtonClickListener {

	private CallBack callBack;
	private ClickListener clickListener;
	private Actor button;
	
	public static ButtonClickListener createButtonClickListener(CallBack callBack, Actor button) {
		return new ButtonClickListener(callBack, button);
	}
	
	private ButtonClickListener(final CallBack listener, final Actor button) {
		this.callBack = listener;
		this.button = button;
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)  {
				System.out.println("CLICKED!");
				callBack.buttonClicked(new ChangeEvent(), button);
			}
	    });
	}
	
}
