precision highp float;

varying vec2 ex_texcord;
uniform float in_time;
uniform sampler2D in_random;

uniform vec3 in_color;
void main() {
	vec2 pos = ex_texcord * 2.0 - vec2(1.0, 1.0);
	pos.x = abs(pos.x);
	if(pos.x<0.5)
		gl_FragColor = vec4(in_color,0.6);
	else
		gl_FragColor = vec4(vec3(0.7),1.0);
}