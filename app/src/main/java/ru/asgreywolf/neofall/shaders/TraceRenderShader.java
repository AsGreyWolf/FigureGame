package ru.asgreywolf.neofall.shaders;

import android.opengl.GLES20;
import ru.asgreywolf.neofall.R;

public class TraceRenderShader extends Shader {
	Texture texture = null;
	private int texUniform = 0;

	public TraceRenderShader() {
		super(R.raw.basic_2d, R.raw.basic_texture);
		setAttribute("in_pos", SHADER_POS);
		setAttribute("in_texcord", SHADER_TEXCOORD);
		texUniform = GLES20.glGetUniformLocation(program, "in_tex");
	}

	@Override
	public void use() {
		super.use();
		texture.bind(1, texUniform);
	}
}
