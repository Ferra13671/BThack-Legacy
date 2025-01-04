#version 120
/*
 * Original shader from: https://www.shadertoy.com/view/4s2cWm
 */

#ifdef GL_ES
precision mediump float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;

// shadertoy emulation
#define iTime time
#define iResolution resolution

// --------[ Original ShaderToy begins here ]---------- //
float distfunc(vec3 p)
{
    p=sin(p);
    p-=0.5;
    return length(p)-0.4;
}

float rand(vec2 co){
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    float time=iTime;
    vec2 p=(2.0*fragCoord.xy-iResolution.xy)/min(iResolution.x, iResolution.y)*0.5;

    float z0=time*28.0/1.0;
    float i=floor(z0);
    float offs=fract(z0);
    float shadow=1.0;

    for (float z=1.0;z<150.0;z+=1.0)
    {
        float z2=z-offs;
        float randz=z+i;
        float dadt=(rand(vec2(randz, 1.0))*2.0-1.0)*0.5;
        float a=rand(vec2(randz, 1.0))*2.0*3.141592+dadt*time;
        float pullback=rand(vec2(randz, 3.0))*4.0+1.0;
        float r=rand(vec2(randz, 4.0))*0.5+1.4;
        float g=rand(vec2(randz, 5.0))*0.5+0.7;
        float b=rand(vec2(randz, 6.0))*0.5+0.7;

        vec2 origin=vec2(sin(randz*0.005)+sin(randz*0.002), cos(randz*0.005)+cos(randz*0.002))*z2*0.002;

        vec2 dir=vec2(cos(a), sin(a));
        float dist=dot(dir, p-origin)*z2;
        float xdist=dot(vec2(-dir.y, dir.x), p-origin)*z2;
        float wobble=dist-pullback+sin(xdist*20.0)*0.05;
        if (wobble>0.0)
        {
            float dotsize=rand(vec2(randz, 7.0))*0.5+0.1;
            float patternsize=rand(vec2(randz, 8.0))*2.0+2.0;
            float pattern=step(dotsize, length(fract(vec2(dist, xdist)*patternsize)-0.5))*0.1+0.9;

            float bright;
            if (wobble<0.2) bright=1.2;
            else if (wobble<0.22) bright=0.9;
            else bright=1.0*pattern;

            fragColor=vec4(30.0*vec3(r, g, b)*shadow/(z2+30.0)*bright, 1.0);
            return;
        }
        else
        {
            shadow*=1.0-exp((dist-pullback)*2.0)*0.2;
        }
    }
    fragColor=vec4(vec3(0.0), 1.0);
}

// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}