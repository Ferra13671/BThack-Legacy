#version 120
/*
 * Original shader from: https://www.shadertoy.com/view/dly3Dz
 */

#ifdef GL_ES
precision highp float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;

// shadertoy emulation
#define iTime time
#define iResolution resolution

// --------[ Original ShaderToy begins here ]---------- //
#define st(a, b, t) smoothstep(a, b, t)
#define clamp01(x) clamp(x, 0., 1.)
#define DEBUG 0
#define MIRRORED 1
#define PI 3.141592
#define blur .003

mat2 rotate2d(float _angle){
    return mat2(cos(_angle),-sin(_angle),
        sin(_angle),cos(_angle));
}

float remap01(float a, float b, float t){
    return clamp01((t-a)/(b-a));
}

float remap(float a, float b, float c, float d, float t){
    return remap01(a, b, t) * (d-c) + c;
}

// rect.xy = point1
// rect.zw = point2
// rectangle between point1 and point2
vec2 within(vec2 uv, vec4 rect){
    return (uv-rect.xy)/(rect.zw-rect.xy);
}

vec2 withinRect(vec2 uv, vec2 origin, vec2 size){
    vec4 originRect = vec4(origin-size/2., origin+size/2.);
    return (uv-originRect.xy)/(originRect.zw-originRect.xy);
}

vec2 withinRect(vec2 uv, vec2 origin, vec2 size, float angle){
    vec4 centeredRect = vec4(-size/2., size/2.);

    uv -= origin;
    uv *= rotate2d(angle);
    return (uv-centeredRect.xy)/(centeredRect.zw-centeredRect.xy);
}

vec2 withinSq(vec2 uv, vec2 origin, float len){
    return withinRect(uv, origin, vec2(len));
}

vec2 withinSq(vec2 uv, vec2 origin, float len, float angle){
    return withinRect(uv, origin, vec2(len), angle);
}

vec4 maskSq(vec2 uv, vec4 col, vec2 origin, float len){
    len *= .5;
    if(uv.x < origin.x - len || uv.x > origin.x + len
        || uv.y < origin.y - len || uv.y > origin.y + len){
        col.a = 0.;
    }
    return col;
}

float roundedBox(vec2 p, vec2 size, float r) {
    return length(max(abs(p)-size+r,0.0)) - r;
}

// gradientAngle = direction of gradient in radians
// gradientMult = set how long or short the gradient is
// gradientDelta = offset in the direction of the gradientAngle
vec4 Eyebrow(vec2 uv, float gradientAngle, float gradientMult, float gradientDelta){
    uv -= .5;
    vec4 col = vec4(9.46, .22, .06, 1.);
    vec4 lightCol = vec4(.72, .45, .063, 1.);

    vec2 gradientUv = vec2(uv) * rotate2d(gradientAngle);

    col.rgb = mix(col.rgb, lightCol.rgb, gradientUv.x * gradientMult + gradientDelta);

    vec2 q = vec2(uv);
    q.y += q.x*q.x*1.3;
    q.y *= 1.8;
    //uv.y += uv.x;
    float d = length(q);

    col.a = st(.5, .48, d);
    return col;
}

vec4 Eye(vec2 uv, vec2 pupilOffset){
    uv -= .5;
    vec4 col = vec4(1.);

    float d = length(uv);
    float mask = st(.5, .5-blur, d);

    float shadeSize = .1;
    float edgeShade = st(.5-shadeSize, .5, d);
    col.rgb = mix(col.rgb, vec3(.5), edgeShade);

    d = length(uv - pupilOffset);
    float pupilSize = .15;
    float pupil = st(pupilSize, pupilSize-blur, d);
    col.rgb = mix(col.rgb, vec3(.2), pupil);

    col.a = mask;

    return col;
}

vec4 Teeth(vec2 uv){
    vec4 col = vec4(1.);

    vec2 q = vec2(uv);
    q.y += .1*cos(q.x*3.);
    q.y -= .22;
    q.x -= q.y * .2;

    float d = roundedBox(q, vec2(.44, .05), .02);
    float teeth = 1. - st(.0, .003, d);

    d = abs(uv.x);
    float gradient = st(1.2, -.02, d);
    col.rgb *= gradient;

    col.a = teeth;


    return col;
}

vec4 Mouth(vec2 uv){
    uv -= .5;

    vec4 col = vec4(.36, .22, .06, 1.);
    vec4 lightCol = vec4(.72, .45, .063, 1.);

    float d = length(uv);
    col = mix(lightCol, col, d*3.2);

    vec2 q = vec2(uv);
    q.y += .09*cos(q.x*3.);
    q.y += .007;

    d = roundedBox(q - vec2(.0, .28), vec2(.477, .02), .02);
    float upperLip = 1. - st(.0, .003, d);

    d = length(uv-vec2(.0, .285));
    float size = .48;
    float lowerMouth = st(size, size-.003, d);
    d = length(uv-vec2(.0, 2.));
    size = 1.81;
    lowerMouth *= st(size, size+.003, d);

    col.a = clamp01(lowerMouth + upperLip);

    vec4 teeth = Teeth(uv);

    col = mix(col, teeth, teeth.a);

    return col;
}

