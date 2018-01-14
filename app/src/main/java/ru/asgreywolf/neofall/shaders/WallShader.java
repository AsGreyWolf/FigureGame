package ru.asgreywolf.neofall.shaders;

import android.opengl.GLES20;
import ru.asgreywolf.neofall.R;

public class WallShader extends Shader {
	private int colorUniform;
	private int positionUniform;

	public WallShader() {
		super(R.raw.positioned_2d, R.raw.wall_shader);
		setAttribute("in_pos", SHADER_POS);
		setAttribute("in_texcord", SHADER_TEXCOORD);
		colorUniform = GLES20.glGetUniformLocation(program, "in_color");
		positionUniform = GLES20.glGetUniformLocation(program, "in_position");
	}

	public double position;
	public double colorR;
	public double colorG;
	public double colorB;

	@Override
	public void use() {
		super.use();
		GLES20.glUniform3f(colorUniform, (float) colorR, (float) colorG, (float) colorB);
		GLES20.glUniform3f(positionUniform, 0, (float) position, 0);
	}
}
