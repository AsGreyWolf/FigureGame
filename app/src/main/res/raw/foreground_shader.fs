precision highp float;

varying vec2 ex_texcord;
uniform float in_time;
uniform sampler2D in_random;

const int maxx = 500;
vec4 randV(int i){
   vec2 co = vec2(float(i)/float(maxx), 0.0);
   return texture2D(in_random,co);
}

void main() {
	float c = 0.0;
	vec3 v = randV(int(ex_texcord.x*float(maxx))).xyz;
    if(v.y>=0.8){
		float len = 0.5;
        float cameraSpeed = 0.5*v.y;
        float pos = mod(1.0-ex_texcord.y*len+v.z*len+in_time*cameraSpeed,len);
        float bot = v.x*len;
        float top = bot+0.06*v.y*len;
        if(pos>=bot && pos<=top)
            c=1.0;
	}
	gl_FragColor = vec4(vec3(1.0),c);
}