package ru.asgreywolf.neofall;

public interface StateListener {
	public enum State{
		MENU,
		SETTINGS,
		INGAME,
		DIED,
		PAUSE
	}
	public void OnStateChange(State state);
}
