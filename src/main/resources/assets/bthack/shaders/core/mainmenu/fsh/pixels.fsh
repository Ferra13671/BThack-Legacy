#version 120
/*
 * Original shader from: https://www.shadertoy.com/view/lt33RH
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
float seed = 0.69;//----------------------------------------------------------starting seed - change for different looks
const float particles = 100.0;//----------------------------------------------change particle count
float res = 50.0;//-----------------------------------------------------------pixel resolution (higher is smaller)
float direction =1.0;//-------------------------------------------------------0.0 for expand, 1.0 for contract

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    vec2 uv = (-iResolution.xy + 2.0*fragCoord.xy) / iResolution.y;
    float clr = 0.0;

    //seed = (seed+floor(iTime));

    float iSphere = 1.05-length(uv);
    if (iSphere>0.0)//remove tests outside of influence sphere
    {

        for (float i=0.0; i<particles; i+=1.0)
        {
            seed+=i+tan(seed);
            vec2 tPos = (vec2(cos(seed), sin(seed)));

            vec2 pPos = vec2(0.0, 0.0);
            float speed=i/particles+.4713*(cos(seed)+1.5)/1.5;
            float timeOffset = iTime*speed+(speed);
            float timecycle = timeOffset-floor(timeOffset);

            pPos=mix(tPos, pPos, 1.0+direction-timecycle);

            pPos = floor(pPos*res)/res;//-----------------------------------------comment this out for smooth version

            vec2 p1 = pPos;
            vec4 r1 = vec4(vec2(step(p1, uv)), 1.0-vec2(step(p1+1.0/res+i/particles/res, uv)));
            float px1 = r1.x*r1.y*r1.z*r1.w*speed;

            clr += px1;
        }
    }
    fragColor = (vec4(clr)*vec4(0.1, 2.5, 0.5, 1.0))*(1.0-length(uv));
}
// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}