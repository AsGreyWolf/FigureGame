package ru.asgreywolf.neofall.models;

import ru.asgreywolf.neofall.shaders.BackgroundShader;

public class BackgroundModel extends Model {
	private static BackgroundShader s = null;
	public double colorR, colorG, colorB;

	public BackgroundModel() {
		super();
		addQuad(0,-1,1,-1,1,0);
		addQuad(1,0,1,0,1);
	}

	@Override
	public void render() {
		if (s == null)
			s = new BackgroundShader();
		s.colorR = colorR;
		s.colorG = colorG;
		s.colorB = colorB;
		s.use();
		super.render();
	}
}
