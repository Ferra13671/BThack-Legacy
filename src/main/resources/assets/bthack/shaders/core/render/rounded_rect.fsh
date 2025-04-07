#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 resolution;
uniform vec2 position;
uniform vec2 size;
uniform float radius;
uniform vec4 color;

out vec4 fragColor;

const float edgeSoftness  = 2.;

// from https://iquilezles.org/articles/distfunctions
float roundedBoxSDF(vec2 centerPosition, vec2 size, float radius) {
    return length(max(abs(centerPosition)- size + radius, 0.)) - radius;
}

void main() {
    vec2 _position = vec2(position.x, resolution.y - position.y);

    vec2 size05 = size / 2.;
    float distance = roundedBoxSDF(vec2(gl_FragCoord.x - size05.x, gl_FragCoord.y + size05.y) - _position, size05, radius);

    fragColor = vec4(color.r, color.g, color.b, color.a - smoothstep(0., edgeSoftness, distance));
}