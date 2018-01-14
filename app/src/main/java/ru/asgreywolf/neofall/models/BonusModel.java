package ru.asgreywolf.neofall.models;

import ru.asgreywolf.neofall.game.GameSurface;
import ru.asgreywolf.neofall.shaders.BonusShader;
import ru.asgreywolf.neofall.shaders.Texture;

public class BonusModel extends Model {
	private static BonusShader s = null;
	private double position = 0;
	private Texture tex;

	public BonusModel() {
		super();
		double sizex = GameSurface.xSizeToScreen(0.6);
		double sizey = GameSurface.ySizeToScreen(0.6*9/16);
		double X = sizex / 2;
		double x = -sizex / 2;
		double Y = GameSurface.yToScreen(0.9);
		double y = Y - sizey;
		addQuad(0,x,X,y,Y,0);
		addQuad(1,0,1,1,0);
	}

	public void setPosition(double pos) {
		position = pos;
	}
	public void setTexture(Texture tex) {
		this.tex = tex;
	}

	@Override
	public void render() {
		if (position < -1.0 || position >= 3.0)
			return;
		if (s == null)
			s = new BonusShader();
		s.position = -position;
		s.texture = tex;
		s.use();
		super.render();
	}

}
