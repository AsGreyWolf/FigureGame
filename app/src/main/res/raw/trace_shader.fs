precision highp float;

varying vec2 ex_texcord;
uniform float in_time;
uniform sampler2D in_random;

const int maxx = 500;
vec4 randV(int i){
   vec2 co = vec2(float(i)/float(maxx), 0.0);
   return texture2D(in_random,co);
}

uniform vec2 in_pointer;
uniform vec3 in_color;
uniform float in_aspect;
uniform float in_factor;
uniform float in_clear;
uniform sampler2D in_tex;
void main() {
	vec2 diff = vec2(ex_texcord.x-in_pointer.x,(ex_texcord.y-1.0+in_pointer.y)/in_aspect);
	float d = clamp(1.0-length(diff)*10.0,0.0,1.0);

    //float rand = randV(int(ex_texcord.x*float(maxx))).x;
	vec4 c = texture2D(in_tex,ex_texcord);//+vec2(0.0,-rand*0.01)
	gl_FragColor = vec4(in_color,c.a+d);
	//gl_FragColor.a=min(gl_FragColor.a,0.8);
	if(in_clear>0.0)
		gl_FragColor.a = 1.0-in_clear;
}