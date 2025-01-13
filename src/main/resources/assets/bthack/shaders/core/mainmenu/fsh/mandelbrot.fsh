#version 120
#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

// dashxdr 20200705
// Clone of https://www.shadertoy.com/view/ttscWn by Shane

void main(void) {
	vec3 col = vec3(0.0);
	const int AA=2;
	vec2 pos = gl_FragCoord.xy;
	vec2 h = resolution*.5;
	for(int j=0; j<AA; j++){
		for(int i=0; i<AA; i++) {
			vec2 p = (pos + vec2(i, j)/float(AA) - h)/resolution.y;
			float ttm = cos(sin(time/8.))*6.2831;
			p *= mat2(cos(ttm), sin(ttm), -sin(ttm), cos(ttm));
			p -= vec2(cos(time/2.)/2., sin(time/3.)/5.);
			float zm = (200. + sin(time/7.)*50.);
			vec2 cc = vec2(-.57735 + .004-.01, .57735) + p/zm;
			vec2 z = vec2(0), dz = vec2(0);
			const int iter = 128;
			int ik = 128;
			vec3 fog = vec3(0);
			for(int k=0; k<iter; k++){
				dz = mat2(z, -z.y, z.x)*dz*2. + vec2(1, 0);
				z =  mat2(z, -z.y, z.x)*z + cc;
				if(dot(z, z) > 1./.005){
					ik = k;
					break;
				}
			}
			float ln = step(0., length(z)/15.5  - 1.);
			float d = sqrt(1./max(length(dz), .0001))*log(dot(z, z));
			d = clamp(d*50., 0., 1.);
			float dir = mod(float(ik), 2.)<.5? -1. : 1.;
			float sh = (float(iter - ik))/float(iter);
			vec2 tuv = z/320.;
			float tm = (-ttm*sh*sh*16.);
			tuv *= mat2(cos(tm), sin(tm), -sin(tm), cos(tm));
			tuv = abs(mod(tuv, 1./8.) - 1./16.);
			float pat = smoothstep(0., 1./length(dz), length(tuv) - 1./64.);
			pat = min(pat, smoothstep(0., 1./length(dz), abs(max(tuv.x, tuv.y) - 1./16.) - .04/16.));
			vec3 lCol = pow(min(vec3(2.5, 1, 1)*min(d*.95, .86), 1.), vec3(1, 3, 16))*1.15;
			lCol = dir<.0? lCol*min(pat, ln) : (sqrt(lCol)*.5 + .7)*max(1. - pat, 1. - ln);
			vec3 rd = normalize(vec3(p, 1.));
			rd = reflect(rd, vec3(0, 0, -1));
			float diff = clamp(dot(z*.5 + .5, rd.xy), 0., 1.)*d;
			tuv = z/200.;
			tm = -tm/1.5 + .5;
			tuv *= mat2(cos(tm), sin(tm), -sin(tm), cos(tm));
			tuv = abs(mod(tuv, 1./8.) - 1./16.);
			pat = smoothstep(0., 1./length(dz), length(tuv) - 1./32.);
			pat = min(pat, smoothstep(0., 1./length(dz), abs(max(tuv.x, tuv.y) - 1./16.) - .04/16.));
			lCol += mix(lCol, vec3(1)*ln, .5)*diff*diff*.5*(pat*.6 + .6);
			if (mod(float(ik), 6.)<.5) lCol = lCol.yxz;
			lCol = mix(lCol.xzy, lCol, d/1.2);
			lCol = mix(lCol, vec3(0), (1. - step(0., -(length(z)*.05*float(ik)/float(iter)  - 1.)))*.95);
			lCol = mix(fog, lCol, sh*d);
			col += 1.0-(1.0-min( lCol, 1.));
		}
	}
	col /= float(AA*AA);
	vec2 uv = pos/resolution;
	col *= pow(abs(16.*(1. - uv.x)*(1. - uv.y)*uv.x*uv.y), 1./8.)*1.15;
	gl_FragColor = vec4(sqrt(max(col, 0.)), 1.0 );
}