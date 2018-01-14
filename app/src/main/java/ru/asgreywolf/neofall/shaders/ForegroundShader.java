package ru.asgreywolf.neofall.shaders;

import ru.asgreywolf.neofall.R;

public class ForegroundShader extends Shader {

	public ForegroundShader() {
		super(R.raw.basic_2d, R.raw.foreground_shader);
		setAttribute("in_pos", SHADER_POS);
		setAttribute("in_texcord", SHADER_TEXCOORD);
	}

	@Override
	public void use() {
		super.use();
	}
}
