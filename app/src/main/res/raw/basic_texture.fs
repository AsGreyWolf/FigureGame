precision highp float;

varying vec2 ex_texcord;
uniform float in_time;
uniform sampler2D in_random;

uniform sampler2D in_tex;
void main() {
	gl_FragColor = texture2D(in_tex,ex_texcord);
}