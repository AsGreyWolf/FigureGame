package ru.asgreywolf.neofall.gestures;

public interface OnGestureListener {
	public enum Gesture{
		SQUARE,
		TRIANGLE,
		CIRCLE,
		ANGLE,
		INVALID
	}
	public void onGesture(Gesture g, double posx, double posy);
	public void onPredict(Gesture g, double posx, double posy);
}
