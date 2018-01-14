package ru.asgreywolf.neofall;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {
	private static Context context;

	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	public static Context getAppContext() {
		return context;
	}
	public static SharedPreferences getPreferences() {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", MODE_PRIVATE);
	}
}
