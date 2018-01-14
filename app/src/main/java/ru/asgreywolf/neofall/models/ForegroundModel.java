package ru.asgreywolf.neofall.models;

import ru.asgreywolf.neofall.shaders.ForegroundShader;

public class ForegroundModel extends Model {
	private static ForegroundShader s = null;

	public ForegroundModel() {
		super();
		addQuad(0,-1,1,-1,1,0);
		addQuad(1,0,1,0,1);
	}

	@Override
	public void render() {
		if (s == null)
			s = new ForegroundShader();
		s.use();
		super.render();
	}
}
