package ru.asgreywolf.neofall.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ru.asgreywolf.neofall.App;
import ru.asgreywolf.neofall.R;
import ru.asgreywolf.neofall.StateListener;
import ru.asgreywolf.neofall.game.PlayerForm;

public class UIView extends FrameLayout implements StateListener {
	Context context;
	private OnClickListener listener;
	private static TextView points;
	private static ImageView liveBonus;

	public UIView(Context context) {
		super(context);
		this.context = context;
	}

	public UIView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public UIView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void connect(UIController controller) {
		listener = controller;
	}

	private void refreshSettings(){
		Drawable soundOn = getResources().getDrawable(R.drawable.ic_volume_up_white_48dp);
		Drawable soundOff = getResources().getDrawable(R.drawable.ic_volume_off_white_48dp);
		Drawable vibrateOn = getResources().getDrawable(R.drawable.ic_vibration_white_48dp);
		Drawable vibrateOff = getResources().getDrawable(R.drawable.ic_do_not_disturb_white_48dp);
		((TextView)this.findViewById(R.id.settingsSound)).setCompoundDrawablesWithIntrinsicBounds(App.getPreferences().getBoolean("SOUND", true) ? soundOn : soundOff, null, null, null);
		((TextView)this.findViewById(R.id.settingsVibrate)).setCompoundDrawablesWithIntrinsicBounds(App.getPreferences().getBoolean("VIBRO", true) ? vibrateOn : vibrateOff, null, null, null);
		((TextView)this.findViewById(R.id.settingsSound)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				SharedPreferences.Editor e = App.getPreferences().edit();
				e.putBoolean("SOUND", !App.getPreferences().getBoolean("SOUND", true));
				e.commit();
				refreshSettings();
			}
		});
		((TextView)this.findViewById(R.id.settingsVibrate)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				SharedPreferences.Editor e = App.getPreferences().edit();
				e.putBoolean("VIBRO", !App.getPreferences().getBoolean("VIBRO", true));
				e.commit();
				refreshSettings();
			}
		});
	}

	@Override
	public void OnStateChange(State state) {
		points = null;
		this.removeAllViews();
		switch (state) {
		case MENU:
			inflate(getContext(), R.layout.menu, this);
			this.findViewById(R.id.settingsButton).setOnClickListener(listener);
			this.findViewById(R.id.playButton).setOnClickListener(listener);
			break;
			case DIED:
				inflate(getContext(), R.layout.died, this);
				this.findViewById(R.id.settingsButton).setOnClickListener(listener);
				this.findViewById(R.id.playButton).setOnClickListener(listener);
				int lastScore = App.getPreferences().getInt("lastScore",-1);
				int bestScore = App.getPreferences().getInt("bestScore",-1);
				((TextView)this.findViewById(R.id.points)).setText(""+lastScore);
				((TextView)this.findViewById(R.id.best_score)).setText(""+bestScore);
				if(bestScore<=lastScore)
					this.findViewById(R.id.amazing).setVisibility(View.VISIBLE);
				break;
		case SETTINGS:
			inflate(getContext(), R.layout.settings, this);
			refreshSettings();
			break;
		case INGAME:
			inflate(getContext(), R.layout.gameui, this);
			points = (TextView) this.findViewById(R.id.points);
			liveBonus = (ImageView) this.findViewById(R.id.liveBonus);
			((ImageView) findViewById(R.id.square)).getDrawable().setColorFilter(PlayerForm.SQUARE.color(),
					Mode.MULTIPLY);
			((ImageView) findViewById(R.id.triangle)).getDrawable().setColorFilter(PlayerForm.TRIANGLE.color(),
					Mode.MULTIPLY);
			((ImageView) findViewById(R.id.circle)).getDrawable().setColorFilter(PlayerForm.CIRCLE.color(),
					Mode.MULTIPLY);
			break;
		case PAUSE:
			inflate(getContext(), R.layout.pause, this);
			this.findViewById(R.id.settingsButton).setOnClickListener(listener);
			this.findViewById(R.id.playButton).setOnClickListener(listener);
			break;
		default:
			break;
		}
	}

	static Handler setPointsHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			UIView.points.setText("" + msg.what);
		};
	};
	static Handler setLiveBonusHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what>0)
				UIView.liveBonus.setVisibility(View.VISIBLE);
			else
				UIView.liveBonus.setVisibility(View.GONE);
		};
	};

	public static void setPoints(double points) {
		setPointsHandler.sendEmptyMessage((int) points);
	}

	public static void setLiveBonus(boolean liveBonus) {
		setLiveBonusHandler.sendEmptyMessage(liveBonus?1:0);
	}
}
