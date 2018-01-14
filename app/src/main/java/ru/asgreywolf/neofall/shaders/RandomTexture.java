package ru.asgreywolf.neofall.shaders;

import java.util.Random;

import android.graphics.Bitmap;

public class RandomTexture extends Texture {

	public RandomTexture(int w, int h) {
		super(generate(w, h));
	}

	private static Bitmap generate(int w, int h) {
		Random random = new Random();
		int[] data = new int[w * h];
		for (int i = 0; i < w * h; i++)
			data[i] = ((random.nextInt()&0xFF)<<24) | ((random.nextInt()&0xFF)<<16) | ((random.nextInt()&0xFF)<<8) | (random.nextInt()&0xFF);
		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bmp.setPixels(data, 0, w, 0, 0, w, h);
		return bmp;
	}
}
