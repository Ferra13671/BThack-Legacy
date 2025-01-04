#version 120
/*
 * Original shader from: https://www.shadertoy.com/view/dtXGW4
 */

#ifdef GL_ES
precision highp float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;

// Protect glslsandbox uniform names
#define time        stemu_time

// shadertoy emulation
float iTime = 0.;
#define iResolution resolution
const vec4 iMouse = vec4(0.);

// --------[ Original ShaderToy begins here ]---------- //
// Rough Seas, by Dave Hoskins.
// https://www.shadertoy.com/view/dtXGW4

// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// By David Hoskins, 2022.

// Inspiration was from: https://www.istockphoto.com/search/more-like-this/882229368?assettype=film&phrase=rough%20sea


#define FAR 1000.
#define FOG_COLOUR vec3(.4,.4,.4)
#define SKY_TOP vec3(.1, 0.1, 0.15)

vec3 camPos;
float time = 0.;
vec3 skyColour;
const vec3 sunDir = normalize(vec3(4,8,18));
//#define EXPORT_PARAMS

//------------------------------------------------------------------------------
// Hashes from here: https://www.shadertoy.com/view/4djSRW
float hash12(vec2 p)
{
	vec3 p3  = fract(vec3(p.xyx) * .1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}
vec2 hash22(vec2 p)
{
	vec3 p3 = fract(vec3(p.xyx) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xx+p3.yz)*p3.zy);
}

float hash13(vec3 p3)
{
	p3  = fract(p3 * .1031);
    p3 += dot(p3, p3.zyx + 31.32);
    return fract((p3.x + p3.y) * p3.z);
}


// Pretty basic smoothed noise...
//------------------------------------------------------------------------------
float noise2D(in vec2 p)
{
	vec2 f = fract(p);
    p = floor(p);
    f = f * f * (3.0 - 2.0 * f);
    float res = mix(mix(hash12(p),
						hash12(p + vec2(1.0, 0.0)), f.x),
					mix(hash12(p + vec2(0.0, 1.0)),
						hash12(p + vec2(1.0, 1.0)), f.x), f.y);
    return res;
}

float noise3D(in vec3 p)
{
    const vec2 add = vec2(1.0, 0.0);

    vec3 f = fract(p);
    f *= f * (3.0-2.0*f);
    p = floor(p);

    float h = mix(
                    mix(mix(hash13(p), hash13(p + add.xyy),f.x),
                        mix(hash13(p + add.yxy), hash13(p + add.xxy),f.x), f.y),
                    mix(mix(hash13(p + add.yyx), hash13(p + add.xyx),f.x),
                        mix(hash13(p + add.yxx), hash13(p + add.xxx),f.x), f.y),
                 f.z);
    return h*h*h*2.;
}


//------------------------------------------------------------------------------
// A very basic sky...
vec3 sky(vec3 dir)
{

    return mix(FOG_COLOUR, SKY_TOP ,abs(dir.y)*1.7);
}

//-----------------------------------------------------------------
// This creates the sea, it's complexity is governed by the incoming iteration count...
const float COSR = cos(.43);
const float SINR = sin(.52);
const mat2 rot2D = mat2(COSR, SINR, -SINR, COSR) * 1.4;
float oceanFundamental(vec2 p, float d, float tim, float iter)
{
    float a =noise2D(p*.01)*8.+3.0;
    float h = 0.0;
    float it = 1./iter;
    float spr = 0.0;
    p.x -= tim*5.0;

    p *= .025;// ...Scale it

    float i = 0.0;
    for (int ii=0;ii<100;++ii)
    {
        if (i > 1.0)
            break;
        float t = (1.08-i) * tim;
        float r =noise2D(p*2.1+t) * i;
        vec2 y1 = (cos(p-t)+1.0);
        vec2 y2 = (1.0-abs(sin(p-t)));

        y1 = mix(y1, y2, r);

        float s = y1.x + y1.y;

        h += s*a;

        a *= .59;
        p = p * rot2D;
        p += 19.9;
        i += it;
    }

    return h;
}

