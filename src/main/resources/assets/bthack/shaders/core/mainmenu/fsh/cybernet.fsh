#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 resolution;

const float speed = 0.06;
const float bright = 0.3;
const float direction = 0.5;

float distLine(vec2 p,vec2 a,vec2 b){
	vec2 ap = p - a;
	vec2 ab = b - a;
	float t = clamp(dot(ap,ab)/dot(ab,ab),0.,1.);
	return length(ap-ab*t);
}

float Line(vec2 p,vec2 a,vec2 b){
	float d = distLine(p,a,b);
	d = smoothstep(.02,.01,d);
	float d2 = length(a-b);
	return d*smoothstep(1.5,.6,d2);
}

float N21(vec2 p){
	p = fract(p*vec2(234.334,125.64));
	p+=dot(p,p+25.34);
	return fract(p.x*p.y);
}

vec2 N22(vec2 p){
	float n = N21(p);
	return vec2(n,N21(p+n));
}

vec2 getPos(vec2 id,vec2 offs){
	vec2 n = N22(id+offs)*time;

	return offs+sin(n)*.4;
}

float layer(vec2 uv){
	vec2 id = floor(uv);
	vec2 fd = fract(uv)-.5;
	vec2 p[9];
	p[0] = getPos(id,vec2(-1,-1));
	p[1] = getPos(id,vec2(0,-1));
	p[2] = getPos(id,vec2(1,-1));
	p[3] = getPos(id,vec2(-1,0));
	p[4] = getPos(id,vec2(0,0));
	p[5] = getPos(id,vec2(1,0));
	p[6] = getPos(id,vec2(-1,1));
	p[7] = getPos(id,vec2(0,1));
	p[8] = getPos(id,vec2(1,1));
	float m = 0.;
	for(int i=0;i<9;i++){
		m+=Line(fd,p[4],p[i]);
		vec2 j = (p[i] - fd)*15.;
		float sparkle = 1./dot(j,j);
		m += sparkle*(sin(time*10.+fract(p[i].x)*10.)*.5+.5);
	}
	m+=Line(fd,p[1],p[3]);
	m+=Line(fd,p[1],p[5]);
	m+=Line(fd,p[7],p[3]);
	m+=Line(fd,p[7],p[5]);
	return m;
}

void main( void ) {

	vec2 uv = ( gl_FragCoord.xy/resolution.xy ) -direction;
	uv.x*=resolution.x/resolution.y;
	vec3 col = vec3(0.);

	//uv*=5.;
	float m = bright;
	float t = time*speed;
	float Y = uv.y;

	float s = sin(t);
	float c = cos(t);
	mat2 rot = mat2(c,-s,s,c);
	uv*=rot;
	for(float i=0.;i<1.;i+=1./4.){
		float z = fract(i+t);
		float size = mix(30.,.5,z);
		float fade = smoothstep(0.,.6,z)*smoothstep(1.,.8,z);
		m += layer(uv*size+i*20.)*fade;
	}
	vec3 base = sin(t*5.*vec3(.345,.456,.568))*.4+.6;
	col = m*base;
	col-=Y*base;
	//col = fd.x>0.48||fd.y>0.48?vec3(1,0,0):col;
	gl_FragColor = vec4( col, 1.0 );
}