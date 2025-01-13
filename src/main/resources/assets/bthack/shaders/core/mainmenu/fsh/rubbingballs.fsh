#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 resolution;

// 'balls are rubbing' by Pudi
// Email: k.a.komissar@gmail.com
// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
//
// Thanks to Flopine, provod, YX, NuSan, slerpy, wwrighter, Shane,
// BigWings, FabriceNeyret, iq, and Blackle for sharing their knowledge

const float PI = acos(-1.);
const float TAU = 2. * PI;

vec3 gauss(vec3 x) {
    return 1. / sqrt(2. * PI) * exp(-(x / 0.5) * (x / 0.5));
}

vec4 hash41(float p) {
    vec4 p4 = fract(vec4(p) * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx);
}

// https://www.desmos.com/calculator/4vcidnhumk
vec3 displace_area(vec3 x, float i) {
    vec4 p = hash41(i) * 2. - 1.;
    vec3 q = hash41(i + 10.).xyz * 2. - 1.;
    float r = 2.;
    return exp(-4. * length(x) * length(x)) *
           (1. * p.x * gauss(q + p.y * r) + 1. * p.z * gauss(q + p.w * r));
}

float sd_sphere(vec3 p) {
    return length(p) - 0.2;
}

const int BALL_COUNT = 15;
vec3 BALLS[BALL_COUNT];

vec3 erot(vec3 ax, vec3 p, float a) {
    return mix(dot(ax, p) * ax, p, cos(a)) + cross(ax, p) * sin(a);
}

mat2 rot(float a) {
    float c = cos(a), s = sin(a);
    return mat2(c, -s, s, c);
}

void update_balls(float t) {
    for (int i = 0; i < BALL_COUNT; ++i) {
        float a = float(i) / float(BALL_COUNT) * TAU + t * 0.5;
        vec3 p = 5. * vec3(cos(a), sin(a) * cos(a), 0.);
        p += 6. * displace_area(p, float(i));

        float orientation = PI / 4. * smoothstep(-1., 1., p.x);
        p.yz *= rot(orientation  + 3. * PI / 4.);
        BALLS[i] = 3. * p;
    }
}

float bfield(vec3 p) {
    float force = 0.;
    for (int i = 0; i < BALL_COUNT; ++i) {
        vec3 b = p - BALLS[i];
        float r = 10. - length(p);
        float intensity = (smoothstep(0., 4., r) * 2.5 + 0.9) / dot(b, b);
        /* float intensity = 1. / dot(b, b); */

        force += intensity;
    }

    return 1.0 - force;
}

float glow_r = 0.;
float glow_b = 0.;
float map(vec3 p) {
    float time = time;
    float dist = 1e5;
    vec3 off = hash41(time * 7.).xyz * 0.03;

    dist = bfield(p);

    // sp4ghet https://www.shadertoy.com/view/7sKXRV
    {
        vec3 q = p - vec3(10.0, 0.0, 0.0) + off;
        q.yz *= rot(PI / 1.6);
        q.xz *= rot(PI / 3.2);
        float sp = length(q.xz - .03 * q.y * q.y) - .04;
        glow_r += .08 / (sp * sp + .02);
        dist = min(dist, sp);
    }

    {
        vec3 q = p - vec3(-10.0, 0.0, 0.0) + off;
        q.yz *= rot(PI / 0.6);
        float sp = length(q.xz + .029 * q.y * q.y) - .001;
        glow_b += .08 / (sp * sp + .02);
        dist = min(dist, sp);
    }

    return dist;
}

vec3 get_normal(vec3 p) {
    mat3 k = mat3(p,p,p) - mat3(0.001);
    return normalize(vec3(map(p)) - vec3(map(k[0]), map(k[1]), map(k[2])));
}

vec3 sky(vec3 rd){
    vec3 col = vec3(0.);
    col += mix(0.12 * vec3(0.15, 0.1, 1.0), 0.1 * vec3(0.85, 0.9, 1.0),
               0.5 + 0.5 * normalize(rd).y);
    col += smoothstep(0.2, 1.5, dot(rd, normalize(vec3(0., -1., 0.)))) * 1.1 *
           vec3(0.67843, 0.67451, 0.709);
    col += smoothstep(.2, 1.0, dot(rd, normalize(vec3(10, 1, 1)))) * 0.1 *
           vec3(0.3647, 0.2902, 0.63137);

    return col;
}

vec3 trace(in vec3 ro, in vec3 rd, in float t, in float tmax) {
    float t_prev = t;
    vec3 accum = sky(rd);// vec3(1.);
    for(int bounce = 0; bounce < 2; bounce++){
        for (int i = 0; i < 25; i++) {
            float d = map(ro + rd * t);
            if (d < 0.01 * t) {
                // adjust samples
                for (int i = 0; i < 5; ++i) {
                    float t_mean = (t_prev + t) / 2.;
                    if (map(ro + rd * t_mean) < 0.01 * t_mean) { t = t_mean;
                    } else { t_prev = t_mean; }
                }

                vec3 pos = ro + rd * t;
                vec3 nor = get_normal(pos);

                // bottom light
                accum += 0.1 * clamp(0.5 - 0.5 * nor.y, 0., 1.);

                float fresnel = mix(0.003, 1., pow(1. - dot(-rd, nor), 5.));
                accum *= fresnel;

                accum += 0.01 * sky(reflect(rd, nor));

                ro = pos + nor * .01;
                rd = reflect(rd, nor);
                break;
            }
            t_prev = t;
            t += d * 1.5;
            if (t > tmax) {
                /* accum += sky(reflect(rd, nor); */
                return accum;
            };
        }
    }
    return accum;
}

void main( ) {
    vec2 uv = (gl_FragCoord.xy/resolution.xy * 2. - 1.)
            * vec2(resolution.x / resolution.y, 1.);



    update_balls(time);

    vec3 ro = vec3(0., 0., -26.);
    vec3 rd = normalize(vec3(uv, 2.));

    float t_max = 36.;
    vec3 col = sky(rd);
    col = trace(ro, rd, 0., t_max);

    vec3 cglow_r = vec3(.9, .15, .1) ;
    vec3 cglow_b =  vec3(0.1, 0.2, 0.9);
    col += cglow_r * glow_r + cglow_b * glow_b;

    float scale = 70.;
    float light = dot(uv, uv) * 0.20 - 30.9;
    float dir = dot(uv * scale, normalize(vec2(-1, 1)));
    float pix = fwidth(uv * scale).x;
    float stripes = abs(fract(dir) - 0.5) + (light * 0.5 - 0.5);
    col *= mix(vec3(0.), vec3(1.), smoothstep(pix, -pix, stripes));

    gl_FragColor = vec4(pow(col, vec3(0.4545)),1.0);
}