// Map the ocean relative to the point...
//-----------------------------------------------------------------
float map(in vec3 p, in float d, float iter)
{
    float h = oceanFundamental(p.xz, d, time, iter);
    return p.y-h;
}

// Bog standard ray marching, there's so much noise that any misses get lost...?
//-----------------------------------------------------------------
vec2 rayMarch(vec3 p, vec3 dir)
{
    float d = 0.0;
    float spr = 0.0;
    for ( int i = 0; i < 120; i++)
    {
        vec3 pos = p + dir*d;

        float hh = oceanFundamental(pos.xz, d, time, 7.);
        float h = pos.y-hh;

        if (h < 0.1 || d > FAR)
        {
            break;
        }
        float wind = noise3D(pos*.08) * noise3D(pos*3.5+vec3(0, time*h*.1,0));
        spr += max(20.-h, 0.0) * smoothstep(20.0, .0,max(h, 0.0))*smoothstep(FAR, 150.0,d)*smoothstep(30., 80.0, hh)
        * wind;


        //if (h < 0.0) h *= .5;
        d+= h*.7;
    }
    return vec2(d, min(spr*.03, 1.0));
}

//------------------------------------------------------------------------------
// Get a view of pixel using Euler...
mat3 viewMat (float ay, float az)
{
  vec2 o, ca, sa;
  o = vec2 (ay, az);
  ca = cos (o);
  sa = sin (o);
  return mat3 (ca.y, 0., - sa.y, 0., 1., 0., sa.y, 0., ca.y) *
         mat3 (1., 0., 0., 0., ca.x, - sa.x, 0., sa.x, ca.x);
}

//------------------------------------------------------------------------------

// I forgot where this came from, it using a higher iteration than the ray march...
vec3 normal(vec3 pos, float ds)
{
    ds *= 2./iResolution.y;
    ds = max(ds*ds, .1);

    float c = map(pos, 0., 14.);
    vec2 eps_zero = vec2(ds, 0.0);
    return normalize(vec3(map(pos + eps_zero.xyy, 0.0, 14.),
                          map(pos + eps_zero.yxy, 0.0, 14.),
                          map(pos + eps_zero.yyx, 0.0, 14.)) - c);
}

//------------------------------------------------------------------------------

// I was using my 2 tweet water caustic here,
// but some compilers opimized it broken with the rest of the code
// So I opted for a basic voronoi cell thing...
float waterPattern(vec2 p)
{
    p *=.02;
    vec2 n = floor(p);
    vec2 f = fract(p);
    float wp = 1e10;
    for (int i = -1;i<=1;i++)
    {
        for (int j = -1;j<=1;j++)
        {
            vec2 g = vec2(i, j);
            vec2 o = hash22(n+g);

            vec2 r = g + o - f;
            float d = dot(r, r);
            if (d < wp)
            {
                wp = d;
            }
        }
    }
    return pow(wp, 3.5);
}


//------------------------------------------------------------------------------
float waveDepth(vec3 p, vec3 dir)
{
    float d = 0.0;
    for( float i = 3.0; i < 25.0; i+=5.)
    {
        float h = map(p + dir*i, i, 7.);
        if (h > 0.) break;
        d += -h;
    }
    return clamp(1.0-d*.02, 0.0, 1.0);
}

//------------------------------------------------------------------------------
vec3 lighting(vec3 pos, vec3 nor, in vec3 dir,in vec3 mat)
{
    vec3 col;
    col = mat * max(dot(sunDir, nor), 0.0);
    vec3 ref = reflect(dir, nor);
    float fres = clamp(pow( 1.+dot(nor, dir), 5. ), 0.0, 1.0);
    col = mix(col, sky(nor), .3);
    col = mix(col, sky(ref), fres);
    return col;
}

