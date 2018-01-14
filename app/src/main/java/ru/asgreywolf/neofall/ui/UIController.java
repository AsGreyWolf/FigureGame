package ru.asgreywolf.neofall.ui;

import java.util.Stack;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import ru.asgreywolf.neofall.R;
import ru.asgreywolf.neofall.StateListener;
import ru.asgreywolf.neofall.StateListener.State;

public class UIController implements OnClickListener {
	private static State state;
	private static StateListener listener;
	private static UIView view;
	private static Stack<State> stateStack = new Stack<>();

	public UIController(UIView view) {
		this.view = view;
		view.connect(this);
		setState(State.MENU);
	}

	public static void setState(State newState) {
		if (state != newState) {
			switch(newState){
			case SETTINGS:
				stateStack.push(state);
				break;
			default:
				break;
			}
			if (listener != null)
				listener.OnStateChange(newState);
			view.OnStateChange(newState);
			state = newState;
		}
	}

	public void setStateListener(StateListener listener) {
		this.listener = listener;
	}

	public void onBackPressed() {
		switch(state){
		case INGAME:
			setState(State.PAUSE);
			break;
		case MENU:
			((Activity) view.context).finish();
			break;
		case SETTINGS:
			setState(stateStack.pop());
			break;
		default:
			setState(State.MENU);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playButton:
			setState(State.INGAME);
			break;
		case R.id.settingsButton:
			setState(State.SETTINGS);
			break;
		}
	}
}
