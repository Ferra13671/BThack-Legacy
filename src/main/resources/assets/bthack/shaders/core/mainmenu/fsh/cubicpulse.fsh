#version 120
#extension GL_OES_standard_derivatives : enable

// dirtbox
#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

float iGlobalTime = time;
vec2 iResolution = resolution;


#define PI 3.141592653589793
#define TAU 6.283185307179586

float cubicPulse( float c, float w, float x ){
    x = abs(x - c);
    if( x>w ) return 0.0;
    x /= w;
    return 1.0 - x*x*(3.0-2.0*x);
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    float time = iGlobalTime * 0.55;
    vec2 p = (-iResolution.xy + 2.0*fragCoord)/iResolution.y;
    vec2 uvOrig = p;
    float rotZ = 1. - 0.23 * sin(1. * cos(length(p * 1.5)));
    p *= mat2(cos(rotZ), sin(rotZ), -sin(rotZ), cos(rotZ));
    float a = atan(p.y,p.x);
    float rSquare = pow( pow(p.x*p.x,4.0) + pow(p.y*p.y,4.0), 1.0/8.0 );
    float rRound = length(p);
    float r = mix(rSquare, rRound, 0.5 + 0.5 * sin(time * 2.));
    vec2 uv = vec2( 0.3/r + time, a/3.1415927 );

    uv += vec2(0., 0.25 * sin(time + uv.x * 1.2));
    uv /= vec2(1. + 0.0002 * length(uvOrig));
    vec2 uvDraw = fract(uv * 3.);

    float col = cubicPulse(0.5, 0.06, uvDraw.x);
    col = max(col, cubicPulse(0.5, 0.06, uvDraw.y));

    col = col * r * 0.8;
    col += 0.15 * length(uvOrig);
    fragColor = vec4(vec3(col*0.2, col, col+0.4+sin(time*10.0+col)*0.2), 1.);
}

void main( void ) {
    mainImage(gl_FragColor, gl_FragCoord.xy);
}