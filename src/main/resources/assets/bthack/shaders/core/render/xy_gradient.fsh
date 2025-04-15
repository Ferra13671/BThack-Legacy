#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;
uniform float brightness;
uniform float scale;
uniform float speed;
uniform vec4 color1;
uniform vec4 color2;

out vec4 fragColor;

float _step(float value1, float value2, float state) {
    return value1 + state * (value2 - value1);
}

void main() {
    float colorState = ceil(((time * (400. * speed * 2.5)) + (100. + ((((gl_FragCoord.x / resolution.x) + (gl_FragCoord.y / resolution.y)) * (6000. * scale)) * 2.))) / 20.);
    colorState = mod(colorState, 360.) / 360.;
    if (colorState > 0.5) colorState = 1. - colorState;
    colorState *= 2.;

    fragColor = vec4(_step(color1.r, color2.r, colorState) * brightness, _step(color1.g, color2.g, colorState) * brightness, _step(color1.b, color2.b, colorState) * brightness, _step(color1.a, color2.a, colorState) * brightness);
}