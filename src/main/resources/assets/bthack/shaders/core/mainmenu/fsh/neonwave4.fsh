#version 120
#ifdef GL_FRAGMENT_PRECISION_HIGH
	precision highp float;
#else
	precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

void main( void ) {
	vec2 pos = ( gl_FragCoord.xy / resolution.xy );
	float three = 1.0 - ((pos.x + pos.y) / 3.0);
	vec3 color = vec3(three, pos.x, pos.y);
	vec3 color2 = color;

	pos = gl_FragCoord.xy / resolution.xy * 2.0 - 1.0;
	color *= abs(1.0 / (sin(pos.y + sin(pos.y + time) * 0.4) * 60.0));
	color2 *= abs(1.0 / (sin(pos.y + cos(pos.x + time) * 0.7) * 6.0));
	color += color2;
	color /= 1.0;

	gl_FragColor = vec4(color, 6.0 );
}