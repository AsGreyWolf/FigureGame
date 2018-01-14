package ru.asgreywolf.neofall.shaders;

import android.opengl.GLES20;
import ru.asgreywolf.neofall.R;
import ru.asgreywolf.neofall.game.GameSurface;

public class TraceShader extends Shader {
	public double x, y;
	public double colorR, colorG, colorB;
	public boolean clear = true;
	private int pointerUniform;
	private int texUniform;
	private int factorUniform;
	private int clearUniform;
	private int colorUniform;
	private int fbo;

	private Texture texture1;
	private Texture texture2;
	private Texture depth;

	private TraceRenderShader trs;

	public TraceShader() {
		super(R.raw.basic_2d, R.raw.trace_shader);
		setAttribute("in_pos", SHADER_POS);
		setAttribute("in_texcord", SHADER_TEXCOORD);
		pointerUniform = GLES20.glGetUniformLocation(program, "in_pointer");
		texUniform = GLES20.glGetUniformLocation(program, "in_tex");
		factorUniform = GLES20.glGetUniformLocation(program, "in_factor");
		clearUniform = GLES20.glGetUniformLocation(program, "in_clear");
		colorUniform = GLES20.glGetUniformLocation(program, "in_color");
		texture1 = new Texture(GameSurface.resolutionX, GameSurface.resolutionY, GLES20.GL_RGBA);
		texture2 = new Texture(GameSurface.resolutionX, GameSurface.resolutionY, GLES20.GL_RGBA);
		depth = new Texture(GameSurface.resolutionX, GameSurface.resolutionY, GLES20.GL_DEPTH_COMPONENT);
		int[] buf = new int[1];
		GLES20.glGenFramebuffers(1, buf, 0);
		fbo = buf[0];
		trs = new TraceRenderShader();
	}

	int i = 0;

	public void prepare() {
		super.use();
		GLES20.glUniform2f(pointerUniform, (float) x, (float) y);
		GLES20.glUniform1f(factorUniform, 1.0f);// i==0?
		i++;
		i %= 60;
		GLES20.glUniform1f(clearUniform, clear ? 1.0f : 0.0f);
		GLES20.glUniform3f(colorUniform, (float) colorR, (float) colorG, (float) colorB);
		Texture texture = texture1;
		if (trs.texture == texture)
			texture = texture2;
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
				texture.getId(), 0);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D,
				depth.getId(), 0);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		if (trs.texture != null)
			trs.texture.bind(1, texUniform);
		trs.texture = texture;
	}

	@Override
	public void use() {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		trs.use();
	}
}
