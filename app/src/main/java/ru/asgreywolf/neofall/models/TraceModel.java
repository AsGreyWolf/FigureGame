package ru.asgreywolf.neofall.models;

import ru.asgreywolf.neofall.shaders.TraceShader;

public class TraceModel extends Model {
	private static TraceShader s = null;
	public double x, y;
	public double colorR, colorG, colorB;
	public boolean clear = true;

	public TraceModel() {
		super();
		addQuad(0,-1,1,-1,1,0);
		addQuad(1,0,1,0,1);
	}

	@Override
	public void render() {
		if (s == null)
			s = new TraceShader();
		s.x = x;
		s.y = y;
		s.clear = clear;
		s.colorR = colorR;
		s.colorG = colorG;
		s.colorB = colorB;
		s.prepare();
		super.render();
		s.use();
		super.render();
	}
}