vec4 Head(vec2 uv){
    vec4 col = vec4(1., .8, .1, 1.);

    float d = length(uv);


    //edge gradient
    col.rgb = mix(col.rgb, vec3(1., .6, .1), st(.435, .5, d));

    //red border
    col.rgb = mix(col.rgb, vec3(1., .3, .1), st(.4965, .4975 , d));

    float highlight1 = st(.436, .434, d);
    highlight1 *= pow(remap(.41, .0, .55, .0, uv.y), .8);
    col.rgb = mix(col.rgb, vec3(1.), highlight1);

    float highlight2 = st(.31, .0, d) * .65;
    col.rgb = mix(col.rgb, vec3(1.), highlight2);


    col.a = st(.5, .499, d);

    return col;
}

vec4 Tear(vec2 uv){
    uv -= .5;
    uv *= rotate2d(PI / -4.);
    uv.x *= 2.;
    vec4 col = vec4(.0, .55, .77, 1.);
    vec4 colLight = vec4(.13, .77, .92, 1.);
    vec4 colHighlights = vec4(.13, .7, .9, 1.);

    float gradient = st(-.2, .5, uv.x);
    col.rgb = mix(colLight.rgb, col.rgb, gradient);

    // create tear shape
    uv.x /= (.5 - uv.y) * 1.2 + .3 * sin(sqrt(.5 + uv.y)*1.6);

    // bend tear using -x*x shape
    uv.x -= -uv.y*uv.y*.5;

    float d = length(uv);
    float size = .5;
    float tear = st(size, size-.003, d);


    vec2 q = vec2(uv);
    q *= 1.08;
    q.y -= .01;
    d = length(q);
    float highlight2Area = st(size, size-.003, d);
    col.rgb = mix(col.rgb, colHighlights.rgb, highlight2Area);
    float highlight2 = st(.2, -1.7, uv.y) * highlight2Area;
    col.rgb = mix(col.rgb, vec3(1.), highlight2);

    q.y -= .05;
    q *= 1.08;
    d = length(q);
    float highlight3Area = st(size, size-.003, d);
    float highlight3 = st(-.1, .8, abs(uv.y + .2)) * highlight3Area;
    col.rgb = mix(col.rgb, vec3(1.), highlight3);

    col.a = tear;

    return col;
}

float sinInterval(float speed, float offset){
    return sin(iTime * speed + offset) * .5 + .5;
}

vec4 Lmao(vec2 uv){
    vec4 col = vec4(1.);

    //mirror right to left
    if(MIRRORED == 1){
        uv.x = abs(uv.x);
    }

    vec2 eyeOrigin = vec2(.2, .11);
    float eyeSize = .2;
    vec2 eyePos = vec2(eyeOrigin);
    eyePos.y -= eyeSize;
    vec2 eyebrowPos = vec2(.3, .13);
    vec2 eyebrowSize = vec2(.25, .1);
    float eyebrowAngle = PI / 4.;
    vec2 bottomEyelidPos = vec2(.205, .025);

    vec2 mouthPos = vec2(.0, -.25);
    float mouthSize = .67;

    vec2 tearPos = vec2(.48, -.17);
    float tearSize = .4;
    float tearAngle = 0.;


    float b = iTime;
    float blinkInterval = (sin(b) + sin(b * 6.) - cos(b * 2.)) * sin(b * 1.4);
    blinkInterval = clamp(blinkInterval * 4., -.5, .5) + .5;
    float sinInterval1 = sinInterval(3., .0);

    eyePos.y += eyeSize * .8 * blinkInterval;
    eyebrowPos += .05 * blinkInterval;
    eyebrowAngle += PI * blinkInterval;
    eyebrowAngle *= .2;
    bottomEyelidPos.y -= .01 * blinkInterval + .003 * sinInterval1;

    mouthPos.y -= .019 * blinkInterval;
    mouthSize += .1 * blinkInterval + .03 * sinInterval1;

    tearAngle += PI * sinInterval(5., .0);
    tearAngle *= .1;


    vec4 head = Head(uv);
    vec4 eye = Eye(withinSq(uv, eyePos, eyeSize), vec2(0.));
    vec4 bottomEyelid = Eyebrow(withinRect(uv, bottomEyelidPos, eyebrowSize), PI, 1.6, .22);
    vec4 eyebrow = Eyebrow(withinRect(uv, eyebrowPos, eyebrowSize, eyebrowAngle), .5 * PI, 1.6, .17);
    vec4 mouth = Mouth(withinSq(uv, mouthPos, mouthSize));
    vec4 tear = Tear(withinSq(uv, tearPos, tearSize, tearAngle));

    eye = maskSq(uv, eye, eyeOrigin, eyeSize);

    col = mix(col, head, head.a);
    col = mix(col, eye, eye.a);
    col = mix(col, bottomEyelid, bottomEyelid.a);
    col = mix(col, eyebrow, eyebrow.a);
    col = mix(col, mouth, mouth.a);
    col = mix(col, tear, tear.a);

    if(DEBUG == 1){
        uv = mod(uv, .1);
        float h = smoothstep(.002, .00205, length(uv.y));
        float v = smoothstep(.002, .00205, length(uv.x));
        col *= h * v;
    }

    return col;
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord/iResolution.xy;
    uv -= .5;
    uv.x *= iResolution.x/iResolution.y;

    fragColor = Lmao(uv);
}
// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}