//------------------------------------------------------------------------------
vec3 diffuse(in vec3 pos, in vec3 nor, in float dep)
{
    pos.x -= time*1.8;
    vec3 mat = vec3(.1,.1,.12);
    float h = smoothstep(0., 1.0,nor.y);

    mat += h*.1;

    mat = mix(mat, vec3(.3,.7,.7), dep);


    // Add different frequencies of voronoi cells...
    float foam = waterPattern(pos.xz*vec2(.5,1.)+99.)*15.;
    foam += waterPattern(pos.xz*3.63)*10.;
    foam += waterPattern(pos.xz*12.)*3.;

    foam = clamp(foam, 0.0, 1.0);


    mat = mat+foam * dep*dep*3.;

    return mat;
}

//------------------------------------------------------------------------------
// Exponential fader...
float fader(float edge0, float edge1, float x)
{
    float t = (x - edge0) / (edge1 - edge0);
    return  clamp(exp((t-.9825)*3.)-.0525, 0.0, 1.0);
}

//------------------------------------------------------------------------------
void mainImage( out vec4 outCol, in vec2 coord )
{
    // Take into account non-square viewport to keep aspect ratio of shapes..
    // Zero in centre and .5 at the max Ys
    vec2 uv = (coord-iResolution.xy*.5)/iResolution.y;

    vec2 mouse = vec2(0);
    if (iMouse.z > 0.) mouse = (iMouse.xy-iResolution.xy*.5) /iResolution.y;


    vec3 colour = vec3(0);
    // Find a good point in time with time blurring for the top of the display...
    float f = coord.y/iResolution.y;
    time = iTime+30.;

    // Set colour to zero then call the rayMarcher to get distant object...

    camPos = vec3(time*.01,100,0);
    float h = (sin(time*.65)+1.0)*40.+ 10.;

    float oce = 0.0;

    // Bounce along the average wave height for a set time...
    for (float i = 0.0; i < 4.0; i++)
    {
        oce += oceanFundamental(camPos.xz, 0.0, time + i, 7.0);
    }
    oce = (oce / 4.0)+140.0;
    oce= h-oce;
    if (oce < 0.0)
    {
        // Don't lock any movement - it's the sea!
        h = h+pow(-oce, .3)*4.;
    }


    // Setup camera...
    vec3 col;
    vec3 dir  = vec3(0,0, 1.);
    dir = viewMat (uv.y -.3, uv.x-2.+time*.25 + mouse.x*6.28) * dir;

    camPos.y = h;
    vec2 dis = rayMarch(camPos, dir);


    if (dis.x < FAR)
    {
        // The position is the start position plus the normalised direction X distance...
        vec3  pos = camPos + dir * dis.x; // ...wave hit position
        vec3  nor = normal(pos, dis.x);   // ... Normal
         // The depth of the wave in forward direction, it's simple but effective in helping the water transparent effect...
        float dep = waveDepth(pos+dir*.3, dir);
        vec3  mat = diffuse(pos, nor, dep);

        col = lighting(pos, nor, dir, mat);
        col = mix(col, FOG_COLOUR, smoothstep(250.0, FAR, dis.x));
    }else
    {
        col = sky(dir);
    }
    colour += col;


    colour = mix(col, vec3(.55,.56,.59),dis.y);

    // Some adjustment..
    colour = colour*.5 + smoothstep(0.0, 1.0, colour)*.5;

    vec2 xy = coord/iResolution.xy;
    colour *= 0.5 + .5*pow( 80.0*xy.x*xy.y*(1.0-xy.x)*(1.0-xy.y), .5);  // ...Vignette.
    // Fade out at five minutes...
    #ifdef EXPORT_PARAMS
    outCol = vec4(fader(0.0, 4.0, iTime) * fader(299.0, 294.0, iTime)*sqrt(colour), 1);
    #else
    outCol = vec4(fader(0.0, 4.0, iTime) * sqrt(colour), 1);
    #endif
}
// --------[ Original ShaderToy ends here ]---------- //

#undef time

void main(void)
{
    iTime = time;
    mainImage(gl_FragColor, gl_FragCoord.xy);
}