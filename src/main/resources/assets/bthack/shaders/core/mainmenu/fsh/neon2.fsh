#version 120
/*
 * Original shader from: https://www.shadertoy.com/view/tlfGRN
 */

#ifdef GL_ES
precision highp float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;

// shadertoy globals
#define iTime time
#define iResolution resolution
const vec4  iMouse = vec4(0.0);

// --------[ Original ShaderToy begins here ]---------- //
#define EPS 0.0001
#define PI 3.14159265359
#define FLT_MAX 3.402823466e+38
#define FLT_MIN 1.1e-38
#define DBL_MAX 1.7976931348623158e+308
#define DBL_MIN 2.2250738585072014e-308

const int maxIterations = 64;
const float stepScale = .9;
const float stopThreshold = .005;

float fov = .65;
float nearClip = 0.;
float farClip = 80.;

struct Surface {
  	float dist;
  	vec3 position;
  	vec3 baseColor;
  	vec3 normal;
  	vec3 emissiveColor;
};

struct Hit {
  	Surface surface;
  	Surface near;
  	vec3 color;
};

float saturate(float s) {
	return clamp(s, 0., 1.);
}

float smin(float a, float b, float k) {
	float res = exp(-k * a) + exp(-k * b);
  	return -log(res) / k;
}

mat2 rot2(float t) {
    return mat2(cos(t), -sin(t), sin(t), cos(t));
}

float scene(vec3 p) {
    vec3 p1 = p;
    p1.xy += vec2(iTime * .8 + 10., iTime * .4 + 20.);
    p1.xy *= rot2(PI * .05);

    vec3 p2 = p;
    p2.yz += vec2(iTime * .4 + 30., iTime * .8 + 40.);
    p2.yz *= rot2(PI * .04);

    vec3 p3 = p;
    p3.xz += vec2(iTime * .8 + 50., iTime * .6 + 60.);
    p3.xz *= rot2(PI / 2. + iTime * .0);

    float m = 6.;

    p1.y += sin(sin(p1.z * 1.2 + iTime * 4.) * .3) * .3;
	p1.x += sin(sin(p1.z * 1. + iTime * 2.) * .4) * .2;
    p1.y = mod(p1.y, m) - m * .5;
    p1.x = mod(p1.x, m) - m * .5;


    p2.y += sin(sin(p2.z * 1.2 + iTime * 4.) * .4) * .4;
	p2.x += sin(sin(p2.z * .5 + iTime * 3.) * .5) * .3;
    p2.y = mod(p2.y, m) - m * .5;
    p2.x = mod(p2.x, m) - m * .5;

    p3.y += sin(sin(p3.z * .8 + iTime * 2.) * .4) * .2;
	p3.x += sin(sin(p3.z * 1.1 + iTime * 3.) * .5) * .4;
    p3.y = mod(p3.y, m) - m * .5;
    p3.x = mod(p3.x, m) - m * .5;

    float c = smin(length(p1.xy), length(p2.xy), 4.);
    c = smin(c, length(p3.xy), 4.);

    return c;
}

Hit rayMarching(vec3 origin, vec3 dir, float start, float end) {
  	Surface cs;
  	cs.dist = -1.;

  	Hit hit;
    hit.color = vec3(0.);

  	float sceneDist = 0.;
  	float rayDepth = start;

  	for(int i = 0; i < maxIterations; i++) {
    	sceneDist = scene(origin + dir * rayDepth);

    	if((sceneDist < stopThreshold) || (rayDepth >= end)) {
     		break;
    	}
	    rayDepth += sceneDist * stepScale;
    	vec3 p = origin + dir * rayDepth;
     	vec3 c = sin((iTime + PI / 2.) * 4. * vec3(.123, .456, .789)) * .4 + .6;
      	hit.color += max(vec3(0.), .09 / sceneDist * c);
  	}

	/*
  	if (sceneDist >= stopThreshold) {
    	rayDepth = end;
  	} else {
    	rayDepth += sceneDist;
  	}
  	*/

	cs.dist = rayDepth;
    hit.surface = cs;

	return hit;
}

vec3 fog(vec3 color, float distance, vec3 fogColor, float b) {
	float fogAmount = 1. - exp(-distance * b);
  	return mix(color, fogColor, fogAmount);
}

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
  	vec2 mouse = iMouse.xy;

  	vec2 aspect = vec2(iResolution.x / iResolution.y, 1.);
  	vec2 screenCoord = (2. * fragCoord.xy / iResolution.xy - 1.) * aspect;

    // displacement
    vec2 uv = screenCoord;
    uv.xy *= rot2(iTime * .07);
    uv.y += sin(screenCoord.x * 2.4 + iTime * .05) * .16;
    uv.x += sin(uv.y * 2.4 + iTime * .1) * .12;

  	// mouse = mouse.xy / iResolution.xy - .5;

  	// camera settings
    //vec3 lookAt = vec3(cos(iTime * .4) * .5, sin(iTime * .3) * .5, 0.);
    float z = iTime * -5.;
  	vec3 lookAt = vec3(0., 0., z - 1.);
    vec3 cameraPos = vec3(0., 0., z);

  	// camera vectors
  	vec3 forward = normalize(lookAt - cameraPos);
  	vec3 right = normalize(cross(forward, vec3(0., 1., 0.)));
  	vec3 up = normalize(cross(right, forward));

  	// raymarch
  	vec3 rayOrigin = cameraPos;
  	vec3 rayDirection = normalize(forward + fov * uv.x * right + fov * uv.y * up);
  	Hit hit = rayMarching(rayOrigin, rayDirection, nearClip, farClip);
  	Surface surface = hit.surface;

  	surface.position = rayOrigin + rayDirection * surface.dist;

  	// color
  	vec3 sceneColor = vec3(0.);

    sceneColor = hit.color;

   	sceneColor = fog(sceneColor, surface.dist, vec3(0.), .065);

    // vignet by channel
    float vignetR = 1. - smoothstep(0., 2.5 + sin(iTime * 1.) * 1.5, length(screenCoord)) * .8;
    float vignetG = 1. - smoothstep(0., 2.5 + cos(iTime * 1.2) * 1.5, length(screenCoord)) * .8;
    float vignetB = 1. - smoothstep(0., 2.5 + sin(iTime * 1.4) * 1.5, length(screenCoord)) * .8;

    sceneColor.x *= vignetR;
    sceneColor.y *= vignetG;
    sceneColor.z *= vignetB;

    // debug distance color
    //sceneColor.rgb = vec3(surface.dist / farClip);

  	fragColor = vec4(sceneColor, 1.);
}

// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}