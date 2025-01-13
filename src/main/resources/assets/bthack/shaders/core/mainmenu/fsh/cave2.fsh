#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 resolution;

float rand(vec3 p){
	return fract(sin(dot(p, vec3(23.5349, 12.4324, 94.5204))) * 8471.4309);
}

float rand(vec2 p){
	return fract(sin(dot(p, vec2(23.5349, 94.5204))) * 8471.4309);
}

vec3 path(float a){
	return vec3(pow(sin(a * .6), 2.) - 2. * exp(cos(a * .2 + 1.)), cos(a) * .6 + sin(a * 1.6) * .5, a);
}

vec3 path2(float a){
	return vec3(pow(sin(a + 1.), 2.) - 2., cos(a * 1.3) * .6 + sin(a + 2.1) * .5, a);
}


float map(vec3 p){
	float d1 = length(p - path(p.z)) - 1.;
	float d2 = length(p - path2(p.z)) - 1.;
	return -min(d1, d2);
}

float vertexAo(vec2 side, float corner) {
	//if (side.x == 1.0 && side.y == 1.0) return 1.0;
	return (side.x + side.y + max(corner, side.x * side.y)) / 3.0;
}

vec4 voxelAo(vec3 pos, vec3 d1, vec3 d2, float res) {
	d1 *= res;
	d2 *= res;
	vec4 side = vec4(map(pos + d1), map(pos + d2), map(pos - d1), map(pos - d2));
	vec4 corner = vec4(map(pos + d1 + d2), map(pos - d1 + d2), map(pos - d1 - d2), map(pos + d1 - d2));
	side = step(side, vec4(0.));
	corner = step(corner, vec4(0.));
	vec4 ao;
	ao.x = vertexAo(side.xy, corner.x);
	ao.y = vertexAo(side.yz, corner.y);
	ao.z = vertexAo(side.zw, corner.z);
	ao.w = vertexAo(side.wx, corner.w);
	return 1.0 - ao;
}

void main( void ) {

	vec2 p = ( gl_FragCoord.xy * 2. - resolution.xy )/ min(resolution.x, resolution.y);

	vec3 col = vec3(0.0);
	//col.xy = p;

	float speed = .5 * time;

	vec3 cp = vec3(sin(time) * 3., 0., -3.);
	cp = path(speed);
	vec3 t = vec3(0.);
	t = path(speed + .5);
	vec3 f = normalize(t - cp);
	vec3 u = vec3(0., 1., 0.);
	vec3 s = normalize(cross(u, f));
	u = normalize(cross(f, s));
	vec3 rd = normalize(p.x * s + p.y * u + f);

	float res = .25;
	vec3 mp = floor(cp / res) * res;
	vec3 rs = sign(rd) * res;
	vec3 dd = abs(1. / rd);
	vec3 sd = (sign(rd) * (mp - cp) + (sign(rd) * .5 + .5) * res) * dd;

	vec3 mask;
	int k;
	bool hit;

	for(int i = 0; i < 100; i++){
		if(map(mp) < 0.){
			hit = true;
			break;
		}
		mask = step(sd, sd.yzx) * step(sd, sd.zxy);
		sd += mask * dd * res;
		mp += mask * rs;
		k = i;
	}

	if(hit){
		float depth = dot(sd - dd * res, mask);

		vec3 uvw = fract((cp + depth * rd) / res);
		vec2 uv = vec2(dot(uvw.yzx, mask), dot(uvw.zxy, mask));
		vec2 uu = abs(uv - .5);
		float grid = smoothstep(.45, .5, max(uu.x, uu.y));

		vec4 ambient;
		ambient = voxelAo(mp - rs * mask, mask.zxy, mask.yzx, res);
		float interpAo = mix(mix(ambient.z, ambient.w, uv.x), mix(ambient.y, ambient.x, uv.x), uv.y);
		interpAo = pow(interpAo, 1.0 / 3.0);

		float a = .015;
		float tm = floor(time * .2 / a) * a;
		tm = floor(tm) + smoothstep(.8, 1., fract(tm));
		vec3 surf_col = mix(vec3(.65, .4, .95), vec3(.8), step(rand(mp + tm), .8));
		col += mix(surf_col, vec3(.3, .3, .3), grid);

		col *= min(1., 15. / depth / depth);
		interpAo = clamp(interpAo, 0., 1.);
		col *= interpAo * .65 + .35;
	}

	col = sqrt(col);

	gl_FragColor = vec4( col, 2.0 );

}