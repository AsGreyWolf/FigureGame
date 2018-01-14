package ru.asgreywolf.neofall.shaders;

import android.opengl.GLES20;

import ru.asgreywolf.neofall.R;

public class BonusShader extends Shader {
	private int textureUniform;
	private int positionUniform;

	public BonusShader() {
		super(R.raw.positioned_2d, R.raw.basic_texture);
		setAttribute("in_pos", SHADER_POS);
		setAttribute("in_texcord", SHADER_TEXCOORD);
		textureUniform = GLES20.glGetUniformLocation(program, "in_tex");
		positionUniform = GLES20.glGetUniformLocation(program, "in_position");
	}

	public double position;
	public Texture texture;

	@Override
	public void use() {
		super.use();
		texture.bind(0, textureUniform);
		GLES20.glUniform3f(positionUniform, 0, (float) position, 0);
	}
}
