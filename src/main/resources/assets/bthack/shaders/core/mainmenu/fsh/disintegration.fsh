#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

// https://github.com/jewbob
uniform sampler2D texture;

uniform float time;
uniform vec2 resolution;
uniform float alpha;

varying vec3 pos;
varying vec3 normal;
varying vec4 tex_coord;


vec3 firePalette(float i){

    float T = 1400. + 1300.*i; // Temperature range (in Kelvin).
    vec3 L = vec3(7.4, 5.6, 4.4); // Red, green, blue wavelengths (in hundreds of nanometers).
    L = pow(L,vec3(5)) * (exp(1.43876719683e5/(T*L)) - 1.);
    return 1. - exp(-5e8/L); // Exposure level. Set to "50." For "70," change the "5" to a "7," etc.
}


vec3 hash33(vec3 p){

    float n = sin(dot(p, vec3(7, 157, 113)));
    return fract(vec3(2097152, 262144, 32768)*n);
}


float voronoi(vec3 p){

    vec3 b, r, g = floor(p);
    p = fract(p);
    float d = 1.;

    for(int j = -1; j <= 1; j++) {
        for(int i = -1; i <= 1; i++) {

            b = vec3(i, j, -1);
            r = b - p + hash33(g+b);
            d = min(d, dot(r,r));

            b.z = 0.0;
            r = b - p + hash33(g+b);
            d = min(d, dot(r,r));

            b.z = 1.;
            r = b - p + hash33(g+b);
            d = min(d, dot(r,r));

        }
    }

    return d; // Range: [0, 1]
}


float noiseLayers(in vec3 p) {

    vec3 t = vec3(0., 0., p.z + time*1.5);

    const int iter = 5; // Just five layers is enough.
    float tot = 0., sum = 0., amp = 1.; // Total, sum, amplitude.

    for (int i = 0; i < iter; i++) {
        tot += voronoi(p + t) * amp; // Add the layer to the total.
        p *= 2.; // Position multiplied by two.
        t *= 1.5; // Time multiplied by less than two.
        sum += amp; // Sum of amplitudes.
        amp *= .5; // Decrease successive layer amplitude, as normal.
    }

    return tot/sum; // Range: [0, 1].
}


vec3 hsv2rgb(vec3 c) {
	vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
	vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
	return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}



void main() {
    // Screen coordinates.


  vec2 uv = (gl_FragCoord.xy - resolution.xy*.5) / resolution.y;

    // Shifting the central position around, just a little, to simulate a
    // moving camera, albeit a pretty lame one.
    uv += vec2(sin(time*.5)*.25, cos(time*.5)*.125);

    // Constructing the unit ray.
    vec3 rd = normalize(vec3(uv.x, uv.y, 3.1415926535898/8.));

    // Rotating the ray about the XY plane, to simulate a rolling camera.
    float cs = cos(time*.25), si = sin(time*.25);
    // Apparently "r *= rM" can break in some older browsers.
    rd.xy = rd.xy*mat2(cs, -si, si, cs);

    // Passing a unit ray multiple into the Voronoi layer function, which
    // is nothing more than an fBm setup with some time dialation.
    float c = noiseLayers(rd*2.);

    // Optional: Adding a bit of random noise for a subtle dust effect.
    c = max(c + dot(hash33(rd)*2. - 1., vec3(.015)), 0.);

    // Coloring:

    // Nebula.
    c *= sqrt(c)*1.5; // Contrast.
    vec3 col = firePalette(c); // Palettization.
    //col = mix(col, col.zyx*.1+ c*.9, clamp((1.+rd.x+rd.y)*0.45, 0., 1.)); // Color dispersion.
    //col = mix(col, col.zyx*.15 + c*.85, min(pow(dot(rd.xy, rd.xy)*1.2, 1.5), 1.)); // Color dispersion.
    //col = pow(col, vec3(1.25)); // Tweaking the contrast a little.

    // The fire palette on its own. Perhaps a little too much fire color.
    //c = pow(c*1.33, 1.25);
    //vec3 col =  firePalette(c);

    // Black and white, just to keep the art students happy. :)
    //c *= c*1.5;
    //col = vec3(c,c,c);

    // Rough gamma correction, and done.
	//vec2 position = (gl_FragCoord.xy / resolution.xy);
	//vec3 rgb = hsv2rgb(vec3(position.x + position.y + (time / 5.0), 0.4, 1));
	//vec4 rgba = vec4(rgb.xyz, 255);

    gl_FragColor = vec4(sqrt(clamp(col, 0., 1.)) , 1);
    // gl_FragColor = vec4(1, 1, 1, 0.3);
}