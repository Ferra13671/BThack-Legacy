#version 150
#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

out vec4 BThack_FragColor;

void main( void ) {

    vec2 U = gl_FragCoord.xy;
    vec4 f = resolution.xyxy;
      f = length(U+=U-f.xy)/f;
      f = sin(f.w-.1) * vec4(sin(6./f + atan(U.x,U.y)*4. - time).w < 0.);

    BThack_FragColor = vec4(f.x, 0, f.x * 5.4, 2);

}