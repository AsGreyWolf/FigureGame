precision highp float;

varying vec2 ex_texcord;
uniform float in_time;
uniform sampler2D in_random;

float sinR(float c){
	float sinR = c;
    sinR *= 2.0;
    if(sinR>1.0)
    	sinR = 2.0-sinR;
    sinR*=0.5;
    return sinR;
}
float sinF(float c){
    return c*6.28*2.0;
}
float triangleCircle(vec2 pos, float c){
	if(pos.x<-1.0 || pos.x>1.0 || pos.y<-1.0 || pos.y>1.0)
        return 0.0;
    float a = sqrt(3.0)/2.0;
    float h = sqrt(3.0)*a;
    float R = 1.0/max(c,0.0001)+sin(atan(pos.y,pos.x)*10.0+sinF(c))*sinR(c);
    pos.x=abs(pos.x);
    pos.y+=h/3.0;
	vec2 center = vec2(0.0, sqrt(R*R-a*a));
    if(pos.y<center.y-sqrt(R*R-pos.x*pos.x)) return 0.0;
    float L = sqrt(R*R - (h*h+a*a)/4.0);
    vec2 dir = normalize(vec2(-h, -a));
    center = vec2(a/2.0,h/2.0) + dir*L;
	pos-=center;
    if(dot(pos,pos)>R*R) return 0.0;
    return 1.0;
}
float circleSquare(vec2 pos, float c){
	pos=abs(pos);
	if(pos.x>1.0 || pos.y>1.0)
        return 0.0;
    float R = mix(1.0, 0.0, c);
    if(pos.x > 1.0-R && pos.y-1.0+R > sqrt(R*R-pow(pos.x-1.0+R,2.0)))
        return 0.0;
    return 1.0;
}
float quintic(float t){
	return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
}
float coeff(){
	float t = in_time;
    t = 2.0 * (t - floor(t));
    return clamp(t, 0.0, 1.0) - mix(0.0, 1.0, clamp(t - 1.0, 0.0, 1.0));
}
float coeff2(){
    float t = in_time/3.0;
    t = 2.0 * (t - floor(t));
    return clamp(t, 0.0, 1.0) - mix(0.0, 1.0, clamp(t - 1.0, 0.0, 1.0));
}
uniform vec3 in_color;
uniform vec3 in_form;
void main() {
	vec2 pos = ex_texcord * 2.0 - vec2(1.0, 1.0);
	float t = clamp((in_time-in_form.z)*3.0,0.0,1.0);
	float fi = (quintic(t)*0.0+quintic(coeff2())/4.0-1.0/8.0)*3.1415926535897932384626433832795;
	pos = mat2(cos(fi), sin(fi), -sin(fi), cos(fi)) * pos * 1.414;
	t = mix(in_form.y, in_form.x, clamp(quintic(t), 0.0, 1.0));
	float a = 0.0;
    if(t < 1.0){
    	a=circleSquare(pos, 1.0 - t);
    }
    else{
    	a=triangleCircle(pos, 2.0 - t);
    }
	gl_FragColor = vec4(in_color,a);
}
