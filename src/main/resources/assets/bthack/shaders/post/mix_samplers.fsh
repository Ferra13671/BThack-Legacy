#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D UpSampler;
uniform sampler2D DownSampler;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(UpSampler, texCoord);
    if (color.a == 0.)
        color = texture(DownSampler, texCoord);
    fragColor = color;
}