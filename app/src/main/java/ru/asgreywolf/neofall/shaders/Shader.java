package ru.asgreywolf.neofall.shaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import android.opengl.GLES20;
import android.util.Log;
import ru.asgreywolf.neofall.App;
import ru.asgreywolf.neofall.game.GameSurface;

public class Shader {
	public static final int SHADER_POS = 0;
	public static final int SHADER_TEXCOORD = 1;
	protected static Texture randomTexture;

	protected int program;
	private int timeUniform;
	private int aspectUniform;
	private int randomUniform;
	private long startMillis;

	public Shader(int vertexResource, int fragmentResource) {
		this(readStream(App.getAppContext().getResources().openRawResource(vertexResource)),
				readStream(App.getAppContext().getResources().openRawResource(fragmentResource)));
	}

	public Shader(String vertexCode, String fragmentCode) {
		if (randomTexture == null)
			randomTexture = new RandomTexture(GameSurface.resolutionX, GameSurface.resolutionY);

		int vShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexCode);
		int fShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentCode);
		program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vShader);
		GLES20.glAttachShader(program, fShader);
		GLES20.glLinkProgram(program);
		int[] isLinked = new int[1];
		GLES20.glGetShaderiv(program, GLES20.GL_LINK_STATUS, isLinked, 0);
		if (isLinked[0] == GLES20.GL_FALSE)
			Log.e("testgame", GLES20.glGetProgramInfoLog(program));
		GLES20.glDeleteShader(vShader);
		GLES20.glDeleteShader(fShader);
		timeUniform = GLES20.glGetUniformLocation(program, "in_time");
		aspectUniform = GLES20.glGetUniformLocation(program, "in_aspect");
		randomUniform = GLES20.glGetUniformLocation(program, "in_random");
		startMillis = System.currentTimeMillis();
	}

	public long getStartTime() {
		return startMillis;
	}

	public void use() {
		GLES20.glUseProgram(program);
		GLES20.glUniform1f(timeUniform, (System.currentTimeMillis() - getStartTime()) * 0.001f);
		GLES20.glUniform1f(aspectUniform, (float) GameSurface.aspect());
		randomTexture.bind(0, randomUniform);
	}

	protected void setAttribute(String name, int id) {
		GLES20.glBindAttribLocation(program, id, name);
	}

	@Override
	protected void finalize() throws Throwable {
		if(program>=0)
			GLES20.glDeleteProgram(program);
	}

	private static String readStream(InputStream is) {
		StringBuilder sb = new StringBuilder(512);
		try {
			Reader r = new InputStreamReader(is, "UTF-8");
			int c = 0;
			while ((c = r.read()) != -1) {
				sb.append((char) c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	private static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		int[] isCompiled = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, isCompiled, 0);
		if (isCompiled[0] == GLES20.GL_FALSE) {
			Log.e("testgame", shaderCode);
			Log.e("testgame", GLES20.glGetShaderInfoLog(shader));
		}
		return shader;
	}
}
