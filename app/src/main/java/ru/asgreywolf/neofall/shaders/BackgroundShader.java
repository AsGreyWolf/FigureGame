package ru.asgreywolf.neofall.shaders;

import android.opengl.GLES20;
import ru.asgreywolf.neofall.R;

public class BackgroundShader extends Shader {
	private int colorUniform;

	public BackgroundShader() {
		super(R.raw.basic_2d, R.raw.background_shader);
		setAttribute("in_pos", SHADER_POS);
		setAttribute("in_texcord", SHADER_TEXCOORD);
		colorUniform = GLES20.glGetUniformLocation(program, "in_color");
	}

	public double colorR, colorG, colorB;

	@Override
	public void use() {
		super.use();
		GLES20.glUniform3f(colorUniform, (float) colorR, (float) colorG, (float) colorB);
	}
}
