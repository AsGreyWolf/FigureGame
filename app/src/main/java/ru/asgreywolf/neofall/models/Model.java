package ru.asgreywolf.neofall.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

public class Model {
	private final int bufferDims[] = new int[] { 3, 2 };
	private int bufferIds[] = new int[2];
	private ArrayList<Double> buffers[] = new ArrayList[2];
	private boolean valid = true;

	public Model() {
		GLES20.glGenBuffers(2, bufferIds, 0);
		for (int i = 0; i < bufferIds.length; i++) {
			buffers[i] = new ArrayList<Double>();
		}
	}

	public void addToBuffer(int buffer, double x, double y) {
		buffers[buffer].add(x);
		buffers[buffer].add(y);
		valid = false;
	}

	public void addToBuffer(int buffer, double x, double y, double z) {
		buffers[buffer].add(x);
		buffers[buffer].add(y);
		buffers[buffer].add(z);
		valid = false;
	}

	public void addQuad(int buffer, double x, double X, double y, double Y, double z) {
		addToBuffer(buffer, x, y, z);
		addToBuffer(buffer, X, y, z);
		addToBuffer(buffer, X, Y, z);
		addToBuffer(buffer, x, y, z);
		addToBuffer(buffer, X, Y, z);
		addToBuffer(buffer, x, Y, z);
	}

	public void addQuad(int buffer, double x, double X, double y, double Y) {
		addToBuffer(buffer, x, y);
		addToBuffer(buffer, X, y);
		addToBuffer(buffer, X, Y);
		addToBuffer(buffer, x, y);
		addToBuffer(buffer, X, Y);
		addToBuffer(buffer, x, Y);
	}

	private void validate() {
		if (valid)
			return;
		valid = true;
		for (int i = 0; i < bufferIds.length; i++) {
			ByteBuffer bb = ByteBuffer.allocateDirect(buffers[i].size() * 4);
			bb.order(ByteOrder.nativeOrder());
			FloatBuffer fb = bb.asFloatBuffer();
			for (int j = 0; j < buffers[i].size(); j++)
				fb.put((float) (double) buffers[i].get(j));
			fb.position(0);
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIds[i]);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffers[i].size() * 4, fb, GLES20.GL_STATIC_DRAW);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		GLES20.glDeleteBuffers(2, bufferIds, 0);
	}

	public void render() {
		validate();
		for (int i = 0; i < bufferIds.length; i++) {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIds[i]);
			GLES20.glEnableVertexAttribArray(i);
			GLES20.glVertexAttribPointer(i, bufferDims[i], GLES20.GL_FLOAT, false, 0, 0);
		}
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffers[0].size());
		for (int i = 0; i < bufferIds.length; i++) {
			GLES20.glDisableVertexAttribArray(i);
		}
	};
}
