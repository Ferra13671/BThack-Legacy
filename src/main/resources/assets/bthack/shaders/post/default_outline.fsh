#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D InSampler;
in vec2 texCoord;
in vec2 oneTexel;
uniform vec4 color;
uniform vec4 outlinecolor;
out vec4 fragColor;
uniform int quality;
uniform int quality_multiplier;
uniform int extra_quality;

uniform vec2 InSize;


void main() {
    vec4 centerCol = texture(InSampler, texCoord);

    int qualityInternal = quality + (extra_quality * quality_multiplier);

    if(centerCol.a != 0) {
        fragColor = color;
    } else {
        for (int x = -qualityInternal; x < qualityInternal + 1; x++) {
            for (int y = -qualityInternal; y < qualityInternal + 1; y++) {
                vec2 offset = vec2(x, y);
                vec2 coord = texCoord + offset * oneTexel;
                vec4 t = texture(InSampler, coord);
                if (t.a != 0){
                    fragColor = outlinecolor;
                    return;
                }
            }
        }
        fragColor = vec4(0., 0., 0., 0.);
    }
}