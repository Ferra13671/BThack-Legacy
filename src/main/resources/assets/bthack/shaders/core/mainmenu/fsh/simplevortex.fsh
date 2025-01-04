#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 resolution;

void main( void ) {

    vec2 U = gl_FragCoord.xy;
    vec4 f = resolution.xyxy;
      f = length(U+=U-f.xy)/f;
      f = sin(f.w-.1) * vec4(sin(6./f + atan(U.x,U.y)*4. - time).w < 0.);

    gl_FragColor = vec4(f.x, 0, f.x * 5.4, 2);

}