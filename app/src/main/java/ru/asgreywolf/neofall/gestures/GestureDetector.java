package ru.asgreywolf.neofall.gestures;

import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import ru.asgreywolf.neofall.gestures.OnGestureListener.Gesture;

public class GestureDetector implements OnTouchListener {
	private static final int TOUCH_RADIUS = 50 * 50;
	private ArrayList<Float> touchPointsX = new ArrayList<Float>();
	private ArrayList<Float> touchPointsY = new ArrayList<Float>();
	public static ArrayList<Float> touchAngleX = new ArrayList<Float>(); //// TODO: debug only
	public static ArrayList<Float> touchAngleY = new ArrayList<Float>(); //// TODO: debug only
	private OnGestureListener listener = null;

	public GestureDetector(OnGestureListener ogl) {
		listener = ogl;
	}

	float startX,startY;
	private Gesture predict(float endX, float endY){
		double minDistance = 90000;
		float distX= endX-startX;
		float distY= endY-startY;
		if(distX*distX+distY*distY>minDistance) return Gesture.INVALID;

		int count = 0;
		int countDistanced = 0;
		int prevI=-1;
		for(int i=0;i<touchAngleX.size();i++){
			int l = i-1;
			if(l<0) l += touchAngleX.size();
			int r = (i+1)%touchAngleX.size();
			float v1x = touchAngleX.get(i)-touchAngleX.get(l);
			float v1y = touchAngleY.get(i)-touchAngleY.get(l);
			float v2x = touchAngleX.get(r)-touchAngleX.get(i);
			float v2y = touchAngleY.get(r)-touchAngleY.get(i);
			if(v1x*v2y-v2x*v1y<0) {
				count++;
				if(prevI<0 || (touchAngleX.get(i)-touchAngleX.get(prevI))*(touchAngleX.get(i)-touchAngleX.get(prevI))+
						(touchAngleY.get(i)-touchAngleY.get(prevI))*(touchAngleY.get(i)-touchAngleY.get(prevI))>minDistance){
					countDistanced++;
					prevI=i;
				}
			}
		}
		if(count>=touchAngleX.size()/2)
			if (count > 4) {
				return Gesture.CIRCLE;
			} else if (countDistanced == 4) {
				return Gesture.SQUARE;
			} else if (countDistanced == 3) {
				return Gesture.TRIANGLE;
			} else if (count <= 1 && touchPointsX.size() <= 1) {
				return Gesture.ANGLE;
			}

		count = 0;
		countDistanced = 0;
		prevI=-1;
		for(int i=0;i<touchAngleX.size();i++){
			int l = i-1;
			if(l<0) l += touchAngleX.size();
			int r = (i+1)%touchAngleX.size();
			float v1x = touchAngleX.get(i)-touchAngleX.get(l);
			float v1y = touchAngleY.get(i)-touchAngleY.get(l);
			float v2x = touchAngleX.get(r)-touchAngleX.get(i);
			float v2y = touchAngleY.get(r)-touchAngleY.get(i);
			if(v1x*v2y-v2x*v1y>0) {
				count++;
				if(prevI<0 || (touchAngleX.get(i)-touchAngleX.get(prevI))*(touchAngleX.get(i)-touchAngleX.get(prevI))+
						(touchAngleY.get(i)-touchAngleY.get(prevI))*(touchAngleY.get(i)-touchAngleY.get(prevI))>minDistance/2){
					countDistanced++;
					prevI=i;
				}
			}
		}
		if(count>=touchAngleX.size()/2)
			if (count > 4) {
				return Gesture.CIRCLE;
			} else if (countDistanced == 4) {
				return Gesture.SQUARE;
			} else if (countDistanced == 3) {
				return Gesture.TRIANGLE;
			} else if (count <= 1 && touchPointsX.size() <= 1) {
				return Gesture.ANGLE;
			}

		return Gesture.INVALID;
	}

	private void track(float x, float y){
		float fromAngleX = x - touchAngleX.get(touchAngleX.size() - 1);
		float fromAngleY = y - touchAngleY.get(touchAngleY.size() - 1);
		if (fromAngleX * fromAngleX + fromAngleY * fromAngleY > TOUCH_RADIUS)
			for (int i = 1; i < touchPointsX.size(); i++) {
				float dx = x - touchPointsX.get(0);
				float dy = y - touchPointsY.get(0);
				{
					float len = (float) Math.sqrt(dx * dx + dy * dy);
					dx /= len;
					dy /= len;
				}
				float checkX = touchPointsX.get(i) - touchPointsX.get(0);
				float checkY = touchPointsY.get(i) - touchPointsY.get(0);

				if (checkX * checkX + checkY * checkY
						- (checkX * dx + checkY * dy) * (checkX * dx + checkY * dy) > TOUCH_RADIUS) {
					touchAngleX.add(touchPointsX.get(touchPointsX.size() - 1));
					touchAngleY.add(touchPointsY.get(touchPointsY.size() - 1));
					listener.onGesture(Gesture.ANGLE, touchAngleX.get(touchAngleX.size() - 1),
							touchAngleY.get(touchAngleY.size() - 1));
					touchPointsX.clear();
					touchPointsY.clear();
					break;
				}
			}

		touchPointsX.add(x);
		touchPointsY.add(y);
	}

	@Override
	public boolean onTouch(View view, MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchPointsX.clear();
			touchPointsY.clear();
			touchAngleX.clear();
			touchAngleY.clear();
			touchAngleX.add(e.getX());
			touchAngleY.add(e.getY());
			startX=e.getX();
			startY=e.getY();
			listener.onGesture(Gesture.ANGLE, e.getX(), e.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			track(e.getX(),e.getY());
			listener.onPredict(predict(e.getX(), e.getY()), e.getX(), e.getY());
			break;
		case MotionEvent.ACTION_UP:
			switch(predict(e.getX(),e.getY())){
				case CIRCLE:
					listener.onGesture(Gesture.CIRCLE, e.getX(), e.getY());
					break;
				case SQUARE:
					listener.onGesture(Gesture.SQUARE, e.getX(), e.getY());
					break;
				case TRIANGLE:
					listener.onGesture(Gesture.TRIANGLE, e.getX(), e.getY());
					break;
				case ANGLE:
					view.performClick();
					break;
			}
			break;
		}
		return true;
	}
}
