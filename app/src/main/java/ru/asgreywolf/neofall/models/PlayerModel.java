package ru.asgreywolf.neofall.models;

import ru.asgreywolf.neofall.game.GameSurface;
import ru.asgreywolf.neofall.game.PlayerForm;
import ru.asgreywolf.neofall.shaders.PlayerShader;

public class PlayerModel extends Model {
	private static PlayerShader s = null;
	private PlayerForm form;

	public PlayerModel() {
		super();
		double sizex = GameSurface.xSizeToScreen(0.6);
		double sizey = GameSurface.ySizeToScreen(0.6*9/16);
		double X = sizex / 2;
		double x = -sizex / 2;
		double Y = GameSurface.yToScreen(0.9);
		double y = Y - sizey;
		addQuad(0,x,X,y,Y,0);
		addQuad(1,0,1,0,1);
	}

	public void setForm(PlayerForm form) {
		this.form = form;
	}

	@Override
	public void render() {
		if (s == null)
			s = new PlayerShader();
		s.setForm(form);
		s.use();
		super.render();
	}
}
