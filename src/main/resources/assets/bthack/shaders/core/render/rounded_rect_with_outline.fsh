#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 resolution;
uniform vec2 position;
uniform vec2 size;
uniform float radius;
uniform vec4 color;
uniform vec4 outlineColor;
uniform float depth;

out vec4 fragColor;

const float edgeSoftness = 0.;

// from https://iquilezles.org/articles/distfunctions
float roundedBoxSDF(vec2 centerPosition, vec2 size, float radius) {
    return length(max(abs(centerPosition)- size + radius, 0.)) - radius;
}

vec4 roundedRect(vec2 pos, vec2 scale, vec4 _color, float round) {
    vec2 scale05 = scale / 2.;
    float distance = roundedBoxSDF(vec2(gl_FragCoord.x - scale05.x, gl_FragCoord.y + scale05.y) - pos, scale05, round);
    return vec4(_color.r, _color.g, _color.b, _color.a - smoothstep(0., edgeSoftness, distance));
}

void main() {
    vec4 outRoundColor = roundedRect(vec2(position.x, resolution.y - position.y), size, outlineColor, radius);
    vec4 defRoundColor = roundedRect(vec2(position.x + depth, resolution.y - (position.y + depth)), size - (depth * 2.), color, radius - depth);

    if (defRoundColor.a != color.a) fragColor = outRoundColor;
    else fragColor = defRoundColor;

    //fragColor = vec4(smoothstep(defRoundColor.r, outRoundColor.r, outRoundColor.a), smoothstep(defRoundColor.g, outRoundColor.g, outRoundColor.a), smoothstep(defRoundColor.b, outRoundColor.b, outRoundColor.a), smoothstep(defRoundColor.a, outRoundColor.a, outRoundColor.a));
}