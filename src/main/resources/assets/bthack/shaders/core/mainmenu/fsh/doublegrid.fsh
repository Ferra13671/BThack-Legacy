#version 120
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

mat2 rot(float a)
{
  float s = sin(a);
  float c = cos(a);
  return mat2(c, s, -s, c);
}

// ret.x  - distance to border
// ret.y  - distance to center
// ret.zw - cell uv
// id - cell coordinates
vec4 SquareGrid(vec2 uv, out vec2 id)
{
    uv += 0.5;
    vec2 fs =  fract(uv)-0.5;
    id = floor(uv);
    vec2 d = abs(fs)-0.5;
    float edge = length(max(d,0.0)) + min(max(d.x,d.y),0.0);
    return vec4(abs(edge),length(fs),fs.xy);
}

float GridF(vec2 uv)
{
    //uv.y += sin(time*3.3+uv.x*5.0)*0.05; // distort
    float fade = (abs(uv.y)-.1)/.9;


    float t1 = fract(iTime*0.1);
    float t = fract(iTime);
    vec2 v = vec2(uv.x*abs(1.0/uv.y),abs(1.0/uv.y));
    v += vec2(sin(t1*6.28),t);

    vec2 id;
    vec4 gg = SquareGrid(v,id);
    float grid = pow(1.0-gg.x,2.0);

    float pulsegrid = pow(grid,sin(iTime*2.)*1.+5.);
    pulsegrid = max( pulsegrid,smoothstep(.93,.96,grid)*2.)*fade;
    return pulsegrid;
}

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    // Pixel coordinates
    vec2 uv = (2.0*fragCoord-iResolution.xy)/iResolution.y;

    //uv *= rot(iTime*0.3);
	float d = length(uv);
	uv.y += sin(time+d*1.5)*0.1;


    vec3 col = vec3(mix(-1.,.9,uv.y+0.5),0.,.9) + vec3(1.2);
    col *= GridF(uv);

    // Output to screen
    fragColor = vec4(col, 1.0);
}

// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}