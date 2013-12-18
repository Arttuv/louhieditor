package com.louhigames.editor.objects;

import java.util.ArrayList;

public class MapObject {

	private int id;
	private String name;
	public MapCellObject start;
	public ArrayList<MapCellObject> goals;
	public ArrayList<MapCellObject> gameObjects;
	
	public MapObject(int id, String name) {
		this.id = id;
		this.name = name;
		this.goals = new ArrayList<MapCellObject>();
		this.gameObjects = new ArrayList<MapCellObject>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MapCellObject getStart() {
		return start;
	}

	public void setStart(MapCellObject start) {
		this.start = start;
	}

	public ArrayList<MapCellObject> getGoals() {
		return goals;
	}

	public void setGoals(ArrayList<MapCellObject> goals) {
		this.goals = goals;
	}

	public ArrayList<MapCellObject> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(ArrayList<MapCellObject> gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	public void addGoal(MapCellObject goal) {
		this.goals.add(goal);
	}
	
	public void addGameObject(MapCellObject gameObject) {
		this.gameObjects.add(gameObject);
	}
	
}
