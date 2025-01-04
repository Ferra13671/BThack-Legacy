#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 resolution;

vec3 rotatey(in vec3 p, float ang) { return vec3(p.x*cos(ang)-p.z*sin(ang),p.y,p.x*sin(ang)+p.z*cos(ang));  }
vec3 rotatex(in vec3 p, float ang) { return vec3(p.x,p.y*cos(ang)-p.z*sin(ang),p.y*sin(ang)+p.z*cos(ang));  }
vec3 rotatez(in vec3 p, float ang) { return vec3(p.x*cos(ang)-p.y*sin(ang),p.x*sin(ang)+p.y*cos(ang),p.z);  }


void main( void ) {

	vec2 p = 2.0*( gl_FragCoord.xy / resolution.xy ) -1.0;
	vec3 col = vec3(0);
	p.x *= resolution.x/resolution.y;

	for (int i = 0; i < 100; i++) {
		float a = float(i)*2.0*3.1415/100.0;
		vec2 sc = vec2(1,1);
		vec3 pos = vec3(cos(a)*sc.x,sin(a)*sc.y, 0.0);
		pos = rotatey(pos, 0.6*time);
		pos = rotatex(pos, 0.4*time);
		pos = rotatez(pos, 0.2*time);
		col += clamp(vec3(1,0,0)/(0.001+abs(12.0*length(p.xy-pos.xy)-4.0)), 0.0, 1000.0);

	}
	col /=100.0;
	gl_FragColor = vec4(col, 1.0);
}