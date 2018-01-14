package ru.asgreywolf.neofall.shaders;

import android.opengl.GLES20;
import ru.asgreywolf.neofall.R;
import ru.asgreywolf.neofall.game.PlayerForm;

public class PlayerShader extends Shader {
	private int colorUniform;
	private int formUniform;
	private PlayerForm form = PlayerForm.DEFAULT;
	private PlayerForm prevForm = PlayerForm.DEFAULT;
	float formStart;

	public PlayerShader() {
		super(R.raw.basic_2d, R.raw.player_shader);
		setAttribute("in_pos", SHADER_POS);
		setAttribute("in_texcord", SHADER_TEXCOORD);
		colorUniform = GLES20.glGetUniformLocation(program, "in_color");
		formUniform = GLES20.glGetUniformLocation(program, "in_form");
	}

	public void setForm(PlayerForm form) {
		if (this.form != form) {
			formStart = (System.currentTimeMillis() - getStartTime()) * 0.001f;
			prevForm = this.form;
			this.form = form;
		}
	}

	@Override
	public void use() {
		super.use();
		GLES20.glUniform3f(colorUniform, (float) form.colorR(), (float) form.colorG(), (float) form.colorB());
		GLES20.glUniform3f(formUniform, form.value(), prevForm.value(), formStart);
	}

}
