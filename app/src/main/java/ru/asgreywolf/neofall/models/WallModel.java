package ru.asgreywolf.neofall.models;

import ru.asgreywolf.neofall.game.GameSurface;
import ru.asgreywolf.neofall.shaders.WallShader;

public class WallModel extends Model {
	private static WallShader s = null;
	private double position = 0;
	private double colorR, colorG, colorB;

	public WallModel() {
		super();
		double sizey = GameSurface.ySizeToScreen(0.1);
		double X = 1;
		double x = -1;
		double Y = GameSurface.yToScreen(0.9) - GameSurface.ySizeToScreen(0.3375) * GameSurface.aspect();
		double y = Y - sizey;
		addQuad(0,x,X,y,Y,0);
		addQuad(1,0,1,0,1);
	}

	public void setPosition(double pos) {
		position = pos;
	}

	public void setColor(double colorR, double colorG, double colorB) {
		this.colorR = colorR;
		this.colorG = colorG;
		this.colorB = colorB;
	}

	@Override
	public void render() {
		if (position < -1.0 || position >= 3.0)
			return;
		if (s == null)
			s = new WallShader();
		s.position = -position;
		s.colorR = colorR;
		s.colorG = colorG;
		s.colorB = colorB;
		s.use();
		super.render();
	}

}
