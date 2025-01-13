#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 resolution;

#define linewidth .05
#define colordistmul .05
#define movespeed .5
#define swaymul 1.0

#define zigazaga_mode

float fmod(float x, float y)
{
	return x - y * floor(x/y);
}

vec3 hsv2rgb(vec3 c)
{
	vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
	vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
	return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

#ifdef zigazaga_mode
float Hash21(vec2 p)
{
	int x = int(mod(p.x, 6.0));
	int y = int(mod(p.y, 6.0));

	if (y == 0)
	{
		if (x == 0)
			return 0.0;
		else if (x == 1)
			return 1.0;
		else if (x == 2)
			return 1.0;
		else if (x == 3)
			return 0.0;
		else if (x == 4)
			return 1.0;
		else if (x == 5)
			return 1.0;
	}
	else if (y == 1)
	{
		if (x == 0)
			return 0.0;
		else if (x == 1)
			return 1.0;
		else if (x == 2)
			return 0.0;
		else if (x == 3)
			return 0.0;
		else if (x == 4)
			return 0.0;
		else if (x == 5)
			return 0.0;
	}
	else if (y == 2)
	{
		if (x == 0)
			return 1.0;
		else if (x == 1)
			return 1.0;
		else if (x == 2)
			return 0.0;
		else if (x == 3)
			return 1.0;
		else if (x == 4)
			return 1.0;
		else if (x == 5)
			return 0.0;
	}
	else if (y == 3)
	{
		if (x == 0)
			return 0.0;
		else if (x == 1)
			return 1.0;
		else if (x == 2)
			return 1.0;
		else if (x == 3)
			return 0.0;
		else if (x == 4)
			return 1.0;
		else if (x == 5)
			return 1.0;
	}
	else if (y == 4)
	{
		if (x == 0)
			return 0.0;
		else if (x == 1)
			return 0.0;
		else if (x == 2)
			return 0.0;
		else if (x == 3)
			return 0.0;
		else if (x == 4)
			return 1.0;
		else if (x == 5)
			return 0.0;
	}
	else if (y == 5)
	{
		if (x == 0)
			return 1.0;
		else if (x == 1)
			return 1.0;
		else if (x == 2)
			return 0.0;
		else if (x == 3)
			return 1.0;
		else if (x == 4)
			return 1.0;
		else if (x == 5)
			return 0.0;
	}
}
#else
float Hash21(vec2 p)
{
	p = fract(p*vec2(123.34, 456.21));
	p += dot(p, p+45.32);
	return fract(p.x*p.y);
}
#endif

vec3 rainbowstalin(vec2 pos)
{
	float dist = sqrt(dot(pos, pos)) * colordistmul;
	return hsv2rgb(vec3(fmod(dist, 1.0), 0.5, 1.0));
}

void main()
{
	vec2 uv = (gl_FragCoord.xy-.5 * resolution.xy) / resolution.y;

	vec3 col = vec3(0);

	float scaledtime = time * movespeed;
	uv *= 15.;
	uv += vec2(sin(scaledtime) * swaymul, scaledtime);
	vec2 gv = fract(uv)-.5;
	vec2 id = floor(uv);
	vec3 pointcolor = rainbowstalin(uv);

	float n = Hash21(id);

	if (n<.5) gv.x *= -1.;
	float d = abs(abs(gv.x + gv.y)-.5);
	float mask = smoothstep(.01, -.01, d-linewidth);
	col += mask * pointcolor;

	// if (gv.x>.48 || gv.y>.48) col = vec3(1,0,0);

	gl_FragColor = vec4(col, 1.0);
}