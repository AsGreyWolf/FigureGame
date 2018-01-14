package ru.asgreywolf.neofall;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import ru.asgreywolf.neofall.game.GameController;
import ru.asgreywolf.neofall.game.GameControllerImpl;
import ru.asgreywolf.neofall.game.GameView;
import ru.asgreywolf.neofall.ui.UIController;
import ru.asgreywolf.neofall.ui.UIView;

public class MainActivity extends Activity {
	private UIController uiController;
	private UIView uiView;
	private GameView surface;
	private GameController gameController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutInflater().inflate(R.layout.main, null));
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		surface = (GameView) findViewById(R.id.Surface);
		gameController = new GameControllerImpl(surface);
		uiView = (UIView) findViewById(R.id.UI);
		uiController = new UIController(uiView);
		uiController.setStateListener(gameController);
	}

	@Override
	public void onBackPressed() {
		uiController.onBackPressed();
	}
}
