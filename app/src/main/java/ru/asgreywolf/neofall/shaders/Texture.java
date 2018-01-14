package ru.asgreywolf.neofall.shaders;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
	private int id;

	private Texture() {
		int[] ids = new int[1];
		GLES20.glGenTextures(1, ids, 0);
		id = ids[0];
		if (id == 0)
			throw new RuntimeException("Error loading texture.");
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	}

	public Texture(Bitmap bmp) {
		this();
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
	}

	public Texture(int w, int h, int type) {
		this();
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, type, w, h, 0, type, GLES20.GL_UNSIGNED_BYTE, null);
	}

	public void bind(int index, int uniform) {
		if (uniform < 0) return;
		if (index < 0) return;
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
		GLES20.glUniform1i(uniform, index);
	}

	public void unbind(int index) {
		if (index < 0) return;
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	public int getId() {
		return id;
	}

	@Override
	protected void finalize() throws Throwable {
		if(id>=0)
			GLES20.glDeleteTextures(1, new int[] { id }, 0);
	}
}
