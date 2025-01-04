#version 120
#ifdef GL_ES
precision mediump float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;

// shadertoy emulation
#define iTime time
#define iResolution vec3(resolution,1.)


#define R(p,a,r)mix(a*dot(p,a),p,cos(r))+sin(r)*cross(p,a)
#define H(h)(cos((h)*6.3+vec3(0,23,21))*.5+.5)
void mainImage(out vec4 O, vec2 C)
{
    O-=O;
    vec3 p,r=iResolution,
    d=normalize(vec3((C-.5*r.xy)/r.y,1));
    float g=0.,e,s;
    for(float i=0.;i<59.;++i)
    {
        p=g*d;
        p.z-=.6;
        p=R(p,normalize(vec3(1,2,3)),iTime*.3);
        s=4.;
        for(int j=0;j<8;++j)
            p=abs(p),p=p.x<p.y?p.zxy:p.zyx,
            s*=e=1.8/min(dot(p,p),1.3),
            p=p*e-vec3(5,1.5,12.02+cos(time)/0.1096);
        g+=e=length(p.xz)/s;
        O.rgb+=mix(r/r,H(log(s)),.7)*.08*exp(-i*i*e);
    }
    O=pow(O,vec4(5));
 }

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
    gl_FragColor.a = 1.;
}