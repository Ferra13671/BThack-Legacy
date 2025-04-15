#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 resolution;
uniform vec2 position;
uniform float time;
uniform vec2 size;
uniform float radius;
uniform vec4 color;
uniform vec4 outlineColor1;
uniform vec4 outlineColor2;
uniform float depth;
uniform float scale;
uniform float speed;

out vec4 fragColor;

const float edgeSoftness = 0.;
const float defaultGradientScale = 30000.;

// from https://iquilezles.org/articles/distfunctions
float roundedBoxSDF(vec2 centerPosition, vec2 size, float radius) {
    return length(max(abs(centerPosition)- size + radius, 0.)) - radius;
}

vec4 roundedRect(vec2 pos, vec2 scale, vec4 _color, float round) {
    vec2 scale05 = scale / 2.;
    float distance = roundedBoxSDF(vec2(gl_FragCoord.x - scale05.x, gl_FragCoord.y + scale05.y) - pos, scale05, round);
    return vec4(_color.r, _color.g, _color.b, _color.a - smoothstep(0., edgeSoftness, distance));
}

float _step(float value1, float value2, float state) {
    return value1 + state * (value2 - value1);
}

vec4 gradient() {
    float colorState = ceil(((time * (400. * speed * 2.5)) + (100. + ((((gl_FragCoord.x / resolution.x) + (gl_FragCoord.y / resolution.y)) * (defaultGradientScale * scale)) * 2.))) / 20.);
    colorState = mod(colorState, 360.) / 360.;
    if (colorState > 0.5) colorState = 1. - colorState;
    colorState *= 2.;

    return vec4(_step(outlineColor1.r, outlineColor2.r, colorState), _step(outlineColor1.g, outlineColor2.g, colorState), _step(outlineColor1.b, outlineColor2.b, colorState), _step(outlineColor1.a, outlineColor2.a, colorState));
}

void main() {
    vec4 gradientColor = gradient();
    vec4 outRoundColor = roundedRect(vec2(position.x, resolution.y - position.y), size, gradientColor, radius);
    vec4 defRoundColor = roundedRect(vec2(position.x + depth, resolution.y - (position.y + depth)), size - (depth * 2.), color, radius - depth);

    if (defRoundColor.a != color.a) fragColor = outRoundColor;
    else fragColor = defRoundColor;

    //fragColor = vec4(smoothstep(defRoundColor.r, outRoundColor.r, outRoundColor.a), smoothstep(defRoundColor.g, outRoundColor.g, outRoundColor.a), smoothstep(defRoundColor.b, outRoundColor.b, outRoundColor.a), smoothstep(defRoundColor.a, outRoundColor.a, outRoundColor.a));
}