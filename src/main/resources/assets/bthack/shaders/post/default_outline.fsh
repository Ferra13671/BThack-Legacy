#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
in vec2 oneTexel;
uniform vec4 color;
uniform vec4 outlinecolor;
out vec4 fragColor;
uniform int quality;

uniform vec2 InSize;


void main() {
    vec4 centerCol = texture(DiffuseSampler, texCoord);

    if(centerCol.a != 0) {
        fragColor = color;
    } else {
        for (int x = -quality; x < quality + 1; x++) {
            for (int y = -quality; y < quality + 1; y++) {
                vec2 offset = vec2(x, y);
                vec2 coord = texCoord + offset * oneTexel;
                vec4 t = texture(DiffuseSampler, coord);
                if (t.a != 0){
                    fragColor = outlinecolor;
                    return;
                }
            }
        }
        fragColor = vec4(0., 0., 0., 0.);
    }
}