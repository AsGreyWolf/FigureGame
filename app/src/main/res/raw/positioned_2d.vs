attribute vec3 in_pos;
attribute vec2 in_texcord;
uniform float in_aspect;
uniform vec3 in_position;

varying vec2 ex_texcord;

void main() {
	gl_Position = vec4(in_pos+in_position,1.0);
	ex_texcord = in_texcord;
}