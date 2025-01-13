#version 120
#extension GL_OES_standard_derivatives : enable

// Parts stolen from: https://gist.github.com/patriciogonzalezvivo/670c22f3966e662d2f83
// Then, I derped it until it looked pretty.

#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

#define PI 3.1416
#define screenWidth resolution.x

float rand(vec2 c){
    return fract(sin(dot(c.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float noise(vec2 p, float freq ){
    float unit = screenWidth/freq;
    vec2 ij = floor(p/unit);
    vec2 xy = mod(p,unit)/unit;
    //xy = 3.*xy*xy-2.*xy*xy*xy;
    xy = .5*(1.-cos(PI*xy));
    float a = rand((ij+vec2(0.,0.)));
    float b = rand((ij+vec2(1.,0.)));
    float c = rand((ij+vec2(0.,1.)));
    float d = rand((ij+vec2(1.,1.)));
    float x1 = mix(a, b, xy.x);
    float x2 = mix(c, d, xy.x);
    return mix(x1, x2, xy.y);
}

float pNoise(vec2 p, int res){
    float persistance = .5;
    float n = 0.;
    float normK = 0.;
    float f = 4.;
    float amp = 1.;
    int iCount = 0;
    for (int i = 0; i<50; i++){
        n+=amp*noise(p, f);
        f*=2.;
        normK+=amp;
        amp*=persistance;
        if (iCount == res) break;
        iCount++;
    }
    float nf = n/normK;
    return nf*nf*nf*nf;
}
uniform sampler2D s;

const float color_intensity = 1.45;
const float Pi = 3.14159;

void main()
{
    vec2 p = gl_FragCoord.xy / resolution.xy;
    for (int i=1;i<5;i++)
    {

        vec2 newp=p;
        float ii = float(i);
        newp.x+=0.55/ii*sin(pNoise(p * 2500., 3) * ii*Pi*p.y+time*.15+cos((time/(10.*ii))*ii));
        newp.y+=0.55/ii*cos(pNoise(p * 2500., 3) * ii*Pi*p.x+time*.15+sin((time/(10.*ii))*ii));
        p=newp;
    }
    vec3 col=vec3(cos(p.x+p.y+3.)*.5+.75, sin(p.x+p.y+6.)*.5+.35, (sin(p.x+p.y+9.)+cos(p.x+p.y+12.))*.45+.25);
    gl_FragColor=vec4(col*col, 1.0);
}