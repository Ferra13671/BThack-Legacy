#version 150

#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;
uniform int quality;
uniform int lineWidth;
uniform float scale;
uniform float time;
uniform vec2 resolution;
uniform float brightness;
uniform float fillAlpha;
uniform float outlineAlpha;
uniform float speed;
uniform float saturation;

uniform vec2 InSize;

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {

    float rainbowState = ceil(((time * 400. * speed) + (100. + ((gl_FragCoord.y / resolution.y * scale) * 2.))) / 20.);
    rainbowState = mod(rainbowState, 360.);

    vec3 rainbowColor = vec3(hsv2rgb(vec3((rainbowState / 360.), saturation, brightness)));

    vec4 centerCol = texture(DiffuseSampler, texCoord);

    if(centerCol.a != 0) {
        fragColor = vec4(rainbowColor, fillAlpha);
    } else {
        float alphaOutline = 0;
        vec3 colorFinal = vec3(-1);
        for (int x = -quality; x < quality + 1; x++) {
            for (int y = -quality; y < quality + 1; y++) {
                vec2 offset = vec2(x, y);
                vec2 coord = texCoord + offset * oneTexel;
                vec4 t = texture(DiffuseSampler, coord);
                if (t.a != 0){
                    fragColor = vec4(rainbowColor.rgb, outlineAlpha);
                    return;
                }
            }
        }
        fragColor = vec4(colorFinal, alphaOutline);
    }
}