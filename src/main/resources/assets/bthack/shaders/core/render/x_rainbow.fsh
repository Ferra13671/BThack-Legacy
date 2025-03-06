#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;
uniform float alpha;
uniform float brightness;

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
	float rainbowState = ceil(((time * 400.) + (100. + ((gl_FragCoord.x / resolution.x * 2400.) * 2.))) / 20.);
	rainbowState = mod(rainbowState, 360.);

	gl_FragColor = vec4(hsv2rgb(vec3((rainbowState / 360.), 0.6, brightness)), alpha);

}